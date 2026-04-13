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

package com.google.jetstream.presentation.components.shim.stylable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.google.jetstream.presentation.components.shim.borderIndicationStyle
import com.google.jetstream.presentation.components.shim.scaleIndicationStyle
import com.google.jetstream.presentation.theme.JetStreamBorderWidth
import com.google.jetstream.presentation.theme.JetStreamCardShape

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StylableCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable () -> Unit,
) {
    StylableBox(
        modifier =
            modifier
                .clickable(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    indication = null,
                )
                .semantics {
                    role = Role.Button
                },
        style = StylableCardDefaults.style() then style,
        contentAlignment = contentAlignment,
        interactionSource = interactionSource,
    ) {
        content()
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
object StylableCardDefaults {
    val shape =
        Style {
            shape(JetStreamCardShape)
            clip()
        }

    @Composable
    fun style(): Style {
        val borderIndication =
            borderIndicationStyle(
                focused = JetStreamBorderWidth,
            )
        val scaleIndication = scaleIndicationStyle()
        return shape then borderIndication then scaleIndication
    }
}
