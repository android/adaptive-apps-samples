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

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.pressed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun borderIndicationStyle(
    focused: Dp = 0.dp,
    pressed: Dp = focused,
    hovered: Dp = focused,
): Style {
    val color = MaterialTheme.colorScheme.outline

    return Style {
        if (focused.value > 0) {
            focused {
                animate {
                    border(focused, color)
                }
            }
        }
        if (pressed.value > 0) {
            pressed {
                animate {
                    border(pressed, color)
                }
            }
        }
        if (hovered.value > 0) {
            hovered {
                animate {
                    border(hovered, color)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
fun scaleIndicationStyle(
    focused: Float = 1.05f,
    pressed: Float = 0.9f,
    hovered: Float = focused,
): Style {
    return Style {
        focused {
            animate {
                scale(focused)
            }
        }
        pressed {
            animate {
                scale(pressed)
            }
        }
        hovered {
            animate {
                scale(hovered)
            }
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun contentColorIndicationStyle(
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    focusedContentColor: Color = contentColor,
    pressedContentColor: Color = focusedContentColor,
): Style {
    return Style {
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
    }
}
