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

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieReviewsAndRatings
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.Border
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.theme.JetStreamBorder
import com.google.jetstream.presentation.theme.JetStreamCardShape
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

@Composable
fun MovieReviews(
    reviewsAndRatings: List<MovieReviewsAndRatings>,
    modifier: Modifier = Modifier,
    contentPadding: Padding = LocalContentPadding.current,
) {
    Column(
        modifier = modifier
            .padding(horizontal = contentPadding.start)
            .padding(bottom = contentPadding.bottom)
    ) {
        Text(text = stringResource(R.string.reviews), style = MaterialTheme.typography.titleMedium)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            reviewsAndRatings.forEach { reviewAndRating ->
                Review(
                    reviewAndRating,
                    modifier
                        .weight(1f)
                        .height(96.dp)
                )
            }
        }
    }
}

@Composable
private fun Review(
    reviewAndRating: MovieReviewsAndRatings,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .focusable(interactionSource = interactionSource)
            .indication(
                interactionSource = interactionSource,
                indication = borderIndication(
                    focused = Border(
                        stroke = JetStreamBorder.stroke.copy(width = ReviewItemOutlineWidth),
                        shape = JetStreamCardShape
                    )
                )
            )
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.3f)
                    .background(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                    ),
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .align(Alignment.Center),
                )
            }
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = reviewAndRating.reviewerName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = StringConstants
                        .Composable
                        .reviewCount(reviewAndRating.reviewCount),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.alpha(0.75f)
                )
            }
        }
        Text(
            text = reviewAndRating.reviewRating,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(end = 16.dp)
        )
    }
}

private val ReviewItemOutlineWidth = 2.dp
