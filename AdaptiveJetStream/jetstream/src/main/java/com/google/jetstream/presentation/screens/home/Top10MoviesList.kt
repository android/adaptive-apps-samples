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
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

            // Material3 の Carousel 実装例
            HorizontalMultiBrowseCarousel(
                state = rememberCarouselState { movieList.size },
                preferredItemWidth = itemWidth, // 各カードの幅
                itemSpacing = LocalListItemGap.current,
                contentPadding = LocalContentPadding.current.intoPaddingValues(),
                modifier = modifier,
            ) { index ->
                val movie = movieList[index]
                // 各映画のカードを表示
                MovieCard(
                    onClick = { onMovieClick(movie) },
                    style = JetStreamTokens.contentColorIndication(),
                    title = {
                        Text(text = movie.name)
                    },
                    image = {
                        PosterImage(
                            movie = movie,
                            style = {
                                fillWidth()
                                height((JetStreamTokens.PortraitCardSize.height.value * 1.414).dp)
                            },
                        )
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
