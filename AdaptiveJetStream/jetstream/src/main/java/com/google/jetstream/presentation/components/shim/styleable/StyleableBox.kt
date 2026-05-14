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

package com.google.jetstream.presentation.components.shim.styleable

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * A Box component that supports styling through the [Style] API.
 *
 * @param modifier The modifier to be applied to this Box.
 * @param style The [Style] to be applied to this Box.
 * @param contentAlignment The alignment of the content within the Box.
 * @param content The content to be placed inside the Box.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StyleableBox(
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable BoxScope.() -> Unit = {},
) {
    StyleableBox(
        modifier = modifier,
        style = style,
        contentAlignment = contentAlignment,
        interactionSource = null,
        content = content,
    )
}

/**
 * A Box component that supports styling through the [Style] API and tracks interaction states.
 *
 * @param modifier The modifier to be applied to this Box.
 * @param style The [Style] to be applied to this Box.
 * @param contentAlignment The alignment of the content within the Box.
 * @param interactionSource The [InteractionSource] to track focus, hover, and other interaction states.
 * @param content The content to be placed inside the Box.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StyleableBox(
    modifier: Modifier = Modifier,
    style: Style = Style,
    contentAlignment: Alignment = Alignment.TopStart,
    interactionSource: InteractionSource? = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit = {},
) {
    val styleState =
        remember(interactionSource) { MutableStyleState(interactionSource = interactionSource) }
    Box(
        modifier = modifier.styleable(styleState = styleState, style = style),
        contentAlignment = contentAlignment,
        content = content,
    )
}
