/*
 * Copyright 2025 Google LLC
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

@file:OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)

package com.google.jetstream.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.indication.scaleIndication
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
import com.google.jetstream.presentation.components.shim.stylable.StylableGrid
import com.google.jetstream.presentation.theme.Padding
import com.google.jetstream.presentation.theme.Typography
import com.google.jetstream.presentation.theme.styles.LocalProminentCardStyle
import com.google.jetstream.presentation.theme.styles.isFocusOptimized

@Composable
fun ProminentMovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style,
    onMovieClick: (movie: Movie) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    CompositionLocalProvider(LocalRippleConfiguration provides null) {
        ProminentMovieCard(
            movie = movie,
            isFocusOptimized = LocalEngagementMode.current.isFocusOptimized(),
            modifier = modifier,
            style = style,
            interactionSource = interactionSource,
            onClick = { onMovieClick(movie) },
        )
    }
}

@Composable
private fun ProminentMovieCard(
    movie: Movie,
    isFocusOptimized: Boolean,
    modifier: Modifier = Modifier,
    style: Style = Style,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
) {
    StylableBox(
        style = LocalProminentCardStyle.current.card then style,
        interactionSource = interactionSource,
        modifier =
            modifier
                .then(
                    if (isFocusOptimized) {
                        Modifier
                            .clickable(onClick = onClick, interactionSource = interactionSource)
                            .semantics(
                                properties = {
                                    role = Role.Button
                                },
                            )
                    } else {
                        Modifier
                    },
                )
                .bringCardIntoView(interactionSource = interactionSource),
    ) {
        PosterImage(
            movie = movie,
            style = {
                fillSize()
            },
        )
        CinematicScrim(style = LocalProminentCardStyle.current.scrim)
        CardContainer(
            movie = movie,
            style = LocalProminentCardStyle.current.container,
        ) {
            if (!isFocusOptimized) {
                WatchNowButton(
                    onClick = onClick,
                    interactionSource = interactionSource,
                    style = {
                        height(48.dp)
                        scaleIndication()
                    },
                )
            }
        }
    }
}

@Composable
private fun CardContainer(
    movie: Movie,
    modifier: Modifier = Modifier,
    style: Style = Style,
    postfix: @Composable () -> Unit = {},
) {
    StylableGrid(
        config = {
            column(1.fr)

            row(1.fr)
            row(GridTrackSize.Auto)
            row(GridTrackSize.Auto)

            gap(8.dp)
        },
        modifier = modifier,
        style = style,
    ) {
        Text(
            text = movie.description,
            // Workaround: Style is not applied in lazy lists.
            style = Typography.labelSmall,
            modifier =
                Modifier
                    .gridItem(row = -2)
                    .styleable {
                        textStyle(Typography.labelSmall)
                    },
        )
        Text(
            text = movie.name,
            // Workaround: Style is not applied in lazy lists.
            style = Typography.headlineSmall,
            modifier =
                Modifier
                    .gridItem(row = -1)
                    .styleable {
                        textStyle(Typography.headlineSmall)
                    },
        )
        postfix()
    }
}

@Composable
private fun Modifier.bringCardIntoView(
    interactionSource: MutableInteractionSource,
    padding: Padding = Padding(horizontal = 32.dp),
): Modifier {
    return requestBringIntoViewOnFocus(
        interactionSource = interactionSource,
        padding = padding,
    )
}
