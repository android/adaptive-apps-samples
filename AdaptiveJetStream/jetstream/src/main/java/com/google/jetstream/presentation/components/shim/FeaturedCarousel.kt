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

import androidx.compose.foundation.focusGroup
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.presentation.components.feature.JetStreamUiMedia
import com.google.jetstream.presentation.components.onPointerHovered
import com.google.jetstream.presentation.components.shim.stylable.StylableBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
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
    isAutoScrollEnabled: Boolean = true,
    autoScrollInterval: Long = 5000L,
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
    var autoScroll by remember { mutableStateOf(isAutoScrollEnabled) }
    val carouselItems =
        remember(itemCount) {
            List(itemCount) { FocusRequester() }
        }

    LaunchedEffect(state.currentItem) {
        state.onScrollFinished {
            if (!autoScroll) {
                carouselItems[state.currentItem].requestFocus()
            }
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier =
            modifier
                .focusProperties {
                    onEnter = {
                        autoScroll = false
                    }
                    onExit = {
                        autoScroll = true
                    }
                }
                .onPointerHovered(
                    onEnter = { autoScroll = false },
                    onExit = { autoScroll = true },
                )
                .carouselNavigation(state, itemCount, rememberCoroutineScope()),
    ) {
        AutoScrollHorizontalUncontainedCarousel(
            itemCount = itemCount,
            state = state,
            style = style,
            itemWidth = JetStreamUiMedia.map { windowWidth }.value,
            isAutoScrollEnabled = autoScroll,
            autoScrollInterval = autoScrollInterval,
        ) { index ->
            Box(
                modifier =
                    Modifier
                        .focusRequester(focusRequester = carouselItems[index])
                        .focusGroup(),
            ) {
                content(index)
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier.styleable {
                    externalPadding(horizontal = 48.dp, vertical = 16.dp)
                },
        ) {
            previousButton()
            carouselIndicator()
            nextButton()
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun AutoScrollHorizontalUncontainedCarousel(
    itemCount: Int,
    state: CarouselState,
    itemWidth: Dp,
    modifier: Modifier = Modifier,
    style: Style = Style,
    isAutoScrollEnabled: Boolean,
    autoScrollInterval: Long = 5000L,
    content: @Composable CarouselItemScope.(Int) -> Unit = {},
) {
    HorizontalUncontainedCarousel(
        state = state,
        modifier =
            modifier
                .styleable(style = style)
                .autoScroll(
                    state = state,
                    itemCount = itemCount,
                    autoScrollInterval = autoScrollInterval,
                    enabled = isAutoScrollEnabled,
                ),
        itemWidth = itemWidth,
        content = content,
    )
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
                    state.previousItem()
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
    onScrollFinished {
        if (hasNextItem(itemCount)) {
            currentCoroutineContext().ensureActive()
            val nextItemIndex = currentItem + 1
            animateScrollToItem(nextItemIndex)
        }
    }
}

private suspend fun CarouselState.previousItem() {
    onScrollFinished {
        if (hasPreviousItem()) {
            currentCoroutineContext().ensureActive()
            val previousItemIndex = currentItem - 1
            animateScrollToItem(previousItemIndex)
        }
    }
}

internal suspend fun CarouselState.onScrollFinished(block: suspend () -> Unit) {
    snapshotFlow {
        isScrollInProgress
    }.filter { !it }.first()
    block()
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
                delay(autoScrollInterval)
                yield()
                state.nextItem(itemCount)
            }
        }
    }
    return this
}

internal fun Modifier.carouselNavigation(
    state: CarouselState,
    itemCount: Int,
    coroutineScope: CoroutineScope,
): Modifier {
    return onKeyEvent { keyEvent ->
        when (keyEvent.key) {
            Key.DirectionLeft
                if keyEvent.type == KeyEventType.KeyUp &&
                        keyEvent.modifierKeys() == ModifierKeys.None -> {
                coroutineScope.launch {
                    state.previousItem()
                }
                true
            }

            /*
            Key.DirectionRight
            if keyEvent.type == KeyEventType.KeyUp &&
                keyEvent.modifierKeys() == ModifierKeys.None -> {
                coroutineScope.launch {
                    state.nextItem(itemCount)
                }
                true
            }

             */
            else -> {
                false
            }
        }
    }
}
