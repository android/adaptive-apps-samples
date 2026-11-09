/*
 * Copyright 2026 Google LLC
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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCoerceAtMost
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.theme.LocalListItemGap
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun MovieList(
    movieList: List<Movie>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(LocalListItemGap.current),
    itemContent: @Composable (Int, Movie) -> Unit = { _, _ -> },
) {
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val layoutDirection = LocalLayoutDirection.current

    ScrollNavigationOverlay(
        previous = {
            PreviousItemButton(
                onClick = {
                    coroutineScope.launch {
                        state.animateToPreviousItem()
                    }
                },
                style = {
                    externalPaddingStart(contentPadding.calculateStartPadding(layoutDirection))
                },
                enabled = state.canScrollBackward,
            )
        },
        next = {
            NextItemButton(
                onClick = {
                    coroutineScope.launch {
                        state.animateToNextItem()
                    }
                },
                style = {
                    externalPaddingEnd(contentPadding.calculateEndPadding(layoutDirection))
                },
                enabled = state.canScrollForward,
            )
        },
    ) {
        LazyRow(
            state = state,
            contentPadding = contentPadding,
            horizontalArrangement = horizontalArrangement,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = movieList,
                key = { _, movie -> movie.id },
            ) { index, movie ->
                itemContent(index, movie)
            }
        }
    }
}

private suspend fun LazyListState.animateToPreviousItem() {
    val previousIndex = (firstVisibleItemIndex - 1).coerceAtLeast(0)
    animateScrollToItem(previousIndex)
}

private suspend fun LazyListState.animateToNextItem() {
    val totalItemCount = layoutInfo.totalItemsCount
    if (totalItemCount > 0) {
        val nextIndex = (firstVisibleItemIndex + 1).fastCoerceAtMost(totalItemCount - 1)
        animateScrollToItem(nextIndex)
    }
}
