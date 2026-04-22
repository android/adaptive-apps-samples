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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.MovieCard
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.SectionTitle
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.JetStreamUiMedia
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.carouselNavigation
import com.google.jetstream.presentation.components.shim.indication.scaleIndication
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
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
            val itemWidth by JetStreamUiMedia.map {
                (windowWidth.value * 0.4).dp
            }
            val carouselState = rememberCarouselState { movieList.size }

            HorizontalMultiBrowseCarousel(
                state = carouselState,
                preferredItemWidth = itemWidth, // 各カードの幅
                itemSpacing = LocalListItemGap.current,
                contentPadding = LocalContentPadding.current.intoPaddingValues(),
                modifier =
                    modifier.carouselNavigation(
                        state = carouselState,
                        coroutineScope = rememberCoroutineScope(),
                    ),
            ) { index ->
                val movie = movieList[index]
                MovieCard(
                    onClick = { onMovieClick(movie) },
                    style = JetStreamTokens.contentColorIndication(),
                    title = {
                        Text(text = movie.name)
                    },
                    image = {
                        Box {
                            PosterImage(
                                movie = movie,
                                style = {
                                    fillWidth()
                                    height((JetStreamTokens.PortraitCardSize.height.value * 1.414).dp)
                                    scaleIndication(focused = 1f)
                                },
                            )
                            OutlinedRankText(rank = index + 1, style = { externalPadding(8.dp) })
                        }
                    },
                )
            }
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
        EngagementMode.Leanback -> ListComponentType.ImmersiveList
        else -> ListComponentType.Carousel
    }
}

@Composable
private fun OutlinedRankText(
    rank: Int,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val text = "#$rank"

    val base = MaterialTheme.typography.displayLarge

    val textStyle =
        base.copy(
            color = MaterialTheme.colorScheme.onSurface,
        )
    val outlineTextStyle =
        base.copy(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
            drawStyle =
                Stroke(
                    miter = 20f,
                    width = 10f,
                    join = StrokeJoin.Round,
                ),
        )

    StylableBox(
        modifier = modifier,
        style = style,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = outlineTextStyle,
            modifier =
                Modifier.styleable {
                    textStyle(outlineTextStyle)
                },
        )
        Text(
            text = text,
            style = textStyle,
            modifier =
                Modifier.styleable {
                    textStyle(textStyle)
                },
        )
    }
}
