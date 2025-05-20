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

package com.google.jetstream.presentation.app.withNavigationSuiteScaffold

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.CinematicScrim
import com.google.jetstream.presentation.components.PosterImage
import com.google.jetstream.presentation.components.WatchNowButton
import com.google.jetstream.presentation.components.requestBringIntoViewOnFocus
import com.google.jetstream.presentation.components.shim.scaleIndication
import com.google.jetstream.presentation.screens.movies.components.ProminentMovieListScope
import com.google.jetstream.presentation.theme.Padding

val ProminentMovieCardWithButton = object : ProminentMovieListScope {
    @Composable
    override fun ProminentMovieCard(
        movie: Movie,
        modifier: Modifier,
        onMovieClick: (movie: Movie) -> Unit
    ) {
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .bringCardIntoView(interactionSource)
                .then(modifier),
        ) {
            PosterImage(
                movie,
                modifier = Modifier.fillMaxSize()
            )
            CinematicScrim(
                colors = listOf(
                    Color.Black.copy(alpha = 0.5f),
                    Color.Black.copy(alpha = 0.9f)
                ),
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MovieTitle(movie)
                WatchNowButton(
                    onClick = { onMovieClick(movie) },
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .height(48.dp)
                        .scaleIndication(interactionSource = interactionSource)
                )
            }
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
}
