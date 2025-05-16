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

package com.google.jetstream.presentation.screens.movies.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.LocalProminentCardSize
import com.google.jetstream.presentation.theme.LocalProminentListItemGap
import com.google.jetstream.presentation.theme.Padding

@Composable
fun ProminentMovieList(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    prominentMovieListScope: ProminentMovieListScope = LocalProminentMovieListScope.current,
    contentPadding: Padding = LocalContentPadding.current,
    onMovieClick: (movie: Movie) -> Unit = {},
) {
    // ToDo: specify the pivot offset to 0.07f
    LazyRow(
        modifier = modifier.focusRestorer(),
        contentPadding = contentPadding.copy(top = 0.dp, bottom = 0.dp).intoPaddingValues(),
        horizontalArrangement = Arrangement.spacedBy(LocalProminentListItemGap.current)
    ) {
        items(movieList) {
            prominentMovieListScope.ProminentMovieCard(
                onMovieClick = onMovieClick,
                movie = it,
                modifier = Modifier.size(LocalProminentCardSize.current)
            )
        }
    }
}
