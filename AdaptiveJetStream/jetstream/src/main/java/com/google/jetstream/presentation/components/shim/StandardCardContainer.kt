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

package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.jetstream.presentation.theme.JetStreamTokens

/**
 * A container for a standard card that displays an image card, title, subtitle, and description.
 * It manages a [MutableStyleState] based on the provided [interactionSource].
 *
 * @param imageCard The composable for the image card part of the container.
 * @param title The composable for the title.
 * @param modifier The modifier to be applied to the container.
 * @param style The style to be applied to the container.
 * @param subtitle The composable for the subtitle.
 * @param description The composable for the description.
 * @param interactionSource The interaction source for the card.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StandardCardContainer(
    imageCard: @Composable (interactionSource: InteractionSource) -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    interactionSource: InteractionSource = remember { MutableInteractionSource() },
) {
    val styleState =
        remember(interactionSource) {
            MutableStyleState(interactionSource = interactionSource)
        }

    Column(modifier = modifier.styleable(styleState = styleState, style)) {
        Box {
            imageCard(interactionSource)
        }
        Column(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ProvideTextStyle(MaterialTheme.typography.titleMedium) { title() }
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                Box(Modifier.styleable { alpha(JetStreamTokens.SUBTITLE_ALPHA) }) { subtitle() }
            }
            ProvideTextStyle(MaterialTheme.typography.bodySmall) {
                Box(Modifier.styleable { alpha(JetStreamTokens.DESCRIPTION_ALPHA) }) { description() }
            }
        }
    }
}
