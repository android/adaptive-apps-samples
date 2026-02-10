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

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.presentation.screens.categories.CategoriesScreenUiState
import com.google.jetstream.presentation.theme.JetStreamTheme

@Preview(device = Devices.PHONE, name = "Phone")
annotation class PhonePreview

@Preview(device = Devices.FOLDABLE, name = "Foldable")
annotation class FoldablePreview

@Preview(device = Devices.TABLET, name = "Tablet")
annotation class TabletPreview

@Preview(device = Devices.DESKTOP, name = "Desktop")
annotation class DesktopPreview

@Preview(device = Devices.TV_1080p, name = "TV")
annotation class TvPreview

@Preview(device = Devices.AUTOMOTIVE_1024p, name = "Auto")
annotation class AutoPreview

@PhonePreview
@FoldablePreview
@TabletPreview
@DesktopPreview
@TvPreview
@AutoPreview
annotation class AdaptivePreview

@Composable
fun JetStreamPreview(content: @Composable () -> Unit) {
    JetStreamTheme {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onSurface
        ) {
            content()
        }
    }
}

val mockCategoryScreenState = CategoriesScreenUiState.Ready(
    listOf(
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
        MovieCategory("Action", "Action"),
    )
)
