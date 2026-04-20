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
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.FeaturedCarousel
import com.google.jetstream.presentation.components.shim.FeaturedCarouselDefaults
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
                moveList = moveList,
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
        EngagementMode.Leanback -> FeaturedMovieCarouselType.Featured
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
        modifier = modifier.styleable(style = style),
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
    moveList: List<Movie>,
    onMovieSelected: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    style: Style,
    state: CarouselState = rememberCarouselState(0) { moveList.count() },
) {
    FeaturedCarousel(
        itemCount = moveList.size,
        state = state,
        nextButton = {
            if (LocalEngagementMode.current.isBackButtonRequired) {
                FeaturedCarouselDefaults.NextButton(
                    itemCount = moveList.size,
                    state = state,
                )
            }
        },
        previousButton = {
            if (LocalEngagementMode.current.isBackButtonRequired) {
                FeaturedCarouselDefaults.PreviousButton(
                    itemCount = moveList.size,
                    state = state,
                )
            }
        },
        modifier = modifier,
        style = style,
    ) { index ->
        val movie = moveList[index]
        CarouselMovieCard(
            movie = movie,
            style = {
                fillSize()
            },
            onMovieSelected = onMovieSelected,
        )
    }
}

@Composable
fun CarouselMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style,
    onMovieSelected: (movie: Movie) -> Unit = {},
) {
    StylableCard(
        onClick = { onMovieSelected(movie) },
        style = style,
        modifier = modifier,
    ) {
        PosterImage(
            movie = movie,
            style = {
                fillSize()
            },
        )
    }
}
