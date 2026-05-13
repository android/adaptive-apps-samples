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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillWidth
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.NextItemButton
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.PreviousItemButton
import com.google.jetstream.presentation.components.ScrollNavigationOverlay
import com.google.jetstream.presentation.components.SectionTitle
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.JetStreamUiMedia
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.carouselNavigation
import com.google.jetstream.presentation.components.shim.styleable.StyleableBox
import com.google.jetstream.presentation.screens.home.immersivelist.ImmersiveList
import com.google.jetstream.presentation.theme.JetStreamTokens
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.LocalListItemGap

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Top10MoviesList(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: (movie: Movie) -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(JetStreamTokens.TitleListGap),
    ) {
        SectionTitle(stringResource(R.string.top_10_movies_title))
        Top10MoviesList(
            movieList = movieList,
            componentType = listComponentType(),
            modifier = modifier,
            onMovieClick = onMovieClick,
            onExpanded = onExpanded,
            onCollapsed = onCollapsed,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Top10MoviesList(
    movieList: List<Movie>,
    componentType: ListComponentType,
    modifier: Modifier = Modifier,
    onMovieClick: (movie: Movie) -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
) {
    when (componentType) {
        ListComponentType.ImmersiveList -> {
            ImmersiveList(
                movieList = movieList,
                modifier = modifier,
                onMovieSelected = onMovieClick,
                onExpanded = onExpanded,
                onCollapsed = onCollapsed,
            )
        }

        else -> {
            val itemWidth by JetStreamUiMedia.query {
                (windowWidth.value * 0.8).dp
            }
            val carouselState = rememberCarouselState { movieList.size }
            val contentPadding = LocalContentPadding.current

            ScrollNavigationOverlay(
                previous = {
                    PreviousItemButton(
                        carouselState = carouselState,
                        style = {
                            externalPaddingStart(contentPadding.start)
                        },
                    )
                },
                next = {
                    NextItemButton(
                        carouselState = carouselState,
                        itemCount = movieList.size,
                        style = {
                            externalPaddingEnd(contentPadding.end)
                        },
                    )
                },
            ) {
                Top10MovieListCarousel(carouselState, itemWidth, modifier, movieList, onMovieClick)
            }
        }
    }
}

@Composable
private fun Top10MovieListCarousel(
    carouselState: CarouselState,
    itemWidth: Dp,
    modifier: Modifier,
    movieList: List<Movie>,
    onMovieClick: (Movie) -> Unit,
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val scrimColor =
        remember(surfaceColor) {
            surfaceColor.copy(alpha = 0.9f)
        }

    val scrimBrush =
        Brush.composite(
            Brush.linearGradient(
                listOf(
                    scrimColor,
                    Color.Transparent,
                ),
                start = Offset(0f, Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY, 0f),
            ),
            Brush.verticalGradient(
                listOf(Color.Transparent, scrimColor),
            ),
            BlendMode.Darken,
        )

    HorizontalMultiBrowseCarousel(
        state = carouselState,
        preferredItemWidth = itemWidth,
        itemSpacing = LocalListItemGap.current,
        contentPadding = LocalContentPadding.current.intoPaddingValues(),
        modifier =
            modifier.carouselNavigation(
                state = carouselState,
                coroutineScope = rememberCoroutineScope(),
            ),
    ) { index ->
        val movie = movieList[index]
        // Set clearance around CarouselMovieCard to ensure border indication is visible.
        StyleableBox(
            style = {
                contentPadding(4.dp)
            },
        ) {
            CarouselMovieCard(
                onClick = { onMovieClick(movie) },
                // style = JetStreamTokens.contentColorIndication(),
                title = {
                    Text(
                        text = "#${index + 1}: ${movie.name}",
                        style = MaterialTheme.typography.titleLarge,
                        softWrap = true,
                    )
                },
                background = {
                    PosterImage(
                        movie = movie,
                        style = {
                            fillWidth()
                            height((JetStreamTokens.PortraitCardSize.height.value * 1.414).dp)
                            foreground(scrimBrush)
                        },
                    )
                },
            )
        }
    }
}

private enum class ListComponentType {
    ImmersiveList,
    Carousel,
}

@Composable
private fun listComponentType(): ListComponentType {
    return when (LocalEngagementMode.current) {
        is EngagementMode.Leanback -> ListComponentType.ImmersiveList
        else -> ListComponentType.Carousel
    }
}
