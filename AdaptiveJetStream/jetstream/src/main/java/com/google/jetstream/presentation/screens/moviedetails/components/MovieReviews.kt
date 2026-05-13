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

import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridFlow
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieReviewsAndRatings
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.shim.indication.scaleIndication
import com.google.jetstream.presentation.components.shim.styleable.StyleableBox
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

@OptIn(
    ExperimentalFoundationStyleApi::class,
    ExperimentalFlexBoxApi::class,
)
@Composable
fun MovieReviews(
    reviewsAndRatings: List<MovieReviewsAndRatings>,
    modifier: Modifier = Modifier,
    contentPadding: Padding = LocalContentPadding.current,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState =
        remember(interactionSource) {
            MutableStyleState(interactionSource = interactionSource)
        }
    Column(
        modifier =
            modifier
                .styleable(
                    styleState = styleState,
                    scaleIndication(),
                    {
                        contentPaddingStart(contentPadding.start)
                    },
                )
                .focusable(interactionSource = interactionSource),
    ) {
        Text(
            text = stringResource(R.string.reviews),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier.styleable {
                    externalPaddingBottom(8.dp)
                },
        )
        FlexBox(
            config = {
                wrap(FlexWrap.Wrap)
                gap(16.dp)
            },
        ) {
            reviewsAndRatings.forEach { reviewAndRating ->
                Review(
                    reviewAndRating = reviewAndRating,
                    style = {
                        externalPadding(0.dp)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)
@Composable
private fun Review(
    reviewAndRating: MovieReviewsAndRatings,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val background = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)

    Grid(
        config = {
            repeat(2) {
                row(GridTrackSize.Auto)
            }
            repeat(3) {
                column(GridTrackSize.Auto)
            }
            columnGap(16.dp)
            flow = GridFlow.Column
        },
        modifier =
            modifier
                .styleable(style = style),
    ) {
        StyleableBox(
            contentAlignment = Alignment.Center,
            style = {
                fillSize()
                background(background)
            },
            modifier = Modifier.gridItem(rowSpan = 2),
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                modifier =
                    Modifier.styleable {
                        externalPadding(8.dp)
                    },
            )
        }
        Text(
            text = reviewAndRating.reviewerName,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text =
                StringConstants
                    .Composable
                    .reviewCount(reviewAndRating.reviewCount),
            style = MaterialTheme.typography.titleMedium,
            modifier =
                Modifier.styleable {
                    alpha(0.75f)
                },
        )
        Text(
            text = reviewAndRating.reviewRating,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.gridItem(rowSpan = 2, alignment = Alignment.Center),
        )
    }
}
