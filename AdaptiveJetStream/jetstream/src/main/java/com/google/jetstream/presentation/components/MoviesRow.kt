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

package com.google.jetstream.presentation.components

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.theme.JetStreamTokens
import com.google.jetstream.presentation.theme.LocalContentPadding

@Composable
fun MoviesRow(
    movieList: List<Movie>,
    title: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
    onMovieSelected: (Movie) -> Unit = {},
) {
    MoviesRow(
        movieList = movieList,
        style = style,
        title = { SectionTitle(title = title) },
        modifier = modifier,
    ) { _, movie ->
        MovieCard(
            onClick = { onMovieSelected(movie) },
            style = JetStreamTokens.contentColorIndication(),
            title = {
                Text(text = movie.name)
            },
            image = {
                PosterImage(
                    movie = movie,
                    style = {
                        size(JetStreamTokens.PortraitCardSize)
                    },
                )
            },
        )
    }
}

@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val textStyle = MaterialTheme.typography.headlineLarge
    val contentPaddingStart = LocalContentPadding.current.start

    Text(
        text = title,
        style = textStyle,
        modifier =
            modifier.styleable(
                styleState = null,
                {
                    textStyle(textStyle)
                    externalPaddingBottom(JetStreamTokens.TitleListGap)
                    externalPaddingStart(contentPaddingStart)
                },
                style,
            ),
    )
}

@Composable
fun MoviesRow(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    style: Style = Style,
    title: @Composable () -> Unit = {},
    itemContent: @Composable (index: Int, movie: Movie) -> Unit = { _, _ -> },
) {
    Column(
        modifier =
            modifier
                .styleable(style = style)
                .focusGroup(),
    ) {
        title()
        MovieList(
            movieList = movieList,
            contentPadding = LocalContentPadding.current.intoPaddingValues(),
            modifier = Modifier.focusRestorer(),
            itemContent = itemContent,
        )
    }
}
