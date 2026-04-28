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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode

@Composable
fun rememberFilteredMoviesGridColumns(): GridCells {
    val engagementMode = LocalEngagementMode.current
    return remember(engagementMode) {
        engagementMode.filteredMovieGridColumns()
    }
}

private fun EngagementMode.filteredMovieGridColumns(): GridCells {
    return when (this) {
        is EngagementMode.Compact -> GridCells.Fixed(3)
        is EngagementMode.Medium -> GridCells.Fixed(4)
        else -> GridCells.Fixed(6)
    }
}
