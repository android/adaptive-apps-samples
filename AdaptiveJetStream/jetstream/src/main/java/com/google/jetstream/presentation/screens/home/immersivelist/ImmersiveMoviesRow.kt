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

package com.google.jetstream.presentation.screens.home.immersivelist

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.MovieList
import com.google.jetstream.presentation.theme.LocalContentPadding

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ImmersiveMoviesRow(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    style: Style = Style,
    onMovieSelected: (Movie) -> Unit = {},
    onMovieFocused: (Movie) -> Unit = {},
) {
    MovieList(
        movieList = movieList,
        contentPadding = LocalContentPadding.current.intoPaddingValues(),
        modifier =
            modifier
                .focusRestorer()
                .styleable(style = style),
    ) { index, movie ->
        ImmersiveListCard(
            index = index,
            movie = movie,
            onMovieSelected = onMovieSelected,
            onMovieFocused = onMovieFocused,
        )
    }
}
