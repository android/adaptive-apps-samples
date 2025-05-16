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

package com.google.jetstream.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component1
import androidx.compose.ui.focus.FocusRequester.Companion.FocusRequesterFactory.component2
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.presentation.theme.LocalCardWidth
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.LocalHorizontalCardAspectRatio
import com.google.jetstream.presentation.theme.LocalListItemGap
import com.google.jetstream.presentation.theme.LocalVerticalCardAspectRatio
import com.google.jetstream.presentation.theme.Padding

sealed interface ItemDirection {
    @Composable
    fun aspectRatio(): Float

    data object Vertical : ItemDirection {
        @Composable
        override fun aspectRatio() = LocalVerticalCardAspectRatio.current
    }

    data object Horizontal : ItemDirection {
        @Composable
        override fun aspectRatio(): Float = LocalHorizontalCardAspectRatio.current
    }
}

@Composable
fun MoviesRow(
    movieList: MovieList,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    contentPadding: Padding = LocalContentPadding.current,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    onMovieSelected: (movie: Movie) -> Unit = {}
) {
    val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }

    Column(
        modifier = modifier.focusGroup()
    ) {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(
                        contentPadding
                            .copy(top = 16.dp, bottom = 16.dp, end = 0.dp)
                            .intoPaddingValues()
                    )
            )
        }
        AnimatedContent(
            targetState = movieList,
            label = "",
        ) { movieState ->
            LazyRow(
                contentPadding =
                contentPadding.copy(top = 0.dp, bottom = 16.dp).intoPaddingValues(),
                horizontalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
                modifier = Modifier
                    .focusRequester(lazyRow)
                    .focusRestorer(fallback = firstItem)
            ) {
                itemsIndexed(movieState, key = { _, movie -> movie.id }) { index, movie ->
                    val itemModifier = if (index == 0) {
                        Modifier.focusRequester(firstItem)
                    } else {
                        Modifier
                    }
                    MoviesRowItem(
                        modifier = itemModifier.weight(1f),
                        index = index,
                        itemDirection = itemDirection,
                        onMovieSelected = {
                            lazyRow.saveFocusedChild()
                            onMovieSelected(it)
                        },
                        movie = movie,
                        showItemTitle = showItemTitle,
                        showIndexOverImage = showIndexOverImage
                    )
                }
            }
        }
    }
}

@Composable
fun ImmersiveListMoviesRow(
    movieList: MovieList,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    contentPadding: Padding = LocalContentPadding.current,
    title: String? = null,
    titleStyle: TextStyle = MaterialTheme.typography.headlineLarge.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 30.sp
    ),
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    onMovieSelected: (Movie) -> Unit = {},
    onMovieFocused: (Movie) -> Unit = {}
) {
    val (lazyRow, firstItem) = remember { FocusRequester.createRefs() }

    Column(
        modifier = modifier.focusGroup()
    ) {
        if (title != null) {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier
                    .alpha(1f)
                    .padding(
                        Padding(
                            start = contentPadding.start,
                            top = 16.dp,
                            bottom = 16.dp
                        ).intoPaddingValues()
                    )
            )
        }
        AnimatedContent(
            targetState = movieList,
            label = "",
        ) { movieState ->
            LazyRow(
                contentPadding = contentPadding.copy(top = 0.dp, bottom = 0.dp).intoPaddingValues(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier
                    .focusRequester(lazyRow)
                    .focusRestorer(fallback = firstItem)
            ) {
                itemsIndexed(
                    movieState,
                    key = { _, movie ->
                        movie.id
                    }
                ) { index, movie ->
                    val itemModifier = if (index == 0) {
                        Modifier.focusRequester(firstItem)
                    } else {
                        Modifier
                    }
                    MoviesRowItem(
                        modifier = itemModifier.weight(1f),
                        index = index,
                        itemDirection = itemDirection,
                        onMovieSelected = {
                            lazyRow.saveFocusedChild()
                            onMovieSelected(it)
                        },
                        onMovieFocused = onMovieFocused,
                        movie = movie,
                        showItemTitle = showItemTitle,
                        showIndexOverImage = showIndexOverImage
                    )
                }
            }
        }
    }
}

@Composable
private fun MoviesRowItem(
    index: Int,
    movie: Movie,
    onMovieSelected: (Movie) -> Unit,
    showItemTitle: Boolean,
    showIndexOverImage: Boolean,
    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Vertical,
    onMovieFocused: (Movie) -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        if (isFocused) {
            onMovieFocused(movie)
        }
    }

    MovieCard(
        onClick = { onMovieSelected(movie) },
        title = {
            MoviesRowItemText(
                showItemTitle = showItemTitle,
                isItemFocused = true,
                movie = movie
            )
        },
        interactionSource = interactionSource,
        modifier = modifier
    ) {
        MoviesRowItemImage(
            modifier = Modifier
                .width(LocalCardWidth.current)
                .aspectRatio(itemDirection.aspectRatio()),
            showIndexOverImage = showIndexOverImage,
            movie = movie,
            index = index
        )
    }
}

@Composable
private fun MoviesRowItemImage(
    movie: Movie,
    showIndexOverImage: Boolean,
    index: Int,
    modifier: Modifier = Modifier,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        PosterImage(
            movie = movie,
            modifier = modifier
                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    if (showIndexOverImage) {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0.1f
                            )
                        )
                    }
                },
        )
        if (showIndexOverImage) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "#${index.inc()}",
                style = MaterialTheme.typography.displayLarge
                    .copy(
                        shadow = Shadow(
                            offset = Offset(0.5f, 0.5f),
                            blurRadius = 5f
                        ),
                        color = Color.White
                    ),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MoviesRowItemText(
    showItemTitle: Boolean,
    isItemFocused: Boolean,
    movie: Movie,
    modifier: Modifier = Modifier
) {
    if (showItemTitle) {
        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
            label = "",
        )
        Text(
            text = movie.name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .alpha(movieNameAlpha)
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
