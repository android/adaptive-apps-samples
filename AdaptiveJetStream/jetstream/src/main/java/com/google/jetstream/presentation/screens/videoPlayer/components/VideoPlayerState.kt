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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.annotation.IntRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class VideoPlayerState internal constructor(
    val player: Player,
    val isTabletopMode: Boolean = false,
    @IntRange(from = 0) val hideSeconds: Int = 4,
) {
    var isControlsVisible by mutableStateOf(true)
        private set

    fun showControls() {
        isControlsVisible = true
        hideControls()
    }

    private fun hideControls() {
        val operation = if (player.isPlaying) {
            ControlsHideOperation.Hide
        } else {
            ControlsHideOperation.Cancel
        }
        channel.trySend(operation)
    }

    private val channel = Channel<ControlsHideOperation>(CONFLATED)

    private val playerEventFlow = callbackFlow {
        val listener = @UnstableApi
        object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                val event = if (isPlaying) {
                    PlayerEvent.Started
                } else {
                    PlayerEvent.Paused
                }
                trySend(event)
            }

            override fun onVideoSizeChanged(videoSize: VideoSize) {
                val event = if (videoSize.width > 0 && videoSize.height > 0) {
                    PlayerEvent.SizeChanged(videoSize)
                } else {
                    PlayerEvent.SizeUnknown
                }
                trySend(event)
            }
        }
        player.addListener(listener)

        awaitClose {
            player.removeListener(listener)
        }
    }.onEach {
        when (it) {
            PlayerEvent.Started -> hideControls()
            PlayerEvent.Paused -> showControls()
            else -> {}
        }
    }

    val videoSize = playerEventFlow.filter {
        it is PlayerEvent.SizeChanged || it == PlayerEvent.SizeUnknown
    }.map { event ->
        when (event) {
            is PlayerEvent.SizeChanged -> Size.from(event)
            else -> Size(1920f, 1080f)
        }
    }

    suspend fun observe() {
        channel
            .consumeAsFlow()
            .collectLatest { operation ->
                isControlsVisible = if (isTabletopMode) {
                    true
                } else {
                    when (operation) {
                        ControlsHideOperation.Hide -> {
                            delay(hideSeconds * 1000L)
                            false
                        }

                        ControlsHideOperation.Cancel -> {
                            true
                        }
                    }
                }
            }
    }
}

/**
 * Create and remember a [VideoPlayerState] instance. Useful when trying to control the state of
 * the [com.google.jetstream.presentation.screens.videoPlayer.components.VideoPlayerOverlay]-related composable.
 * @return A remembered instance of [VideoPlayerState].
 * @param hideSeconds How many seconds should the controls be visible before being hidden.
 * */
@Composable
fun rememberVideoPlayerState(
    player: Player,
    isTabletopMode: Boolean,
    @IntRange(from = 0) hideSeconds: Int = 4,
): VideoPlayerState {
    return remember(player, isTabletopMode, hideSeconds) {
        VideoPlayerState(
            player = player,
            isTabletopMode = isTabletopMode,
            hideSeconds = hideSeconds,
        )
    }
        .also {
            LaunchedEffect(it) {
                it.observe()
            }
        }
}

private sealed interface PlayerEvent {
    data object Paused : PlayerEvent
    data object Started : PlayerEvent
    data class SizeChanged(val size: VideoSize) : PlayerEvent
    data object SizeUnknown : PlayerEvent
}


private fun Size.Companion.from(event: PlayerEvent.SizeChanged): Size {
    return Size.from(event.size)
}

private fun Size.Companion.from(videoSize: VideoSize): Size {
    return Size(
        width = videoSize.width.toFloat(),
        height = videoSize.height.toFloat()
    )
}

private enum class ControlsHideOperation {
    Hide,
    Cancel,
}
