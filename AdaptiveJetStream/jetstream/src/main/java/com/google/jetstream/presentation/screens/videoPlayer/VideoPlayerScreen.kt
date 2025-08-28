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

import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.ContentEdge
import androidx.xr.compose.spatial.Orbiter
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.presentation.components.BackButton
import com.google.jetstream.presentation.components.Error
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.components.desktop.BackNavigationContextMenu
import com.google.jetstream.presentation.components.feature.rememberImmersiveModeAvailability
import com.google.jetstream.presentation.components.feature.isBackButtonRequired
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.components.shim.onSpaceBarPressed
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerControls
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerOverlay
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerPulse
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.BACK
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerPulse.Type.FORWARD
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerPulseState
import com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerState
import com.google.jetstream.presentation.screens.videoPlayer.components.rememberVideoPlayerPulseState
import com.google.jetstream.presentation.screens.videoPlayer.components.rememberVideoPlayerState
import com.google.jetstream.presentation.screens.videoPlayer.components.toggleImmersiveMode
import com.google.jetstream.presentation.utils.handleDPadKeyEvents

object VideoPlayerScreen {
    const val MOVIE_ID_BUNDLE_KEY = "movieId"
}

/**
 * [Work in progress] A composable screen for playing a video.
 *
 * @param onBackPressed The callback to invoke when the user presses the back button.
 * @param videoPlayerScreenViewModel The view model for the video player screen.
 */
@Composable
fun VideoPlayerScreen(
    onBackPressed: () -> Unit,
    videoPlayerScreenViewModel: VideoPlayerScreenViewModel = hiltViewModel()
) {
    val uiState by videoPlayerScreenViewModel.uiState.collectAsStateWithLifecycle()

    // TODO: Handle Loading & Error states
    when (val s = uiState) {
        is VideoPlayerScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is VideoPlayerScreenUiState.Error -> {
            Error(modifier = Modifier.fillMaxSize())
        }

        is VideoPlayerScreenUiState.Done -> {
            VideoPlayerScreenContent(
                movieDetails = s.movieDetails,
                player = s.player,
                isReadyToPlay = s.isReadyToPlay,
                onBackPressed = onBackPressed
            )
        }
    }

    DisposableEffect(videoPlayerScreenViewModel) {
        videoPlayerScreenViewModel.requestPlayer()
        onDispose {
            videoPlayerScreenViewModel.releasePlayer()
        }
    }
}

@Composable
private fun VideoPlayerScreenContent(
    movieDetails: MovieDetails,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit
) {
    val keyboardShortcuts = remember {
        listOf(KeyboardShortcut(Key.Escape, action = onBackPressed))
    }

    BackHandler(onBack = onBackPressed)
    BackNavigationContextMenu(
        onBackPressed,
        modifier = Modifier.handleKeyboardShortcuts(keyboardShortcuts)
    ) {
        VideoPlayer(
            movieDetails = movieDetails,
            player = player,
            isReadyToPlay = isReadyToPlay,
            onBackPressed = onBackPressed
        )
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(
    movieDetails: MovieDetails,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit = {}
) {
    val activity = LocalActivity.current

    // TODO: Move to ViewModel for better reuse
    val pulseState = rememberVideoPlayerPulseState()

    val videoPlayerState = rememberVideoPlayerState(player = player, hideSeconds = 4)
    val videoSize by videoPlayerState.videoSize.collectAsStateWithLifecycle(null)

    val focusRequester = remember { FocusRequester() }

    val keyboardShortcuts = remember {
        listOf(
            KeyboardShortcut(
                key = Key.F,
                action = {
                    activity?.toggleImmersiveMode()
                }
            ),
            KeyboardShortcut(
                key = Key.K,
                action = {
                    videoPlayerState.showControls()
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.J,
                action = {
                    player.seekBack()
                    pulseState.setType(BACK)
                    videoPlayerState.showControls()
                }
            ),
            KeyboardShortcut(
                key = Key.L,
                action = {
                    player.seekForward()
                    pulseState.setType(FORWARD)
                    videoPlayerState.showControls()
                }
            )
        )
    }

    LaunchedEffect(isReadyToPlay) {
        if (isReadyToPlay) {
            player.play()
            videoPlayerState.showControls()
        }
    }

    LaunchedEffect(videoPlayerState.isControlsVisible) {
        if (!videoPlayerState.isControlsVisible) {
            focusRequester.tryRequestFocus()
        }
    }

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .dPadEvents(player, videoPlayerState, pulseState)
            .handleKeyboardShortcuts(keyboardShortcuts)
            .onClick(player, videoPlayerState),
        contentAlignment = Alignment.Center
    ) {
        PlayerSurface(
            player = player,
            modifier = Modifier.resizeWithContentScale(
                contentScale = ContentScale.Fit,
                sourceSizeDp = videoSize
            )
        )
        VideoPlaybackControls(
            movieDetails = movieDetails,
            player = player,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            onBackPressed = onBackPressed,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onPreviewKeyEvent {
                    if (videoPlayerState.isControlsVisible) {
                        videoPlayerState.showControls()
                    }
                    false
                }
                .focusGroup()
        )
    }
}

@Composable
private fun VideoPlaybackControls(
    movieDetails: MovieDetails,
    player: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    modifier: Modifier = Modifier,
    isSpatialUiEnabled: Boolean = LocalSpatialCapabilities.current.isSpatialUiEnabled,
    onBackPressed: () -> Unit = {}
) {
    Box(modifier = modifier) {
        if (!isSpatialUiEnabled) {
            VideoPlayerControlsInOverlay(
                movieDetails = movieDetails,
                player = player,
                videoPlayerState = videoPlayerState,
                videoPlayerPulseState = pulseState,
                onBackPressed = onBackPressed,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .focusGroup()
            )
        } else {
            VideoPlayerOverlay(
                isControlsVisible = videoPlayerState.isControlsVisible,
                backButton = {
                    BackButton(onClick = onBackPressed)
                }
            )
            SpatialVideoPlayerControls(
                movieDetails = movieDetails,
                player = player,
                videoPlayerState = videoPlayerState,
                modifier = Modifier
                    .focusGroup(),
            )
        }
    }
}

@Composable
private fun SpatialVideoPlayerControls(
    movieDetails: MovieDetails,
    player: Player,
    videoPlayerState: VideoPlayerState,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        videoPlayerState.isControlsVisible
    ) {
        Orbiter(
            position = ContentEdge.Bottom,
            offset = 140.dp
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(32.dp)
                    )
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
                    .padding(32.dp)
            ) {
                VideoPlayerControls(
                    movieDetails = movieDetails,
                    player = player,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun VideoPlayerControlsInOverlay(
    movieDetails: MovieDetails,
    player: Player,
    videoPlayerState: VideoPlayerState,
    videoPlayerPulseState: VideoPlayerPulseState,
    modifier: Modifier = Modifier,
    isImmersiveModeAvailable: Boolean = rememberImmersiveModeAvailability(),
    onBackPressed: () -> Unit = {}
) {
    VideoPlayerOverlay(
        modifier = modifier,
        isControlsVisible = videoPlayerState.isControlsVisible,
        centerButton = { VideoPlayerPulse(videoPlayerPulseState) },
        subtitles = { /* TODO Implement subtitles */ },
        controls = {
            VideoPlayerControls(
                movieDetails = movieDetails,
                player = player,
                isImmersiveModeAvailable = isImmersiveModeAvailable,
            )
        },
        backButton = {
            BackButton(
                onBackPressed,
                description = stringResource(R.string.back_from_video_player),
                isRequired = isBackButtonRequired()
            )
        }
    )
}

private fun Modifier.onClick(
    player: Player,
    videoPlayerState: VideoPlayerState,
): Modifier =
    // ToDo: Remove the onSpaceBarPress modifier when Compose 1.8 is released
    onSpaceBarPressed {
        player.pause()
        videoPlayerState.showControls()
    }.clickable {
        when {
            !videoPlayerState.isControlsVisible -> {
                videoPlayerState.showControls()
            }

            player.isPlaying -> {
                player.pause()
            }

            else -> {
                player.play()
            }
        }
    }

private fun Modifier.dPadEvents(
    player: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState
): Modifier = this.handleDPadKeyEvents(
    onLeft = {
        if (!videoPlayerState.isControlsVisible) {
            player.seekBack()
            pulseState.setType(BACK)
        }
    },
    onRight = {
        if (!videoPlayerState.isControlsVisible) {
            player.seekForward()
            pulseState.setType(FORWARD)
        }
    },
    onUp = { videoPlayerState.showControls() },
    onDown = { videoPlayerState.showControls() },
    onEnter = {
        player.pause()
        videoPlayerState.showControls()
    }
)
