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

package com.google.jetstream.presentation.screens.favourites.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.presentation.components.MovieCard
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.theme.JetStreamBottomListPadding
import com.google.jetstream.presentation.theme.LocalListItemGap

@Composable
fun FilteredMoviesGrid(
    state: LazyGridState,
    movieList: MovieList,
    onMovieClick: (movieId: String) -> Unit,
    modifier: Modifier = Modifier,
    columns: GridCells = GridCells.Fixed(6),
) {
    LazyVerticalGrid(
        state = state,
        modifier = modifier,
        columns = columns,
        verticalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
        horizontalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
        contentPadding = PaddingValues(bottom = JetStreamBottomListPadding),
    ) {
        items(movieList, key = { it.id }) { movie ->
            MovieCard(
                onClick = { onMovieClick(movie.id) },
                modifier = Modifier.aspectRatio(1 / 1.5f),
            ) {
                PosterImage(movie = movie, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
