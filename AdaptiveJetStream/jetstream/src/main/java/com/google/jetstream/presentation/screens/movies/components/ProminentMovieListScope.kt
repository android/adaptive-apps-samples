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

package com.google.jetstream.presentation.screens.movies.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.shim.CompactCard
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.theme.JetStreamBorder

interface ProminentMovieListScope {
    @Composable
    fun ProminentMovieCard(
        movie: Movie,
        modifier: Modifier = Modifier,
        onMovieClick: (movie: Movie) -> Unit = {}
    )

    object Default : ProminentMovieListScope {

        @Composable
        override fun ProminentMovieCard(
            movie: Movie,
            modifier: Modifier,
            onMovieClick: (movie: Movie) -> Unit
        ) {
            val interactionSource = remember { MutableInteractionSource() }
            val isFocused by interactionSource.collectIsFocusedAsState()

            val contentAlpha by animateFloatAsState(
                targetValue = if (isFocused) 1f else 0.5f,
                label = "",
            )

            CompositionLocalProvider(LocalRippleConfiguration provides null) {
                CompactCard(
                    modifier = modifier
                        .indication(
                            interactionSource = interactionSource,
                            indication = borderIndication(focused = JetStreamBorder)
                        )
                        .graphicsLayer { alpha = contentAlpha },
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    interactionSource = interactionSource,
                    onClick = { onMovieClick(movie) },
                    image = {
                        PosterImage(
                            movie = movie,
                            modifier = Modifier.fillMaxSize()
                        )
                    },
                    title = {
                        MovieTitle(
                            movie = movie,
                            modifier = Modifier.padding(24.dp)
                        )
                    }
                )
            }
        }

        @Composable
        fun MovieTitle(
            movie: Movie,
            modifier: Modifier = Modifier,
            verticalArrangement: Arrangement.Vertical = Arrangement.Top
        ) {
            Column(
                modifier = modifier,
                verticalArrangement = verticalArrangement
            ) {
                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.graphicsLayer { alpha = 0.6f }
                )
                Text(
                    text = movie.name,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        }
    }
}

val LocalProminentMovieListScope: ProvidableCompositionLocal<ProminentMovieListScope> =
    staticCompositionLocalOf { ProminentMovieListScope.Default }
