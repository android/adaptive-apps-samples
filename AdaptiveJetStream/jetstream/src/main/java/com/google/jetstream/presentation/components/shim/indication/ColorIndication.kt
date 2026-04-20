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

package com.google.jetstream.presentation.components.shim.indication

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.StyleScope
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.pressed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Creates a [Style] that applies both container and content color transitions for different interaction states.
 *
 * @param containerColor The default container color.
 * @param contentColor The default content color.
 * @param focusedContainerColor The container color when focused.
 * @param focusedContentColor The content color when focused.
 * @param pressedContainerColor The container color when pressed.
 * @param pressedContentColor The content color when pressed.
 * @param hoveredContainerColor The container color when hovered.
 * @param hoveredContentColor The content color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun colorIndication(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    focusedContainerColor: Color = containerColor,
    focusedContentColor: Color = contentColor,
    pressedContainerColor: Color = focusedContainerColor,
    pressedContentColor: Color = focusedContentColor,
    hoveredContainerColor: Color = focusedContainerColor,
    hoveredContentColor: Color = focusedContentColor,
): Style {
    return Style {
        colorIndication(
            containerColor = containerColor,
            contentColor = contentColor,
            focusedContainerColor = focusedContainerColor,
            focusedContentColor = focusedContentColor,
            pressedContainerColor = pressedContainerColor,
            pressedContentColor = pressedContentColor,
            hoveredContainerColor = hoveredContainerColor,
            hoveredContentColor = hoveredContentColor,
        )
    }
}

/**
 * Applies animated container and content color transitions for focused, pressed, and hovered states within a [StyleScope].
 *
 * @param containerColor The default container color.
 * @param contentColor The default content color.
 * @param focusedContainerColor The container color when focused.
 * @param focusedContentColor The content color when focused.
 * @param pressedContainerColor The container color when pressed.
 * @param pressedContentColor The content color when pressed.
 * @param hoveredContainerColor The container color when hovered.
 * @param hoveredContentColor The content color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.colorIndication(
    containerColor: Color,
    contentColor: Color,
    focusedContainerColor: Color = containerColor,
    focusedContentColor: Color = contentColor,
    pressedContainerColor: Color = focusedContainerColor,
    pressedContentColor: Color = focusedContentColor,
    hoveredContainerColor: Color = focusedContainerColor,
    hoveredContentColor: Color = focusedContentColor,
) {
    containerColorIndication(
        containerColor = containerColor,
        focusedContainerColor = focusedContainerColor,
        pressedContainerColor = pressedContainerColor,
        hoveredContainerColor = hoveredContainerColor,
    )
    contentColorIndication(
        contentColor = contentColor,
        focusedContentColor = focusedContentColor,
        pressedContentColor = pressedContentColor,
        hoveredContentColor = hoveredContentColor,
    )
}

/**
 * Creates a [Style] that applies content color transitions for different interaction states.
 *
 * @param contentColor The default content color.
 * @param focusedContentColor The content color when focused.
 * @param pressedContentColor The content color when pressed.
 * @param hoveredContentColor The content color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun contentColorIndication(
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    focusedContentColor: Color = contentColor,
    pressedContentColor: Color = focusedContentColor,
    hoveredContentColor: Color = focusedContentColor,
): Style {
    return Style {
        contentColorIndication(
            contentColor = contentColor,
            focusedContentColor = focusedContentColor,
            pressedContentColor = pressedContentColor,
            hoveredContentColor = hoveredContentColor,
        )
    }
}

/**
 * Applies animated content color transitions for focused, pressed, and hovered states within a [StyleScope].
 *
 * @param contentColor The default content color.
 * @param focusedContentColor The content color when focused.
 * @param pressedContentColor The content color when pressed.
 * @param hoveredContentColor The content color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.contentColorIndication(
    contentColor: Color,
    focusedContentColor: Color = contentColor,
    pressedContentColor: Color = focusedContentColor,
    hoveredContentColor: Color = focusedContentColor,
) {
    contentColor(contentColor)
    focused {
        animate {
            contentColor(focusedContentColor)
        }
    }
    pressed {
        animate {
            contentColor(pressedContentColor)
        }
    }
    hovered {
        animate {
            contentColor(hoveredContentColor)
        }
    }
}

/**
 * Creates a [Style] that applies container color transitions for different interaction states.
 *
 * @param containerColor The default container color.
 * @param focusedContainerColor The container color when focused.
 * @param pressedContainerColor The container color when pressed.
 * @param hoveredContainerColor The container color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun containerColorIndication(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    focusedContainerColor: Color = containerColor,
    pressedContainerColor: Color = focusedContainerColor,
    hoveredContainerColor: Color = focusedContainerColor,
): Style {
    return Style {
        containerColorIndication(
            containerColor = containerColor,
            focusedContainerColor = focusedContainerColor,
            pressedContainerColor = pressedContainerColor,
            hoveredContainerColor = hoveredContainerColor,
        )
    }
}

/**
 * Applies animated container color transitions for focused, pressed, and hovered states within a [StyleScope].
 *
 * @param containerColor The default container color.
 * @param focusedContainerColor The container color when focused.
 * @param pressedContainerColor The container color when pressed.
 * @param hoveredContainerColor The container color when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.containerColorIndication(
    containerColor: Color,
    focusedContainerColor: Color = containerColor,
    pressedContainerColor: Color = focusedContainerColor,
    hoveredContainerColor: Color = focusedContainerColor,
) {
    background(containerColor)
    focused {
        animate {
            background(focusedContainerColor)
        }
    }
    pressed {
        animate {
            background(pressedContainerColor)
        }
    }
    hovered {
        animate {
            background(hoveredContainerColor)
        }
    }
}
