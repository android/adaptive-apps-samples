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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.defaultBringIntoViewSpec
import com.google.jetstream.presentation.components.gradientOverlay
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
import com.google.jetstream.presentation.theme.LocalContentPadding

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImmersiveList(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieSelected: (Movie) -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = { },
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf(movieList.first()) }

    ImmersiveList(
        selectedMovie = selectedMovie,
        movieList = movieList,
        isExpanded = isExpanded,
        onMovieFocused = {
            selectedMovie = it
        },
        onMovieSelected = onMovieSelected,
        onExpanded = onExpanded,
        onCollapsed = onCollapsed,
        modifier =
            modifier
                .focusProperties {
                    onEnter = {
                        isExpanded = true
                        true
                    }
                    onExit = {
                        isExpanded = false
                        true
                    }
                }
                .focusGroup(),
    )
}

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalFoundationApi::class)
@Composable
fun ImmersiveList(
    selectedMovie: Movie,
    movieList: List<Movie>,
    isExpanded: Boolean,
    onMovieSelected: (Movie) -> Unit,
    onMovieFocused: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
) {
    val transition =
        updateTransition(targetState = isExpanded, label = "ImmersiveListExpandTransition")
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(transition.currentState) {
        if (transition.currentState) {
            bringIntoViewRequester.bringIntoView()
            onExpanded()
        } else {
            onCollapsed()
        }
    }

    ImmersiveListFrame(
        poster = {
            transition.AnimatedVisibility(
                visible = { it },
            ) {
                val backgroundColor = MaterialTheme.colorScheme.background
                Background(
                    movie = selectedMovie,
                    style = {
                        size(960.dp, 540.dp)
                        background(backgroundColor)
                    },
                    modifier =
                        Modifier
                            .gradientOverlay(
                                MaterialTheme.colorScheme.background.copy(
                                    alpha = 0.7f,
                                ),
                            ),
                )
            }
        },
        title = {
            transition.AnimatedVisibility(
                visible = { it },
            ) {
                val paddingStart = LocalContentPadding.current.start
                Title(
                    movie = selectedMovie,
                    style = {
                        externalPaddingStart(paddingStart)
                    },
                )
            }
        },
        description = {
            transition.AnimatedVisibility(
                visible = { it },
            ) {
                val paddingStart = LocalContentPadding.current.start
                Description(
                    movie = selectedMovie,
                    style = {
                        externalPaddingStart(paddingStart)
                    },
                )
            }
        },
        list = {
            CompositionLocalProvider(
                LocalBringIntoViewSpec provides defaultBringIntoViewSpec(),
            ) {
                ImmersiveMoviesRow(
                    movieList = movieList,
                    onMovieFocused = onMovieFocused,
                    onMovieSelected = onMovieSelected,
                )
            }
        },
        modifier = modifier.bringIntoViewRequester(bringIntoViewRequester),
    )
}

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalGridApi::class)
@Composable
private fun ImmersiveListFrame(
    poster: @Composable () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    list: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    StylableBox(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart,
        style = style,
    ) {
        poster()
        Column {
            title()
            description()
            list()
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun Background(
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style(),
) {
    PosterImage(
        movie = movie,
        modifier = modifier,
        style = style,
    )
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun Title(movie: Movie, modifier: Modifier = Modifier, style: Style = Style) {
    val textStyle = MaterialTheme.typography.displaySmall
    Text(
        text = movie.name,
        style = textStyle,
        modifier =
            modifier.styleable(
                styleState = null,
                {
                    textStyle(textStyle)
                },
                style,
            ),
    )
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun Description(movie: Movie, modifier: Modifier = Modifier, style: Style = Style) {
    val textStyle = MaterialTheme.typography.bodyLarge
    Text(
        text = movie.name,
        style = textStyle,
        modifier =
            modifier.styleable(
                styleState = null,
                {
                    textStyle(textStyle)
                    alpha(0.75f)
                    fontWeight(FontWeight.Light)
                },
                style,
            ),
    )
}
