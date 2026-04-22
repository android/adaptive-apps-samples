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

package com.google.jetstream.presentation.screens.moviedetails.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.stylable.StylableGrid
import com.google.jetstream.presentation.screens.moviedetails.Descriptor
import com.google.jetstream.presentation.theme.JetStreamTokens
import com.google.jetstream.presentation.theme.LocalContentPadding
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalFoundationStyleApi::class,
    ExperimentalGridApi::class,
)
@Composable
internal fun MovieDetails(
    movieDetails: MovieDetails,
    goToMoviePlayer: (MovieDetails) -> Unit,
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val contentPadding = LocalContentPadding.current

    Box(
        modifier =
            Modifier
                .wrapContentSize()
                .bringIntoViewRequester(bringIntoViewRequester),
        contentAlignment = Alignment.Center,
    ) {
        MovieImageWithGradients(
            movieDetails = movieDetails,
            modifier = Modifier.matchParentSize(),
        )
        StylableGrid(
            config = {
                column(0.33f)
                column(0.33f)
                column(0.34f)

                row(GridTrackSize.Auto)
                row(GridTrackSize.Auto)
                row(GridTrackSize.Auto)
                row(GridTrackSize.Auto)
                row(GridTrackSize.Auto)
                row(GridTrackSize.Auto)

                gap(8.dp)
            },
            style = {
                externalPaddingTop(108.dp)
                contentPaddingStart(contentPadding.start)
                contentPaddingEnd(contentPadding.end)
            },
        ) {
            MovieLargeTitle(
                movieTitle = movieDetails.name,
                modifier = Modifier.gridItem(columnSpan = 3),
            )
            MovieDescription(
                description = movieDetails.description,
                modifier = Modifier.gridItem(columnSpan = 3),
                style = {
                    alpha(0.75f)
                    externalPaddingBottom(16.dp)
                },
            )
            Descriptor(
                text = movieDetails.pgRating,
            )
            Descriptor(
                text = movieDetails.releaseDate,
            )
            Descriptor(
                text = movieDetails.duration,
            )
            Descriptor(
                text = movieDetails.categories.joinToString(", "),
                modifier = Modifier.gridItem(columnSpan = 3),
                style = {
                    externalPaddingBottom(8.dp)
                },
            )
            TitleValueText(
                title = stringResource(R.string.director),
                value = movieDetails.director,
            )
            TitleValueText(
                title = stringResource(R.string.screenplay),
                value = movieDetails.screenplay,
            )
            TitleValueText(
                title = stringResource(R.string.music),
                value = movieDetails.music,
            )
            WatchTrailerButton(
                modifier =
                    Modifier
                        .gridItem(columnSpan = 3)
                        .onFocusChanged {
                            if (it.isFocused) {
                                coroutineScope.launch { bringIntoViewRequester.bringIntoView() }
                            }
                        },
                style = {
                    externalPaddingTop(16.dp)
                },
                goToMoviePlayer = { goToMoviePlayer(movieDetails) },
            )
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun WatchTrailerButton(
    modifier: Modifier = Modifier,
    style: Style = Style,
    goToMoviePlayer: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    val styleState =
        remember(interactionSource) {
            MutableStyleState(interactionSource = interactionSource)
        }
    val textStyle = MaterialTheme.typography.titleSmall

    val defaultStyle =
        Style {
            textStyle(textStyle)
        }

    Button(
        onClick = goToMoviePlayer,
        modifier =
            modifier.styleable(
                styleState = styleState,
                JetStreamTokens.buttonStyle(),
                defaultStyle,
                style,
            ),
        // Workaround
        shape = JetStreamTokens.ButtonShape,
        interactionSource = interactionSource,
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
            modifier =
                Modifier.styleable {
                    externalPaddingEnd(8.dp)
                },
        )
        Text(
            text = stringResource(R.string.watch_trailer),
            style = textStyle,
            softWrap = false,
        )
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun MovieDescription(
    description: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    Text(
        text = description,
        style =
            MaterialTheme.typography.titleSmall.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
            ),
        modifier = modifier.styleable(style = style),
        maxLines = 2,
    )
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun MovieLargeTitle(
    movieTitle: String,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    Text(
        text = movieTitle,
        modifier = modifier.styleable(style = style),
        style =
            MaterialTheme.typography.displayMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
        maxLines = 1,
    )
}

@Composable
private fun MovieImageWithGradients(
    movieDetails: MovieDetails,
    modifier: Modifier = Modifier,
    gradientColor: Color = MaterialTheme.colorScheme.surface,
) {
    val drawGradients: Modifier.() -> Modifier = {
        this.drawWithContent {
            drawContent()
            drawRect(
                Brush.verticalGradient(
                    colors = listOf(Color.Transparent, gradientColor),
                    startY = 600f,
                ),
            )
            drawRect(
                Brush.horizontalGradient(
                    colors = listOf(gradientColor, Color.Transparent),
                    endX = 1000f,
                    startX = 300f,
                ),
            )
            drawRect(
                Brush.linearGradient(
                    colors = listOf(gradientColor, Color.Transparent),
                    start = Offset(x = 500f, y = 500f),
                    end = Offset(x = 1000f, y = 0f),
                ),
            )
        }
    }

    if (movieDetails.posterUri.isEmpty()) {
        val seed = movieDetails.id.hashCode()
        val color1 =
            remember(seed) {
                val h = (seed.absoluteValue % 360).toFloat()
                Color.hsl(h, 0.4f, 0.5f)
            }
        val color2 =
            remember(seed) {
                val h = ((seed.absoluteValue + 120) % 360).toFloat()
                Color.hsl(h, 0.6f, 0.3f)
            }
        Box(
            modifier =
                modifier
                    .background(Brush.linearGradient(listOf(color1, color2)))
                    .then(Modifier.drawGradients()),
        )
    } else {
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current).data(movieDetails.posterUri)
                    .crossfade(true).build(),
            contentDescription =
                StringConstants
                    .Composable
                    .ContentDescription
                    .moviePoster(movieDetails.name),
            contentScale = ContentScale.Crop,
            modifier = modifier.then(Modifier.drawGradients()),
        )
    }
}
