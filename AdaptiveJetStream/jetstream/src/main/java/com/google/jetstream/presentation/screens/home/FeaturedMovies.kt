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

package com.google.jetstream.presentation.screens.home

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.colorOverlay
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.FeaturedCarousel
import com.google.jetstream.presentation.components.shim.FeaturedCarouselDefaults
import com.google.jetstream.presentation.components.shim.carouselNavigation
import com.google.jetstream.presentation.components.shim.indication.scaleIndication
import com.google.jetstream.presentation.components.shim.stylable.StylableCard

@Composable
fun FeaturedMovies(
    moveList: List<Movie>,
    onMovieSelected: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    when (featuredMovieCarouselType()) {
        FeaturedMovieCarouselType.Featured -> {
            FeaturedMovieCarousel(
                movieList = moveList,
                onMovieSelected = onMovieSelected,
                modifier = modifier,
                style = style,
            )
        }

        else -> {
            HeroMovieCarousel(
                moveList = moveList,
                onMovieSelected = onMovieSelected,
                modifier = modifier,
                style = style,
            )
        }
    }
}

enum class FeaturedMovieCarouselType {
    Hero,
    Featured,
}

@Composable
fun featuredMovieCarouselType(): FeaturedMovieCarouselType {
    return when (LocalEngagementMode.current) {
        EngagementMode.Leanback, is EngagementMode.Workstation -> FeaturedMovieCarouselType.Featured
        else -> FeaturedMovieCarouselType.Hero
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeroMovieCarousel(
    moveList: List<Movie>,
    onMovieSelected: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    style: Style,
    state: CarouselState = rememberCarouselState(0) { moveList.count() },
) {
    HorizontalCenteredHeroCarousel(
        state = state,
        modifier =
            modifier
                .styleable(style = style)
                .carouselNavigation(
                    state = state,
                    itemCount = moveList.size,
                    coroutineScope = rememberCoroutineScope(),
                ),
    ) {
        val movie = moveList[it]
        CarouselMovieCard(
            movie = movie,
            style = {
                fillSize()
            },
            onMovieSelected = onMovieSelected,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedMovieCarousel(
    movieList: List<Movie>,
    onMovieSelected: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    style: Style,
    state: CarouselState = rememberCarouselState(0) { movieList.count() },
) {
    FeaturedCarousel(
        itemCount = movieList.size,
        state = state,
        nextButton = {
            if (LocalEngagementMode.current.isBackButtonRequired) {
                FeaturedCarouselDefaults.NextButton(
                    itemCount = movieList.size,
                    state = state,
                )
            }
        },
        previousButton = {
            if (LocalEngagementMode.current.isBackButtonRequired) {
                FeaturedCarouselDefaults.PreviousButton(
                    itemCount = movieList.size,
                    state = state,
                )
            }
        },
        modifier = modifier,
        style = style,
    ) { index ->
        val movie = movieList[index]

        CarouselMovieCard(
            movie = movie,
            style = {
                fillSize()
                scaleIndication(focused = 1f)
            },
            onMovieSelected = onMovieSelected,
        )
    }
}

@Composable
fun CarouselMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style,
    onMovieSelected: (movie: Movie) -> Unit = {},
) {
    StylableCard(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart,
        style = style,
        onClick = {
            onMovieSelected(movie)
        },
    ) {
        PosterImage(
            movie = movie,
            style = {
                fillSize()
            },
            modifier = Modifier.colorOverlay(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f)),
        )

        val textStyle = MaterialTheme.typography.titleLarge
        Text(
            text = movie.name,
            style = textStyle,
            modifier =
                Modifier.styleable {
                    textStyle(textStyle)
                    externalPadding(32.dp)
                },
        )
    }
}
