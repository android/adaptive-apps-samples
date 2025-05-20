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
import androidx.annotation.OptIn
import androidx.compose.runtime.Immutable
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.jetstream.PlaybackService
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class VideoPlayerScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val player = MutableStateFlow<Player?>(null)

    private val movieDetails = savedStateHandle
        .getStateFlow<String?>(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY, null)
        .map {
            if (it != null) {
                repository.getMovieDetails(movieId = it)
            } else {
                null
            }
        }

    private val isReadyToPlay = MutableStateFlow(false)
    private val currentMediaIem = MutableStateFlow<MediaItem?>(null)

    val uiState = combine(
        movieDetails,
        player,
        isReadyToPlay,
        currentMediaIem
    ) { details, player, isReady, current ->
        when {
            details == null -> VideoPlayerScreenUiState.Error
            player == null -> VideoPlayerScreenUiState.Loading
            player.mediaItemCount == 0 -> {
                preparePlayer(player, details)
                VideoPlayerScreenUiState.Loading
            }

            else -> {
                val detailsForCurrent = movieDetailsForMediaItem(current) ?: details
                VideoPlayerScreenUiState.Done(
                    movieDetails = detailsForCurrent,
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

    @OptIn(UnstableApi::class)
    fun requestPlayer() {
        player.value?.release()
        createMediaController { createdPlayer ->
            createdPlayer.addListener(
                object : Player.Listener {
                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        isReadyToPlay.tryEmit(createdPlayer.isReadyToPlay())
                    }

                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                        currentMediaIem.tryEmit(mediaItem)
                        isReadyToPlay.tryEmit(false)
                    }
                }
            )
            player.tryEmit(createdPlayer)
        }
    }

    fun releasePlayer() {
        player.value?.release()
        viewModelScope.launch {
            player.tryEmit(null)
        }
    }

    private fun createMediaController(
        onControllerCreated: (MediaController) -> Unit
    ) {
        val sessionToken =
            SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, sessionToken).buildAsync()
        future.addListener(
            {
                onControllerCreated(future.get())
            },
            context.mainExecutor
        )
    }

    private fun preparePlayer(player: Player, movieDetails: MovieDetails) {
        if (player.isPlaying) {
            player.stop()
        }
        player.clearMediaItems()
        player.addMediaItem(movieDetails.intoMediaItem())
        player.addMediaItems(movieDetails.similarMovies.map { it.intoMediaItem() })

        player.prepare()
    }

    private suspend fun movieDetailsForMediaItem(mediaItem: MediaItem?): MovieDetails? {
        return if (mediaItem != null) {
            repository.getMovieDetails(mediaItem.mediaId)
        } else {
            null
        }
    }
}

private fun MediaController.isReadyToPlay(): Boolean {
    return playWhenReady && contentDuration > 0
}

@Immutable
sealed class VideoPlayerScreenUiState {
    data object Loading : VideoPlayerScreenUiState()
    data object Error : VideoPlayerScreenUiState()
    data class Done(
        val movieDetails: MovieDetails,
        val player: Player,
        val isReadyToPlay: Boolean
    ) : VideoPlayerScreenUiState()
}

private fun MovieDetails.intoMediaItem(): MediaItem {
    val movie = Movie.from(this)
    return movie.intoMediaItem()
}

private fun Movie.intoMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(id)
        .setUri(videoUri)
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
