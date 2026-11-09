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

@file:OptIn(ExperimentalFoundationStyleApi::class)

package com.google.jetstream.presentation.screens.movies

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.MovieList
import com.google.jetstream.presentation.components.ProminentMovieCard
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding
import com.google.jetstream.presentation.theme.styles.isFocusOptimized

@Composable
fun ProminentMovieList(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: Padding = LocalContentPadding.current,
    onMovieClick: (movie: Movie) -> Unit = {},
) {
    MovieList(
        movieList = movieList,
        modifier = modifier.focusRestorer(),
        contentPadding = contentPadding.copy(top = 0.dp, bottom = 0.dp).intoPaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(itemGap()),
        itemContent = { _, movie ->
            ProminentMovieCard(
                onMovieClick = onMovieClick,
                movie = movie,
            )
        },
    )
}

@Composable
private fun itemGap(): Dp {
    val isFocusOptimized = LocalEngagementMode.current.isFocusOptimized()
    return remember(isFocusOptimized) {
        if (isFocusOptimized) {
            32.dp
        } else {
            8.dp
        }
    }
}
