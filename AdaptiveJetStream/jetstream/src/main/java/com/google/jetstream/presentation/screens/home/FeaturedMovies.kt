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

/**
 * A container composable that switches between different carousel types based on the current
 * [EngagementMode].
 *
 * @param moveList The list of movies to be displayed in the carousel.
 * @param onMovieSelected Callback triggered when a movie in the carousel is clicked.
 * @param modifier The modifier to be applied to the layout.
 * @param style The style to be applied to the carousel.
 */
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

/**
 * Defines the types of carousels available for displaying featured movies.
 */
enum class FeaturedMovieCarouselType {
    Hero,
    Featured,
}

/**
 * Determines the appropriate [FeaturedMovieCarouselType] based on the current [LocalEngagementMode].
 */
@Composable
fun featuredMovieCarouselType(): FeaturedMovieCarouselType {
    return when (LocalEngagementMode.current) {
        EngagementMode.Cabin, EngagementMode.Leanback, is EngagementMode.Workstation -> FeaturedMovieCarouselType.Featured
        else -> FeaturedMovieCarouselType.Hero
    }
}

/**
 * Displays a list of featured movies using a [HorizontalCenteredHeroCarousel].
 * This carousel is typically used in mobile or tablet-like engagement modes.
 *
 * @param moveList The list of movies to display.
 * @param onMovieSelected Callback when a movie is selected.
 * @param modifier The modifier for this composable.
 * @param style The style for the carousel.
 * @param state The state of the carousel.
 */
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

/**
 * Displays a list of featured movies using a [FeaturedCarousel].
 * This carousel is typically used in leanback (TV) or workstation modes.
 *
 * @param movieList The list of movies to display.
 * @param onMovieSelected Callback when a movie is selected.
 * @param modifier The modifier for this composable.
 * @param style The style for the carousel.
 * @param state The state of the carousel.
 */
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

/**
 * A card component representing a movie within a carousel.
 *
 * @param movie The movie data to display.
 * @param modifier The modifier for the card.
 * @param style The style for the card.
 * @param onMovieSelected Callback when the card is clicked.
 */
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
