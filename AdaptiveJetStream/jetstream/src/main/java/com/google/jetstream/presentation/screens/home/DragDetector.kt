/*
 * Copyright 2025 Google LLC
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

package com.google.jetstream.presentation.screens.home

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun Modifier.dragDetector(
    state: FeaturedMoviesCarouselState,
    dragDetector: DragDetector =
        rememberDragDetector(
            moveToPrevious = state::moveToPreviousItem,
            moveToNext = state::moveToNextItem
        ),
): Modifier =
    pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            dragDetector.update(dragAmount.x)
        }
    }

internal data class DragDetector(
    val threshold: Float,
    val moveToNext: () -> Unit,
    val moveToPrevious: () -> Unit,
) {
    private var delta = 0f

    fun update(delta: Float) {
        this.delta += delta
        if (this.delta > threshold) {
            moveToPrevious()
            reset()
        } else if (this.delta < -threshold) {
            moveToNext()
            reset()
        }
    }

    private fun reset() {
        delta = 0f
    }
}

@Composable
internal fun rememberDragDetector(
    moveToNext: () -> Unit = {},
    moveToPrevious: () -> Unit = {},
    threshold: Dp = 300.dp,
    density: Density = LocalDensity.current,
): DragDetector =
    remember(threshold, density) {
        val thresholdInPx =
            with(density) {
                threshold.toPx()
            }
        DragDetector(thresholdInPx, moveToNext, moveToPrevious)
    }
