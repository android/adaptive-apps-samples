/*
 * Copyright 2026 Google LLC
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

@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.google.jetstream.presentation.theme.styles

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.then
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.Typography

sealed interface ProminentCardStyle {
    val card: Style
    val container: Style
    val scrim: Style

    data object Default : ProminentCardStyle {
        override val card =
            Style {
                size(DpSize(360.dp, 360.dp))
                shape(RoundedCornerShape(16.dp))
                clip(true)
                textStyle(Typography.headlineSmall)
            }
        override val container =
            Style {
                contentPadding(16.dp)
                fillSize()
            }

        override val scrim =
            Style {
                background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.5f),
                            Color.Black.copy(alpha = 0.9f),
                        ),
                    ),
                )
                fillSize()
            }
    }

    data object MediumCardStyle : ProminentCardStyle {
        override val card =
            Default.card then
                Style {
                    size(DpSize(540.dp, 304.dp))
                }

        override val container =
            Style {
                contentPadding(16.dp)
                fillSize()
            }

        override val scrim = Default.scrim then Style
    }

    data class FocusOptimizedProminentCardStyle(val colorScheme: ColorScheme) : ProminentCardStyle {
        override val card =
            Default.card then
                Style {
                    size(DpSize(432.dp, 216.dp))
                    alpha(0.5f)

                    hovered {
                        animate {
                            alpha(1f)
                            border(3.dp, colorScheme.outline)
                        }
                    }
                    focused {
                        animate {
                            alpha(1f)
                            border(3.dp, colorScheme.outline)
                        }
                    }
                }

        override val container =
            Default.container then
                Style {
                    contentPadding(24.dp)
                }

        override val scrim = Default.scrim then Style
    }
}

fun EngagementMode.isFocusOptimized(): Boolean {
    return this is EngagementMode.Leanback ||
        this is EngagementMode.Cabin ||
        this is EngagementMode.Workstation
}

@Composable
fun rememberProminentCardStyle(): ProminentCardStyle {
    val engagementMode = LocalEngagementMode.current
    val colorScheme = MaterialTheme.colorScheme

    return remember(engagementMode, colorScheme) {
        when {
            engagementMode.isFocusOptimized() -> {
                ProminentCardStyle.FocusOptimizedProminentCardStyle(colorScheme = colorScheme)
            }

            engagementMode is EngagementMode.Medium -> {
                ProminentCardStyle.MediumCardStyle
            }

            else -> {
                ProminentCardStyle.Default
            }
        }
    }
}

val LocalProminentCardStyle: ProvidableCompositionLocal<ProminentCardStyle> =
    staticCompositionLocalOf { ProminentCardStyle.Default }
