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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.lifecycle.compose.dropUnlessResumed

/**
 * A clickable surface component that supports styling and interaction tracking.
 * This component uses [StyleableBox] internally and sets the accessibility role to [Role.Button].
 *
 * @param onClick Callback to be invoked when this surface is clicked.
 * @param modifier The modifier to be applied to this surface.
 * @param style The [Style] to be applied to this surface.
 * @param interactionSource The [MutableInteractionSource] to track focus, hover, and press states.
 * @param contentAlignment The alignment of the content within the surface.
 * @param enabled Whether this surface is enabled and clickable.
 * @param content The content to be placed inside the surface.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun StyleableSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    contentAlignment: Alignment = Alignment.TopStart,
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    StyleableBox(
        modifier =
            modifier
                .clickable(
                    onClick = dropUnlessResumed(block = onClick),
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                )
                .semantics {
                    role = Role.Button
                },
        style = style,
        contentAlignment = contentAlignment,
        interactionSource = interactionSource,
    ) {
        content()
    }
}
