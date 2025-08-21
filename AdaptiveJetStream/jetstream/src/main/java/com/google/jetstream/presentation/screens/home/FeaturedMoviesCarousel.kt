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

package com.google.jetstream.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.WatchNowButton
import com.google.jetstream.presentation.components.feature.FormFactor
import com.google.jetstream.presentation.components.feature.rememberFormFactor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeaturedMoviesCarousel(
    movies: List<Movie>,
    goToVideoPlayer: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    val watchNow = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val formFactor = rememberFormFactor()
    val featuredMoviesCarouselState = rememberFeaturedMoviesCarouselState(
        movies = movies,
        initialWatchNowButtonVisibility = formFactor != FormFactor.Tv
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomEnd
    ) {
        HorizontalCenteredHeroCarousel(
            state = featuredMoviesCarouselState.carouselState,
            minSmallItemWidth = 0.dp,
            maxSmallItemWidth = 0.dp,
            modifier = Modifier
                .onPreviewKeyEvent { keyEvent ->
                    when {
                        keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.DirectionRight -> {
                            featuredMoviesCarouselState.nextItem()
                            true
                        }

                        keyEvent.type == KeyEventType.KeyDown &&
                            keyEvent.key == Key.DirectionLeft -> {
                            featuredMoviesCarouselState.previousItem()
                            true
                        }

                        else -> false
                    }
                }
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusChanged {
                    when {
                        it.hasFocus -> {
                            coroutineScope.launch {
                                bringIntoViewRequester.bringIntoView()
                            }
                        }
                    }
                }
                .then(
                    if (formFactor == FormFactor.Tv) {
                        Modifier
                            .onFocusChanged {
                                if (it.hasFocus) {
                                    featuredMoviesCarouselState
                                        .updateWatchNowButtonVisibility(true)
                                } else {
                                    featuredMoviesCarouselState
                                        .updateWatchNowButtonVisibility(false)
                                }
                            }
                            .focusable()
                    } else {
                        Modifier.focusGroup()
                    }
                )

        ) { currentItemIndex ->
            val movie = movies[currentItemIndex]
            // background
            CarouselItemBackground(movie = movie, modifier = Modifier.fillMaxSize())
            // foreground
            CarouselItemForeground(
                movie = movie,
                watchNowButtonVisibility = featuredMoviesCarouselState.watchNowButtonVisibility,
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(watchNow),
                onClick = {
                    goToVideoPlayer(movies[featuredMoviesCarouselState.currentItem])
                }
            )
        }
        CarouselIndication(
            itemCount = movies.size,
            activeItemIndex = featuredMoviesCarouselState.currentItem,
            modifier = Modifier.padding(32.dp)
        )
    }
}

@Composable
private fun CarouselIndication(
    itemCount: Int,
    activeItemIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
    ) {
        IndicatorRow(
            itemCount = itemCount,
            activeItemIndex = activeItemIndex,
            modifier = Modifier
                .padding(16.dp)
                .graphicsLayer {
                    clip = true
                    shape = ShapeDefaults.ExtraSmall
                }
        )
    }
}

@Composable
private fun IndicatorRow(
    itemCount: Int,
    activeItemIndex: Int,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    indicator: @Composable (isActive: Boolean) -> Unit = { isActive ->
        val activeColor = Color.White
        val inactiveColor = activeColor.copy(alpha = 0.3f)

        val backgroundColor = if (isActive) activeColor else inactiveColor
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = backgroundColor, shape = CircleShape)
        )
    }
) {
    Row(
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        modifier = modifier
    ) {
        repeat(itemCount) {
            indicator(it == activeItemIndex)
        }
    }
}

@Composable
private fun CarouselItemForeground(
    movie: Movie,
    modifier: Modifier = Modifier,
    watchNowButtonVisibility: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
        ) {
            Text(
                text = movie.name,
                style = MaterialTheme.typography.displayMedium.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1
            )
            Text(
                text = movie.description,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.65f
                    ),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
            AnimatedVisibility(
                visible = watchNowButtonVisibility,
                content = {
                    WatchNowButton(
                        onClick = onClick
                    )
                }
            )
        }
    }
}

@Composable
private fun CarouselItemBackground(movie: Movie, modifier: Modifier = Modifier) {
    AsyncImage(
        model = movie.posterUri,
        contentDescription = StringConstants
            .Composable
            .ContentDescription
            .moviePoster(movie.name),
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
            },
        contentScale = ContentScale.Crop
    )
}
