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

package com.google.jetstream.presentation.screens.favourites.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.Border
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.theme.JetStreamCardShape

@Composable
fun MovieFilterChip(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    FilterChip(
        modifier = modifier
            .padding(end = 16.dp)
            .indication(interactionSource, borderIndication(focused = ChipFocusedBorder)),
        onClick = { onCheckedChange(!isChecked) },
        selected = isChecked,
        leadingIcon = {
            AnimatedVisibility(visible = isChecked) {
                Icon(
                    Icons.Default.Check,
                    contentDescription =
                    StringConstants.Composable.ContentDescription.FilterSelected,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        label = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    softWrap = false,
                    overflow = TextOverflow.Visible,
                )
            }
        }
    )
}

private val ChipFocusedBorder
    @Composable get() = Border(
        stroke = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        shape = JetStreamCardShape
    )
