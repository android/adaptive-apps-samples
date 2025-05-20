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
import androidx.compose.animation.Crossfade
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.relocation.BringIntoViewModifierNode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.presentation.components.ImmersiveListMoviesRow
import com.google.jetstream.presentation.components.ItemDirection
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.theme.LocalContentPadding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Top10MoviesList(
    movieList: MovieList,
    modifier: Modifier = Modifier,
    scrimColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    inputMode: InputMode = LocalInputModeManager.current.inputMode,
    onMovieClick: (movie: Movie) -> Unit = {},
) {
    var shouldExpand by remember { mutableStateOf(shouldExpand(false, inputMode)) }
    var shouldShowDescription by remember {
        mutableStateOf(
            shouldShowDescription(
                false,
                inputMode
            )
        )
    }
    var selectedMovie by remember(movieList) { mutableStateOf(movieList.first()) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    ImmersiveList(
        selectedMovie = selectedMovie,
        shouldExpand = shouldExpand, // isListFocused,
        shouldShowDescription = shouldShowDescription,
        scrimColor = scrimColor,
        movieList = movieList,
        title = stringResource(R.string.top_10_movies_title),
        onMovieClick = onMovieClick,
        onMovieFocused = {
            selectedMovie = it
        },
        modifier = modifier
            .onFocusChanged {
                shouldExpand = shouldExpand(it.hasFocus, inputMode)
                shouldShowDescription = shouldShowDescription(it.hasFocus, inputMode)
            }
            .bringIntoViewRequester(bringIntoViewRequester)
            .immersiveListBringIntoView(bringIntoViewRequester)
    )
}

private fun shouldExpand(
    hasFocus: Boolean,
    inputMode: InputMode,
): Boolean {
    return inputMode == InputMode.Touch || hasFocus
}

private fun shouldShowDescription(
    hasFocus: Boolean,
    inputMode: InputMode,
): Boolean {
    return inputMode == InputMode.Keyboard && hasFocus
}

class ImmersiveListBringIntoViewModifierNode(
    var bringIntoViewRequester: BringIntoViewRequester,
) : Modifier.Node(), BringIntoViewModifierNode {
    override suspend fun bringIntoView(
        childCoordinates: LayoutCoordinates,
        boundsProvider: () -> Rect?
    ) {
        bringIntoViewRequester.bringIntoView()
    }
}

private class ImmersiveListBringIntoViewModifierElement(
    val bringIntoViewRequester: BringIntoViewRequester,
) : ModifierNodeElement<ImmersiveListBringIntoViewModifierNode>() {
    override fun InspectorInfo.inspectableProperties() {
        name = "ImmersiveListBringIntoViewModifier"
    }

    override fun create(): ImmersiveListBringIntoViewModifierNode {
        return ImmersiveListBringIntoViewModifierNode(
            bringIntoViewRequester = bringIntoViewRequester,
        )
    }

    override fun equals(other: Any?): Boolean {
        return other is ImmersiveListBringIntoViewModifierElement &&
            other.bringIntoViewRequester == bringIntoViewRequester
    }

    override fun hashCode(): Int {
        return bringIntoViewRequester.hashCode()
    }

    override fun update(node: ImmersiveListBringIntoViewModifierNode) {
        node.bringIntoViewRequester = bringIntoViewRequester
    }
}

private fun Modifier.immersiveListBringIntoView(
    bringIntoViewRequester: BringIntoViewRequester,
): Modifier =
    this then ImmersiveListBringIntoViewModifierElement(
        bringIntoViewRequester = bringIntoViewRequester,
    )

@Composable
private fun ImmersiveList(
    selectedMovie: Movie,
    movieList: MovieList,
    modifier: Modifier = Modifier,
    title: String? = null,
    shouldExpand: Boolean = false,
    shouldShowDescription: Boolean = true,
    scrimColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    onMovieFocused: (Movie) -> Unit = {},
    onMovieClick: (Movie) -> Unit = {},
) {
    val moviesRow = remember { FocusRequester() }
    val paddingBottom = if (shouldExpand) {
        LocalContentPadding.current.bottom
    } else {
        0.dp
    }

    ImmersiveListFrame(
        poster = {
            AnimatedVisibility(
                visible = shouldExpand,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Background(
                    movie = selectedMovie,
                    modifier = Modifier
                        .fillMaxSize()
                        .gradientOverlay(scrimColor)
                )
            }
        },
        description = {
            AnimatedVisibility(
                visible = shouldShowDescription,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                MovieDescription(
                    movie = selectedMovie,
                    modifier = Modifier.padding(
                        start = LocalContentPadding.current.start,
                        bottom = 40.dp
                    )
                )
            }
        },
        modifier = modifier
            .focusProperties {
                onEnter = {
                    moviesRow.tryRequestFocus()
                }
            }
            .focusGroup()
    ) {
        ImmersiveListMoviesRow(
            movieList = movieList,
            itemDirection = ItemDirection.Horizontal,
            title = if (shouldShowDescription) {
                null
            } else {
                title
            },
            showItemTitle = !shouldExpand,
            showIndexOverImage = true,
            onMovieSelected = onMovieClick,
            onMovieFocused = onMovieFocused,
            modifier = Modifier
                .focusRequester(moviesRow)
                .padding(bottom = paddingBottom)
        )
    }
}

@Composable
private fun ImmersiveListFrame(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.BottomStart,
    poster: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    list: @Composable () -> Unit = {},
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier,
    ) {
        poster()
        Column {
            description()
            list()
        }
    }
}

@Composable
private fun Background(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = movie,
        label = "posterUriCrossfade",
    ) {
        PosterImage(movie = it, modifier = modifier)
    }
}

@Composable
private fun MovieDescription(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        movie
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = it.name, style = MaterialTheme.typography.displaySmall)
            Text(
                modifier = Modifier.fillMaxWidth(0.5f),
                text = it.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                fontWeight = FontWeight.Light
            )
        }
    }
}

private fun Modifier.gradientOverlay(gradientColor: Color): Modifier =
    drawWithCache {
        val horizontalGradient = Brush.horizontalGradient(
            colors = listOf(
                gradientColor,
                Color.Transparent
            ),
            startX = size.width.times(0.2f),
            endX = size.width.times(0.7f)
        )
        val verticalGradient = Brush.verticalGradient(
            colors = listOf(
                Color.Transparent,
                gradientColor
            ),
            endY = size.width.times(0.3f)
        )
        val linearGradient = Brush.linearGradient(
            colors = listOf(
                gradientColor,
                Color.Transparent
            ),
            start = Offset(
                size.width.times(0.2f),
                size.height.times(0.5f)
            ),
            end = Offset(
                size.width.times(0.9f),
                0f
            )
        )

        onDrawWithContent {
            drawContent()
            drawRect(horizontalGradient)
            drawRect(verticalGradient)
            drawRect(linearGradient)
        }
    }
