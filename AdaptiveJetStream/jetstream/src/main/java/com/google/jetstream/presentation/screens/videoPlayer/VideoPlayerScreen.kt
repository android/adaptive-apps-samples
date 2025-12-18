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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.modifiers.resizeWithContentScale
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.spatial.ContentEdge
import androidx.xr.compose.spatial.Orbiter
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.SpatialExternalSurface
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.fillMaxSize
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.width
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.StereoscopicVisionType
import com.google.jetstream.presentation.components.BackButton
import com.google.jetstream.presentation.components.Error
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.components.desktop.BackNavigationContextMenu
import com.google.jetstream.presentation.components.feature.hasXrSpatialFeature
import com.google.jetstream.presentation.components.feature.isBackButtonRequired
import com.google.jetstream.presentation.components.feature.isSpatialUiEnabled
import com.google.jetstream.presentation.components.feature.rememberImmersiveModeAvailability
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
    val isSpatialUiEnabled = isSpatialUiEnabled()
    LaunchedEffect(isSpatialUiEnabled) {
        videoPlayerScreenViewModel.updateSpatialUiEnabled(isSpatialUiEnabled)
    }

    when (val s = uiState) {
        is VideoPlayerScreenUiState.Loading -> {
            Loading(
                modifier = Modifier
                    .fillMaxSize()
                    // Workaround to make video player visible when spatial UI is enabled.
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        is VideoPlayerScreenUiState.Error -> {
            Error(
                modifier = Modifier
                    .fillMaxSize()
                    // Workaround to make video player visible when spatial UI is enabled.
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        is VideoPlayerScreenUiState.Done -> {
            VideoPlayerScreenContent(
                nowPlayingInfo = s.nowPlayingInfo,
                player = s.player,
                isReadyToPlay = s.isReadyToPlay,
                onBackPressed = onBackPressed,
            )
        }
    }
}

@Composable
private fun VideoPlayerScreenContent(
    nowPlayingInfo: NowPlayingInfo,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit,
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
            nowPlayingInfo = nowPlayingInfo,
            player = player,
            isReadyToPlay = isReadyToPlay,
            onBackPressed = onBackPressed
        )
    }

    LifecycleResumeEffect(player) {
        if (!player.isPlaying) {
            player.play()
        }

        onPauseOrDispose {
            if (player.isPlaying) {
                player.pause()
            }
        }
    }

}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
private fun VideoPlayer(
    nowPlayingInfo: NowPlayingInfo,
    player: Player,
    isReadyToPlay: Boolean,
    onBackPressed: () -> Unit = {}
) {
    val activity = LocalActivity.current

    // TODO: Move to ViewModel for better reuse
    val pulseState = rememberVideoPlayerPulseState()
    val isTabletopMode = currentWindowAdaptiveInfo().windowPosture.isTabletop
    val videoPlayerState = rememberVideoPlayerState(
        player = player,
        isTabletopMode = isTabletopMode,
        hideSeconds = 4
    )
    val videoSize by videoPlayerState
        .videoSize
        .collectAsStateWithLifecycle(Size(1980f, 1080f))

    val hasXrSpatialFeature = hasXrSpatialFeature()
    val isSpatialUiEnabled = isSpatialUiEnabled()
    val spatialConfiguration = LocalSpatialConfiguration.current

    val focusRequester = remember { FocusRequester() }

    val keyboardShortcuts = remember {
        listOf(
            KeyboardShortcut(
                key = Key.F,
                action = {
                    when {
                        !hasXrSpatialFeature -> {
                            activity?.toggleImmersiveMode()
                        }

                        isSpatialUiEnabled -> {
                            spatialConfiguration.requestHomeSpaceMode()
                        }

                        else -> {
                            spatialConfiguration.requestFullSpaceMode()
                        }
                    }
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

    if (isSpatialUiEnabled()) {
        SpatialVideoPlayer(
            nowPlayingInfo = nowPlayingInfo,
            player = player,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            videoSize = videoSize,
            onBackPressed = onBackPressed,
            modifier = Modifier
                .focusRequester(focusRequester)
                .dPadEvents(player, videoPlayerState, pulseState)
                .handleKeyboardShortcuts(keyboardShortcuts)
                .onClick(player, videoPlayerState)
        )
    } else {
        VideoPlayer2D(
            nowPlayingInfo = nowPlayingInfo,
            player = player,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            videoSize = videoSize,
            onBackPressed = onBackPressed,
            modifier = Modifier
                .focusRequester(focusRequester)
                .dPadEvents(player, videoPlayerState, pulseState)
                .handleKeyboardShortcuts(keyboardShortcuts)
                .onClick(player, videoPlayerState)
        )
    }
}

@Composable
private fun SpatialVideoPlayer(
    nowPlayingInfo: NowPlayingInfo,
    player: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    videoSize: Size,
    modifier: Modifier = Modifier,
    zOffset: Dp = 8.dp,
    onBackPressed: () -> Unit = {}
) {
    val density = LocalDensity.current
    var subspaceSize by remember { mutableStateOf(Size.Zero) }

    // Calculate the size of the surface from the video resolution and given subspace size
    val dpSize = remember(density, videoSize, subspaceSize, nowPlayingInfo.stereoscopicVisionType) {
        val size = when (nowPlayingInfo.stereoscopicVisionType) {
            StereoscopicVisionType.SideBySide -> {
                Size(width = videoSize.width / 2, height = videoSize.height)
            }

            StereoscopicVisionType.TopBottom -> {
                Size(width = videoSize.width, height = videoSize.height / 2)
            }

            else -> videoSize
        }

        val scale = when {
            subspaceSize == Size.Zero -> 1.0f
            size.height > size.width -> {
                subspaceSize.height / size.height
            }

            else -> {
                subspaceSize.width / size.width
            }
        }
        with(density) {
            (size * scale).toDpSize()
        }
    }

    Subspace {
        SpatialExternalSurface(
            stereoMode = nowPlayingInfo.stereoscopicVisionType.into(),
            modifier = SubspaceModifier
                .offset(z = zOffset)
                .height(dpSize.height)
                .width(dpSize.width)
        ) {
            onSurfaceCreated { surface ->
                player.setVideoSurface(surface)
            }
        }
        SpatialPanel(
            modifier = SubspaceModifier.fillMaxSize().offset(z = zOffset)
        ) {
            Box(
                modifier = modifier.onPlaced { layoutCoordinates ->
                    // Measure the size of the subspace
                    subspaceSize = Size(
                        width = layoutCoordinates.size.width.toFloat(),
                        height = layoutCoordinates.size.height.toFloat()
                    )
                }
            ) {
                VideoPlayerOverlay(
                    isControlsVisible = videoPlayerState.isControlsVisible,
                    backButton = {
                        BackButton(onClick = onBackPressed)
                    }
                )
                SpatialVideoPlayerControls(
                    movieDetails = nowPlayingInfo.movieDetails,
                    player = player,
                    videoPlayerState = videoPlayerState,
                    modifier = Modifier
                        .focusGroup(),
                )
            }
        }
    }
}

@Composable
private fun VideoPlayer2D(
    nowPlayingInfo: NowPlayingInfo,
    player: Player,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    videoSize: Size,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {}
) {
    val size = if (videoSize == Size.Zero) {
        null
    } else {
        videoSize
    }

    if (videoPlayerState.isTabletopMode) {
        TabletopVideoPlayer(
            modifier = modifier,
            player = player,
            size = size,
            nowPlayingInfo = nowPlayingInfo,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            onBackPressed = onBackPressed
        )
    } else {
        DefaultVideoPlayer(
            modifier = modifier,
            player = player,
            size = size,
            nowPlayingInfo = nowPlayingInfo,
            videoPlayerState = videoPlayerState,
            pulseState = pulseState,
            onBackPressed = onBackPressed
        )
    }
}

@Composable
private fun TabletopVideoPlayer(
    modifier: Modifier = Modifier,
    player: Player,
    size: Size?,
    nowPlayingInfo: NowPlayingInfo,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    onBackPressed: () -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PlayerSurface(
                player = player,
                modifier = Modifier
                    .fillMaxSize()
                    .resizeWithContentScale(
                        contentScale = ContentScale.Fit,
                        sourceSizeDp = size
                    )
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            VideoPlayerControlsInOverlay(
                movieDetails = nowPlayingInfo.movieDetails,
                player = player,
                videoPlayerState = videoPlayerState,
                videoPlayerPulseState = pulseState,
                onBackPressed = onBackPressed,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun DefaultVideoPlayer(
    modifier: Modifier = Modifier,
    player: Player,
    size: Size?,
    nowPlayingInfo: NowPlayingInfo,
    videoPlayerState: VideoPlayerState,
    pulseState: VideoPlayerPulseState,
    onBackPressed: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        PlayerSurface(
            player = player,
            modifier = Modifier
                .fillMaxSize()
                .resizeWithContentScale(
                    contentScale = ContentScale.Fit,
                    sourceSizeDp = size
                )
        )
        VideoPlayerControlsInOverlay(
            movieDetails = nowPlayingInfo.movieDetails,
            player = player,
            videoPlayerState = videoPlayerState,
            videoPlayerPulseState = pulseState,
            onBackPressed = onBackPressed,
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
            offset = 200.dp
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
