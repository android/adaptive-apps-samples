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

package com.google.jetstream.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.presentation.theme.JetStreamTokens
import com.google.jetstream.presentation.theme.Typography

@Composable
fun WatchNowButton(
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    style: Style = Style,
    enabled: Boolean = true,
    colors: ButtonColors = WatchNowButtonDefaults.colors,
    shape: Shape = JetStreamTokens.ButtonShape,
    onClick: () -> Unit = {},
) {
    val styleState =
        remember(interactionSource) {
            MutableStyleState(interactionSource = interactionSource)
        }

    Button(
        onClick = onClick,
        modifier = modifier.styleable(styleState = styleState, defaultStyle(), style),
        // Workaround: Button is filled with default container color without the following setting.
        colors = colors,
        shape = shape,
        interactionSource = interactionSource,
        enabled = enabled,
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
        )
        Spacer(Modifier.size(8.dp))
        Text(text = stringResource(R.string.watch_now))
    }
}

object WatchNowButtonDefaults {
    val colors
        @Composable get() =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                disabledContentColor = MaterialTheme.colorScheme.surface,
            )
}

@Composable
private fun defaultStyle(): Style {
    return JetStreamTokens.buttonStyle() then
            Style {
                textStyle(Typography.titleSmall)
            }
}
