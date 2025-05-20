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

package com.google.jetstream.presentation.app

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.theme.JetStreamBorder

@Composable
fun UserAvatar(
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.AccountCircle,
    description: String? =
        StringConstants.Composable.ContentDescription.UserAvatar,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val border = JetStreamBorder.copy(shape = CircleShape)

    IconButton(
        onClick = onClick,
        modifier = modifier.semantics {
            if (description != null) {
                contentDescription = description
            }
        },
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = modifier
                .fillMaxSize()
                .borderIndication(interactionSource = interactionSource, focused = border),
        )
    }
}
