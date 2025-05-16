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

package com.google.jetstream.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.theme.Padding

@Composable
fun Modifier.requestBringIntoViewOnFocus(
    interactionSource: MutableInteractionSource,
    padding: Padding = Padding(0.dp)
): Modifier {
    val isFocused by interactionSource.collectIsFocusedAsState()
    return requestBringIntoView(isFocused, padding)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Modifier.requestBringIntoView(
    shouldRequest: Boolean,
    padding: Padding = Padding(0.dp)
): Modifier {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val layoutDirection = LocalLayoutDirection.current

    val pxPadding = padding.toPx(LocalDensity.current)
    var rect by remember { mutableStateOf(Rect.Zero) }

    LaunchedEffect(shouldRequest) {
        if (shouldRequest) {
            bringIntoViewRequester.bringIntoView(rect)
        }
    }

    return bringIntoViewRequester(bringIntoViewRequester)
        .onSizeChanged { size ->
            rect = calculateBoundingBox(
                size = size,
                padding = pxPadding,
                layoutDirection = layoutDirection
            )
        }
}

private class PxPadding(val top: Float, val bottom: Float, val start: Float, val end: Float)

private fun Padding.toPx(
    density: Density
): PxPadding {
    return with(density) {
        PxPadding(
            top = top.toPx(),
            bottom = bottom.toPx(),
            start = start.toPx(),
            end = end.toPx()
        )
    }
}

private fun calculateBoundingBox(
    size: IntSize,
    padding: PxPadding,
    layoutDirection: LayoutDirection,
): Rect {
    val (left, right) = when (layoutDirection) {
        LayoutDirection.Ltr -> {
            padding.start to padding.end
        }

        LayoutDirection.Rtl -> {
            padding.end to padding.start
        }
    }
    return Rect(
        top = -padding.top,
        bottom = size.height + padding.bottom,
        left = -left,
        right = size.width + right
    )
}
