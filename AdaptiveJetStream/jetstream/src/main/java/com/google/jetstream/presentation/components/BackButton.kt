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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.jetstream.presentation.components.feature.rememberIsBackButtonRequired
import com.google.jetstream.presentation.screens.videoPlayer.components.button.VideoPlayerControlsIcon

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Default.ArrowBack,
    description: String? = null,
) {
    VideoPlayerControlsIcon(
        onClick = onClick,
        modifier = modifier,
        icon = icon,
        contentDescription = description,
    )
}

@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    description: String? = null,
    isRequired: Boolean = rememberIsBackButtonRequired()
) {
    if (isRequired) {
        BackButton(
            onClick = onClick,
            modifier = modifier,
            icon = icon,
            description = description,
        )
    }
}
