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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.jetstream.presentation.theme.JetStreamTokens

/**
 * A Card component that supports styling and interaction tracking.
 * This component is built on top of [StyleableSurface] and applies a default card style.
 *
 * @param onClick Callback to be invoked when this card is clicked.
 * @param modifier The modifier to be applied to this card.
 * @param style The [Style] to be applied to this card, which will be combined with the default card style.
 * @param interactionSource The [MutableInteractionSource] to track focus, hover, and press states.
 * @param contentAlignment The alignment of the content within the card.
 * @param enabled Whether this card is enabled and clickable.
 * @param content The content to be placed inside the card.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StyleableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.TopStart,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    StyleableSurface(
        onClick = onClick,
        modifier = modifier,
        style = JetStreamTokens.cardStyle() then style,
        contentAlignment = contentAlignment,
        enabled = enabled,
        content = content,
    )
}
