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

package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.foundation.style.then
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.presentation.components.feature.JetStreamUiMedia
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@OptIn(
    ExperimentalGridApi::class,
    ExperimentalFlexBoxApi::class,
    ExperimentalFoundationStyleApi::class,
)
@Composable
fun FeaturedCarousel(
    itemCount: Int,
    state: CarouselState,
    modifier: Modifier = Modifier,
    style: Style = Style,
    carouselIndicator: @Composable () -> Unit = {
        FeaturedCarouselDefaults.IndicatorRow(
            itemCount = itemCount,
            activeItemIndex = state.currentItem,
        )
    },
    previousButton: @Composable () -> Unit = {
        FeaturedCarouselDefaults.PreviousButton(
            itemCount = itemCount,
            state = state,
        )
    },
    nextButton: @Composable () -> Unit = {
        FeaturedCarouselDefaults.NextButton(
            itemCount = itemCount,
            state = state,
        )
    },
    content: @Composable CarouselItemScope.(Int) -> Unit = {},
) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = modifier) {
        HorizontalUncontainedCarousel(
            state = state,
            modifier =
                Modifier
                    .styleable(style = style)
                    .autoScroll(state = state, itemCount = itemCount),
            itemWidth = JetStreamUiMedia.map { windowWidth }.value, // ToDo: replace with MediaQuery
        ) { index ->
            content(index)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier.styleable {
                    externalPadding(16.dp)
                },
        ) {
            previousButton()
            carouselIndicator()
            nextButton()
        }
    }
}

object FeaturedCarouselDefaults {
    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun NextButton(
        itemCount: Int,
        state: CarouselState,
        modifier: Modifier = Modifier,
        style: Style = Style,
    ) {
        val coroutineScope = rememberCoroutineScope()

        IconButton(
            onClick = {
                coroutineScope.launch {
                    state.nextItem(itemCount)
                }
            },
            enabled = state.hasNextItem(itemCount),
            modifier = modifier.styleable(style = style),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = stringResource(R.string.next_item),
            )
        }
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun PreviousButton(
        itemCount: Int,
        state: CarouselState,
        modifier: Modifier = Modifier,
        style: Style = Style,
    ) {
        val coroutineScope = rememberCoroutineScope()
        IconButton(
            onClick = {
                coroutineScope.launch {
                    state.previousItem(itemCount)
                }
            },
            enabled = state.hasPreviousItem(),
            modifier = modifier.styleable(style = style),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = stringResource(R.string.previous_item),
            )
        }
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun IndicatorRow(
        itemCount: Int,
        activeItemIndex: Int,
        modifier: Modifier = Modifier,
        gap: Dp = 8.dp,
        style: Style = Style,
    ) {
        Row(
            modifier = modifier.styleable(style = style),
            horizontalArrangement = Arrangement.spacedBy(gap),
        ) {
            repeat(itemCount) { index ->
                Indicator(
                    isActive = index == activeItemIndex,
                    modifier = Modifier.padding(end = gap),
                )
            }
        }
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun Indicator(
        isActive: Boolean,
        modifier: Modifier = Modifier,
        style: Style = Style,
    ) {
        val color = MaterialTheme.colorScheme.onSurface
        val alpha =
            if (isActive) {
                1f
            } else {
                0.3f
            }
        val defaultStyle =
            Style {
                background(color)
                alpha(alpha)
                shape(CircleShape)
                clip()
                size(8.dp)
            }
        StylableBox(style = defaultStyle then style, modifier = modifier)
    }
}

private suspend fun CarouselState.nextItem(
    itemCount: Int,
) {
    if (itemCount != 0) {
        val nextItemIndex = (currentItem + 1) % itemCount
        animateScrollToItem(nextItemIndex)
    }
}

private suspend fun CarouselState.previousItem(
    itemCount: Int,
) {
    if (itemCount != 0) {
        val previousItemIndex = (itemCount + currentItem - 1) % itemCount
        animateScrollToItem(previousItemIndex)
    }
}

private fun CarouselState.hasPreviousItem(): Boolean {
    return currentItem > 0
}

private fun CarouselState.hasNextItem(itemCount: Int): Boolean {
    return currentItem < itemCount - 1
}

@Composable
private fun Modifier.autoScroll(
    state: CarouselState,
    itemCount: Int,
    autoScrollInterval: Long = 5000L,
    enabled: Boolean = true,
): Modifier {
    LaunchedEffect(state, itemCount, autoScrollInterval, enabled) {
        if (enabled) {
            while (true) {
                yield()
                delay(autoScrollInterval)
                val nextItemIndex = (state.currentItem + 1) % itemCount
                state.animateScrollToItem(nextItemIndex)
            }
        }
    }
    return this
}
