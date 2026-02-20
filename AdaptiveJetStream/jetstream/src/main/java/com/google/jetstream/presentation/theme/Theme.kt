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

package com.google.jetstream.presentation.theme // ktlint-disable filename

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.jetstream.presentation.screens.categories.LocalCategoryCardAspectRatio
import com.google.jetstream.presentation.screens.categories.LocalCategoryGridGridCells

@Composable
fun JetStreamTheme(
    content: @Composable () -> Unit
) {
    // TODO: Consider refactoring this
    CompositionLocalProvider(
        LocalFeaturedCarouselHeight provides rememberFeaturedCarouselHeight(),
        LocalVerticalCardAspectRatio provides rememberVerticalCardAspectRatio(),
        LocalCardWidth provides rememberCardWidth(),
        LocalProminentCardSize provides rememberProminentCardSize(),
        LocalCategoryGridGridCells provides rememberCategoryGridColumns(),
        LocalCategoryCardAspectRatio provides rememberCategoryCardAspectRatio(),
        LocalContentPadding provides rememberContentPadding(),
    ) {
        MaterialTheme(
            // TODO this is redundant because the dark and light color schemes are the same
            colorScheme = if (isSystemInDarkTheme()) {
                darkColorScheme
            } else {
                lightColorScheme
            },
            shapes = MaterialTheme.shapes,
            typography = Typography,
            content = content
        )
    }
}
