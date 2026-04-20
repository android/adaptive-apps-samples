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

/**
 * Creates a [Style] that applies scale transitions for different interaction states.
 *
 * @param focused The scale factor when the component is focused.
 * @param pressed The scale factor when the component is pressed.
 * @param hovered The scale factor when the component is hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun scaleIndication(
    focused: Float = 1.05f,
    pressed: Float = 0.9f,
    hovered: Float = focused,
): Style {
    return Style {
        scaleIndication(focused, pressed, hovered)
    }
}

/**
 * Applies animated scale transitions for focused, pressed, and hovered states within a [StyleScope].
 *
 * @param focused The scale factor when the component is focused.
 * @param pressed The scale factor when the component is pressed.
 * @param hovered The scale factor when the component is hovered.
 */
@OptIn(ExperimentalFoundationStyleApi::class)
fun StyleScope.scaleIndication(
    focused: Float = 1.05f,
    pressed: Float = 0.9f,
    hovered: Float = focused,
) {
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
