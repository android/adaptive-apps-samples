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
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import coil.compose.AsyncImage
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.WatchNowButton
import com.google.jetstream.presentation.components.feature.isLeanbackEnabled
import com.google.jetstream.presentation.theme.Padding
import com.google.jetstream.presentation.theme.jetStreamBorderIndication

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedMoviesCarousel(
    movies: List<Movie>,
    padding: Padding,
    goToVideoPlayer: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLeanbackEnabled = isLeanbackEnabled()
    val featuredMoviesCarouselState =
        rememberSaveable(movies, isLeanbackEnabled, saver = FeaturedMoviesCarouselState.Saver) {
            FeaturedMoviesCarouselState(
                itemCount = movies.size,
                initialWatchNowButtonVisibility = !isLeanbackEnabled
            )
        }

    val interactionSource = remember { MutableInteractionSource() }
    if (isLeanbackEnabled) {
        LaunchedEffect(Unit) {
            interactionSource.interactions.collect {
                when (it) {
                    is FocusInteraction.Unfocus -> {
                        featuredMoviesCarouselState.updateWatchNowButtonVisibility(false)
                    }

                    is FocusInteraction.Focus -> {
                        featuredMoviesCarouselState.updateWatchNowButtonVisibility(true)
                    }

                    else -> {}
                }
            }
        }
    }

    Carousel(
        modifier = modifier
            .padding(start = padding.start, end = padding.start, top = padding.top)
            .semantics {
                contentDescription =
                    StringConstants.Composable.ContentDescription.MoviesCarousel
            }
            .clickable(interactionSource, jetStreamBorderIndication) {
                goToVideoPlayer(movies[featuredMoviesCarouselState.activeItemIndex])
            }
            .clip(ShapeDefaults.Medium)
            .dragDetector(featuredMoviesCarouselState),
        itemCount = movies.size,
        carouselState = featuredMoviesCarouselState.carouselState,
        carouselIndicator = {
            CarouselIndicator(
                itemCount = movies.size,
                activeItemIndex = featuredMoviesCarouselState.activeItemIndex
            )
        },
        contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000))
            .togetherWith(fadeOut(tween(durationMillis = 1000))),
        contentTransformEndToStart = fadeIn(tween(durationMillis = 1000))
            .togetherWith(fadeOut(tween(durationMillis = 1000))),
        content = { index ->
            val movie = movies[index]
            // background
            CarouselItemBackground(movie = movie, modifier = Modifier.fillMaxSize())
            // foreground
            CarouselItemForeground(
                movie = movie,
                watchNowButtonVisibility = featuredMoviesCarouselState.watchNowButtonVisibility,
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    goToVideoPlayer(movies[featuredMoviesCarouselState.activeItemIndex])
                }
            )
        }
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun BoxScope.CarouselIndicator(
    itemCount: Int,
    activeItemIndex: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(32.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            .graphicsLayer {
                clip = true
                shape = ShapeDefaults.ExtraSmall
            }
            .align(Alignment.BottomEnd)
    ) {
        CarouselDefaults.IndicatorRow(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            itemCount = itemCount,
            activeItemIndex = activeItemIndex
        )
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
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom
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
