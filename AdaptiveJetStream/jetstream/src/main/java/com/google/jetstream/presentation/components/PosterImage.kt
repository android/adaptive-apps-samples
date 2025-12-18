/*
 * Copyright 2024 Google LLC
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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import kotlin.math.absoluteValue

@Composable
fun PosterImage(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    if (movie.posterUri.isEmpty()) {
        val seed = movie.id.hashCode()
        val color1 = remember(seed) { 
            val h = (seed.absoluteValue % 360).toFloat()
            Color.hsl(h, 0.4f, 0.5f) 
        }
        val color2 = remember(seed) { 
            val h = ((seed.absoluteValue + 120) % 360).toFloat()
            Color.hsl(h, 0.6f, 0.3f) 
        }
        Box(
            modifier = modifier.background(
                Brush.linearGradient(listOf(color1, color2))
            )
        )
    } else {
        AsyncImage(
            modifier = modifier,
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(movie.posterUri)
                .build(),
            contentDescription = StringConstants.Composable.ContentDescription.moviePoster(movie.name),
            contentScale = ContentScale.Crop
        )
    }
}
