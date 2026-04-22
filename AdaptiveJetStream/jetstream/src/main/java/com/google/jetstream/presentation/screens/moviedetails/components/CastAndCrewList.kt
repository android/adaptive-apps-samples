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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.jetstream.R
import com.google.jetstream.data.entities.MovieCast
import com.google.jetstream.presentation.components.shim.ClassicCard
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.LocalListItemGap
import com.google.jetstream.presentation.theme.ourColors

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationStyleApi::class)
@Composable
internal fun CastAndCrewList(
    castAndCrew: List<MovieCast>,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val contentPadding = LocalContentPadding.current

    Column(
        modifier = modifier.styleable(style = style),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.cast_and_crew),
            style =
                MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                ),
            modifier =
                Modifier.styleable {
                    contentPaddingStart(contentPadding.start)
                },
        )
        // ToDo: specify the pivot offset
        LazyRow(
            modifier = Modifier.focusRestorer(),
            contentPadding = PaddingValues(start = contentPadding.start),
            horizontalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
        ) {
            items(castAndCrew, key = { it.id }) {
                CastAndCrewItem(it, modifier = Modifier.width(144.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun CastAndCrewItem(
    castMember: MovieCast,
    modifier: Modifier = Modifier,
) {
    val background = MaterialTheme.colorScheme.surfaceVariant
    ClassicCard(
        modifier = modifier.aspectRatio(1 / 1.8f),
        style = {
            background(background)
        },
        title = {
            Text(
                modifier =
                    Modifier.styleable {
                        contentPadding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 4.dp)
                    },
                text = castMember.realName,
                maxLines = 1,
                style = MaterialTheme.typography.labelMedium,
                overflow = TextOverflow.Ellipsis,
            )
        },
        subtitle = {
            Text(
                text = castMember.characterName,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                modifier =
                    Modifier.styleable {
                        alpha(0.75f)
                        contentPadding(horizontal = 12.dp, vertical = 0.dp)
                    },
                overflow = TextOverflow.Ellipsis,
            )
        },
        image = {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.725f)
                        .background(ourColors.random()),
            )
        },
        onClick = {},
    )
}
