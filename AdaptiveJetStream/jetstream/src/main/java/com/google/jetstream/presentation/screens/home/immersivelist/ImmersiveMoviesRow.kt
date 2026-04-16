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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.MovieCard
import com.google.jetstream.presentation.components.MovieList
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.colorOverlay
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
import com.google.jetstream.presentation.theme.JetStreamTokens
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

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun ImmersiveListCard(
    index: Int,
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onMovieSelected: (Movie) -> Unit = {},
    onMovieFocused: (Movie) -> Unit = {},
) {
    MovieCard(
        onClick = { onMovieSelected(movie) },
        style = JetStreamTokens.contentColorIndication() then style,
        title = {
            Text(text = movie.name)
        },
        image = {
            ImmersiveListCardPoster(
                index = index,
                movie = movie,
                style = {
                    size(JetStreamTokens.LandscapeCardSize)
                },
                modifier = Modifier.colorOverlay(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f)),
            )
        },
        interactionSource = interactionSource,
        modifier =
            modifier.onFocusChanged {
                if (it.hasFocus) {
                    onMovieFocused(movie)
                }
            },
    )
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun ImmersiveListCardPoster(
    index: Int,
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val textStyle =
        MaterialTheme.typography.displayLarge
            .copy(
                shadow =
                    Shadow(
                        offset = Offset(0.5f, 0.5f),
                        blurRadius = 5f,
                    ),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

    val currentStyle =
        Style {
            textStyle(textStyle)
            fillSize()
        } then style

    StylableBox(
        style = currentStyle,
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        PosterImage(
            movie = movie,
            style = {
                size(JetStreamTokens.LandscapeCardSize)
            },
        )
        Text(
            text = "#${index.inc()}",
            style = textStyle,
        )
    }
}
