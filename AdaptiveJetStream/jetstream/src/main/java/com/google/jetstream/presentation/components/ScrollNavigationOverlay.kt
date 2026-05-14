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

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.jetstream.R
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.hasNextItem
import com.google.jetstream.presentation.components.shim.hasPreviousItem
import com.google.jetstream.presentation.theme.JetStreamTokens
import kotlinx.coroutines.launch

/**
 * A layout that overlays navigation buttons (previous/next) over content.
 *
 * @param previous The composable for the previous navigation button.
 * @param next The composable for the next navigation button.
 * @param modifier The modifier to be applied to the layout.
 * @param isSupportVisible Whether the navigation buttons should be visible.
 * @param content The content to be overlaid with navigation buttons.
 */
@Composable
fun ScrollNavigationOverlay(
    previous: @Composable () -> Unit,
    next: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isSupportVisible: Boolean = LocalEngagementMode.current.hasPointingDevice,
    content: @Composable () -> Unit = {},
) {
    Box(
        modifier = modifier,
    ) {
        content()
        if (isSupportVisible) {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                previous()
            }
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                next()
            }
        }
    }
}

/**
 * A button used to navigate to the previous item.
 *
 * @param onClick Called when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param style The style to be applied to the button.
 * @param colors The colors to be used for the button.
 * @param enabled Whether the button is enabled.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun PreviousItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    colors: IconButtonColors = JetStreamTokens.ScrollNavigationButtonColors,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = remember(interactionSource) { MutableStyleState(interactionSource) }

    IconButton(
        onClick = onClick,
        modifier = modifier.styleable(styleState = styleState, style = style),
        interactionSource = interactionSource,
        colors = colors,
        enabled = enabled,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = stringResource(R.string.previous_item),
        )
    }
}

/**
 * A button used to navigate to the next item.
 *
 * @param onClick Called when the button is clicked.
 * @param modifier The modifier to be applied to the button.
 * @param style The style to be applied to the button.
 * @param colors The colors to be used for the button.
 * @param enabled Whether the button is enabled.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun NextItemButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    colors: IconButtonColors = JetStreamTokens.ScrollNavigationButtonColors,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styleState = remember(interactionSource) { MutableStyleState(interactionSource) }

    IconButton(
        onClick = onClick,
        modifier = modifier.styleable(styleState = styleState, style = style),
        interactionSource = interactionSource,
        colors = colors,
        enabled = enabled,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.next_item),
        )
    }
}

/**
 * A button used to navigate to the previous item in a carousel.
 *
 * @param carouselState The state of the carousel to control.
 * @param modifier The modifier to be applied to the button.
 * @param style The style to be applied to the button.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun PreviousItemButton(
    carouselState: CarouselState,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val coroutineScope = rememberCoroutineScope()
    PreviousItemButton(
        onClick = {
            coroutineScope.launch {
                val previousItemIndex =
                    (carouselState.currentItem - 1).coerceAtLeast(0)
                carouselState.scrollToItem(previousItemIndex)
            }
        },
        style = style,
        enabled = carouselState.hasPreviousItem(),
        modifier = modifier,
    )
}

/**
 * A button used to navigate to the next item in a carousel.
 *
 * @param carouselState The state of the carousel to control.
 * @param itemCount The total number of items in the carousel.
 * @param modifier The modifier to be applied to the button.
 * @param style The style to be applied to the button.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun NextItemButton(
    carouselState: CarouselState,
    itemCount: Int,
    modifier: Modifier = Modifier,
    style: Style = Style,
) {
    val coroutineScope = rememberCoroutineScope()
    NextItemButton(
        onClick = {
            coroutineScope.launch {
                val nextItemIndex =
                    (carouselState.currentItem + 1).coerceAtMost(itemCount - 1)
                carouselState.scrollToItem(nextItemIndex)
            }
        },
        enabled = carouselState.hasNextItem(itemCount),
        modifier = modifier,
        style = style,
    )
}
