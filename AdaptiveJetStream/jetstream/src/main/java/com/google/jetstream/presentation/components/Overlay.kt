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

package com.google.jetstream.presentation.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.gradientOverlay(gradientColor: Color): Modifier =
    drawWithCache {
        val horizontalGradient =
            Brush.horizontalGradient(
                colors =
                    listOf(
                        gradientColor,
                        Color.Transparent,
                    ),
                startX = size.width.times(0.2f),
                endX = size.width.times(0.7f),
            )
        val verticalGradient =
            Brush.verticalGradient(
                colors =
                    listOf(
                        Color.Transparent,
                        gradientColor,
                    ),
                endY = size.width.times(0.3f),
            )
        val linearGradient =
            Brush.linearGradient(
                colors =
                    listOf(
                        gradientColor,
                        Color.Transparent,
                    ),
                start =
                    Offset(
                        size.width.times(0.2f),
                        size.height.times(0.5f),
                    ),
                end =
                    Offset(
                        size.width.times(0.9f),
                        0f,
                    ),
            )

        onDrawWithContent {
            drawContent()
            drawRect(horizontalGradient)
            drawRect(verticalGradient)
            drawRect(linearGradient)
        }
    }
