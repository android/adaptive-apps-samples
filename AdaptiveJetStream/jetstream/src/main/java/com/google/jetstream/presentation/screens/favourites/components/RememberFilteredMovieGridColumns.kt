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

package com.google.jetstream.presentation.screens.favourites.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.window.core.layout.WindowSizeClass
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationComponentType

@Composable
fun rememberFilteredMoviesGridColumns(
    navigationComponentType: NavigationComponentType = rememberNavigationComponentType(),
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
): GridCells {
    return remember(navigationComponentType, windowSizeClass) {
        calculateFilteredMoviesGridColumns(navigationComponentType, windowSizeClass)
    }
}

private fun calculateFilteredMoviesGridColumns(
    navigationComponentType: NavigationComponentType,
    windowSizeClass: WindowSizeClass
): GridCells {
    return when (navigationComponentType) {
        NavigationComponentType.TopBar -> {
            GridCells.Fixed(6)
        }
        else -> {
            windowSizeClass.filteredMoviesGridColumns()
        }
    }
}

private fun WindowSizeClass.filteredMoviesGridColumns(): GridCells {
    return when {
        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> {
            GridCells.Fixed(6)
        }
        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            GridCells.Fixed(4)
        }
        else -> GridCells.Fixed(3)
    }
}
