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
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Creates a [Style] that applies a border indication based on the current [MaterialTheme]'s
 * outline color for different interaction states.
 *
 * @param focused The border width when the component is focused.
 * @param pressed The border width when the component is pressed.
 * @param hovered The border width when the component is hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun borderIndication(
    focused: Dp = 0.dp,
    pressed: Dp = focused,
    hovered: Dp = focused,
): Style {
    return borderIndication(
        focused = Border.current(focused),
        pressed = Border.current(pressed),
        hovered = Border.current(hovered),
    )
}

/**
 * Creates a [Style] that applies a custom [Border] indication for different interaction states.
 *
 * @param focused The [Border] to apply when focused.
 * @param pressed The [Border] to apply when pressed.
 * @param hovered The [Border] to apply when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun borderIndication(
    focused: Border = Border.None,
    pressed: Border = focused,
    hovered: Border = focused,
): Style {
    return Style {
        borderIndication(
            focused = focused,
            pressed = pressed,
            hovered = hovered,
        )
    }
}

/**
 * Applies animated border indications for focused, pressed, and hovered states within a [StyleScope].
 *
 * @param focused The [Border] to apply when focused.
 * @param pressed The [Border] to apply when pressed.
 * @param hovered The [Border] to apply when hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.borderIndication(
    focused: Border = Border.None,
    pressed: Border = focused,
    hovered: Border = focused,
) {
    border(focused.copyAsTransparent())

    focused {
        animate {
            border(border = focused)
        }
    }
    pressed {
        animate {
            border(border = pressed)
        }
    }
    hovered {
        animate {
            border(border = hovered)
        }
    }
}

/**
 * Extension to apply a [Border] using [StyleScope.border].
 *
 * @param border The [Border] configuration to apply.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.border(border: Border) {
    border(width = border.width, brush = border.brush)
}

/**
 * Data class representing the visual properties of a border.
 *
 * @property width The thickness of the border.
 * @property brush The [Brush] used to paint the border.
 */
data class Border(
    val width: Dp,
    val brush: Brush,
) {
    fun copyAsTransparent(): Border {
        return copy(brush = SolidColor(Color.Transparent))
    }

    companion object {
        /**
         * Represents no border (zero width and transparent color).
         */
        val None = Border(width = 0.dp, brush = SolidColor(Color.Transparent))

        /**
         * Returns a [Border] using the current [MaterialTheme]'s outline color.
         *
         * @param width The thickness of the border.
         */
        @Composable
        fun current(width: Dp = 1.dp): Border {
            val outlineColor = MaterialTheme.colorScheme.outline
            return remember(width, outlineColor) {
                Border(width, SolidColor(outlineColor))
            }
        }
    }
}
