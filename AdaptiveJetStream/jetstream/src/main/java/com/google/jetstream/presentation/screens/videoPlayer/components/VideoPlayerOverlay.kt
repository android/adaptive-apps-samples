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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.CinematicScrim
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.theme.JetStreamTheme
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

/**
 * Handles the visibility and animation of the controls.
 */
@Composable
fun VideoPlayerOverlay(
    modifier: Modifier = Modifier,
    isControlsVisible: Boolean = false,
    contentPadding: Padding = LocalContentPadding.current,
    centerButton: @Composable () -> Unit = {},
    subtitles: @Composable () -> Unit = {},
    controls: @Composable () -> Unit = {},
    backButton: @Composable () -> Unit = {},
) {
    Box {
        AnimatedVisibility(isControlsVisible, Modifier, fadeIn(), fadeOut()) {
            Box(
                modifier = Modifier.padding(
                    top = contentPadding.top,
                    start = contentPadding.start
                )
            ) {
                backButton()
            }
        }
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(isControlsVisible, Modifier, fadeIn(), fadeOut()) {
                CinematicScrim(Modifier.fillMaxSize())
            }

            Column {
                Box(
                    Modifier.weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    subtitles()
                }

                AnimatedVisibility(
                    isControlsVisible,
                    Modifier,
                    slideInVertically { it },
                    slideOutVertically { it }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(
                                start = contentPadding.start,
                                end = contentPadding.end,
                                bottom = 32.dp
                            )
                    ) {
                        controls()
                    }
                }
            }
            centerButton()
        }
    }
}

@Preview(device = "id:tv_4k")
@Composable
private fun VideoPlayerOverlayPreview() {
    JetStreamTheme {
        Box(Modifier.fillMaxSize()) {
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                subtitles = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Red)
                    )
                },
                controls = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Blue)
                    )
                },
                centerButton = {
                    Box(
                        Modifier
                            .size(88.dp)
                            .background(Color.Green)
                    )
                },
                backButton = {
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.Cyan)
                    )
                }
            )
        }
    }
}

@PhonePreview
@Composable
private fun VideoPlayerOverlayPreviewForPhone() {
    JetStreamTheme {
        Box(Modifier.fillMaxSize()) {
            VideoPlayerOverlay(
                modifier = Modifier.align(Alignment.BottomCenter),
                subtitles = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Red)
                    )
                },
                controls = {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Blue)
                    )
                },
                centerButton = {
                    Box(
                        Modifier
                            .size(88.dp)
                            .background(Color.Green)
                    )
                },
                backButton = {
                    Box(
                        Modifier
                            .size(32.dp)
                            .background(Color.Cyan)
                    )
                }
            )
        }
    }
}
