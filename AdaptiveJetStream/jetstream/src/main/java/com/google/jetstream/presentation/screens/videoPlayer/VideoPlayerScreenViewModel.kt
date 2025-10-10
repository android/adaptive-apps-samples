/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.videoPlayer

import android.content.ComponentName
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.concurrent.futures.await
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.jetstream.PlaybackService
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.StereoscopicVisionType
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val playerManager = PlayerManager()

    private val specifiedMovieDetails = savedStateHandle
        .getStateFlow<String?>(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY, null)
        .map {
            if (it != null) {
                repository.getMovieDetails(movieId = it)
            } else {
                null
            }
        }

    private var isSpatialUiEnabledFlow = MutableStateFlow(false)

    private val nowPlayingInfo = combine(
        playerManager.player,
        specifiedMovieDetails,
        playerManager.currentMediaItem,
        isSpatialUiEnabledFlow
    ) { player, movieDetails, mediaItem, isSpatialUiEnabled ->
        when {
            // Wait for the player to be initialized.
            player == null -> {
                null
            }

            // No movie is specified when the video player is opened.
            movieDetails == null && player.mediaItemCount == 0 -> {
                null
            }

            // Use the movie that the player remembers.
            movieDetails == null -> {
                nowPlayingInfoFor(player.currentMediaItem)
            }

            else -> {
                // Check if the specified movie is already in the queue,
                // and if not, create new queue from it.
                val isViewed = (0 until player.mediaItemCount).any { index ->
                    player.getMediaItemAt(index).mediaId == movieDetails.id
                }
                if (isViewed) {
                    nowPlayingInfoFor(mediaItem)
                } else {
                    val stereoscopicVisionType = StereoscopicVisionType.select(movieDetails, isSpatialUiEnabled)
                    val playingInfo =
                        nowPlayingInfoFor(movieDetails.intoMediaItem(stereoscopicVisionType))!!
                    playerManager.prepare(playingInfo, isSpatialUiEnabled)
                    playingInfo
                }
            }
        }
    }

    val uiState = combine(
        playerManager.player,
        playerManager.isReadyToPlay,
        nowPlayingInfo,
    ) { player, isReady, playingInfo ->
        when {
            player == null -> {
                requestPlayer()
                VideoPlayerScreenUiState.Loading
            }

            playingInfo == null -> {
                // Movie should be specified when the video player is opened.
                VideoPlayerScreenUiState.Error
            }

            else -> {
                VideoPlayerScreenUiState.Done(
                    nowPlayingInfo = playingInfo,
                    player = player,
                    isReadyToPlay = isReady,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = VideoPlayerScreenUiState.Loading
    )

    fun requestPlayer() {
        viewModelScope.launch {
            playerManager.request(context)
        }
    }

    fun releasePlayer() {
        viewModelScope.launch {
            playerManager.release()
        }
    }

    fun updateSpatialUiEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            isSpatialUiEnabledFlow.emit(isEnabled)
            updateCurrentMediaItem()
        }
    }

    private suspend fun nowPlayingInfoFor(mediaItem: MediaItem?): NowPlayingInfo? {
        return if (mediaItem != null) {
            val movieDetails = repository.getMovieDetails(mediaItem.mediaId)
            val stereoscopicVisionType = StereoscopicVisionType.select(movieDetails, isSpatialUiEnabledFlow.value)
            return NowPlayingInfo(movieDetails = movieDetails, stereoscopicVisionType = stereoscopicVisionType)
        } else {
            null
        }
    }

    private suspend fun updateCurrentMediaItem() {
        val player = playerManager.player.value
        val currentMediaItem = player?.currentMediaItem
        if (player != null && currentMediaItem != null) {
            val nowPlayingInfo = nowPlayingInfoFor(currentMediaItem)
            if (nowPlayingInfo != null) {
                val mediaItem = nowPlayingInfo.intoMediaItem()
                val currentPosition = player.currentPosition
                player.replaceMediaItem(player.currentMediaItemIndex, mediaItem)
                player.seekTo(currentPosition)
            }
        }
    }

}

private class PlayerManager {
    private val _player: MutableStateFlow<MediaController?> = MutableStateFlow(null)
    val player: StateFlow<MediaController?> = _player
    val currentMediaItem = MutableStateFlow(MediaItem.EMPTY)
    val isReadyToPlay = MutableStateFlow(false)

    suspend fun request(context: Context) {
        release()
        create(context)
    }

    suspend fun release() {
        player.value?.release()
        emit(null)
    }

    fun prepare(
        nowPlayingInfo: NowPlayingInfo,
        isSpatialUiEnabled: Boolean = false,
    ) {
        val p = player.value
        if (p != null) {
            if (p.isPlaying) {
                p.stop()
            }
            p.clearMediaItems()
            p.addMediaItem(nowPlayingInfo.intoMediaItem())
            p.addMediaItems(
                nowPlayingInfo.movieDetails.similarMovies.map {
                    val stereoscopicVisionType = StereoscopicVisionType.select(it, isSpatialUiEnabled)
                    it.intoMediaItem(stereoscopicVisionType)
                }
            )
            p.prepare()
        }
    }

    private suspend fun create(context: Context) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, sessionToken).buildAsync()

        try {
            val controller = future.await()
            controller.addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentMediaItem.tryEmit(mediaItem ?: MediaItem.EMPTY)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    isReadyToPlay.tryEmit(playbackState == Player.STATE_READY)
                }
            })
            val mediaItem = controller.currentMediaItem
            if (mediaItem != null) {
                currentMediaItem.emit(mediaItem)
            }
            emit(controller)
        } catch (_: Exception) {

        }
    }

    private suspend fun emit(player: MediaController?) {
        _player.emit(player)
    }
}

@Immutable
sealed class VideoPlayerScreenUiState {
    data object Loading : VideoPlayerScreenUiState()
    data object Error : VideoPlayerScreenUiState()
    data class Done(
        val nowPlayingInfo: NowPlayingInfo,
        val player: Player,
        val isReadyToPlay: Boolean
    ) : VideoPlayerScreenUiState()
}

data class NowPlayingInfo(
    val movieDetails: MovieDetails,
    val stereoscopicVisionType: StereoscopicVisionType,
) {
    fun intoMediaItem(): MediaItem {
        return movieDetails.intoMediaItem(stereoscopicVisionType = stereoscopicVisionType)
    }

    companion object {

        fun from(
            movieDetails: MovieDetails,
            stereoscopicVisionType: StereoscopicVisionType = StereoscopicVisionType.Mono
        ): NowPlayingInfo {
            return NowPlayingInfo(movieDetails = movieDetails, stereoscopicVisionType = stereoscopicVisionType)
        }
    }
}

private fun MovieDetails.intoMediaItem(
    stereoscopicVisionType: StereoscopicVisionType = StereoscopicVisionType.Mono
): MediaItem {
    val movie = Movie.from(this)
    return movie.intoMediaItem(stereoscopicVisionType = stereoscopicVisionType)
}

private fun Movie.intoMediaItem(
    stereoscopicVisionType: StereoscopicVisionType = StereoscopicVisionType.Mono
): MediaItem {
    val uri = videoUriFor(stereoscopicVisionType) ?: videoUri

    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(uri)
        .setSubtitleConfigurations(
            if (subtitleUri == null) {
                emptyList()
            } else {
                listOf(
                    MediaItem.SubtitleConfiguration
                        .Builder(subtitleUri.toUri())
                        .setMimeType("application/vtt")
                        .setLanguage("en")
                        .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                        .build()
                )
            }
        )
        .setMediaMetadata(intoMediaMetaData())
        .build()
}

private fun Movie.intoMediaMetaData(): MediaMetadata {
    return MediaMetadata.Builder()
        .setTitle(name)
        .setMediaType(MediaMetadata.MEDIA_TYPE_MOVIE)
        .setArtworkUri(posterUri.toUri())
        .setDescription(description)
        .build()
}

private fun StereoscopicVisionType.Companion.select(
    movieDetails: MovieDetails,
    isSpatialUiEnabled: Boolean,
): StereoscopicVisionType {
    return StereoscopicVisionType.select(
        sources = movieDetails.sources,
        isSpatialUiEnabled = isSpatialUiEnabled
    )
}

private fun StereoscopicVisionType.Companion.select(
    movie: Movie,
    isSpatialUiEnabled: Boolean,
): StereoscopicVisionType {
    return StereoscopicVisionType.select(
        sources = movie.sources,
        isSpatialUiEnabled = isSpatialUiEnabled
    )
}

private fun StereoscopicVisionType.Companion.select(
    sources: Map<StereoscopicVisionType, Uri>,
    isSpatialUiEnabled: Boolean,
): StereoscopicVisionType {
    return if (isSpatialUiEnabled) {
        StereoscopicVisionType.select(sources, listOf(StereoscopicVisionType.SideBySide, StereoscopicVisionType.Mono))
    } else {
        StereoscopicVisionType.Mono
    }
}

private fun StereoscopicVisionType.Companion.select(
    sources: Map<StereoscopicVisionType, Uri>,
    preference: List<StereoscopicVisionType>
): StereoscopicVisionType {
    return preference.find { sources.containsKey(it) } ?: StereoscopicVisionType.Mono
}