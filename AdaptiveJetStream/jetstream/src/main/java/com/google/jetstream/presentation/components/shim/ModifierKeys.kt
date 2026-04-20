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

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed

data class ModifierKeys(
    val ctrl: Boolean = false,
    val shift: Boolean = false,
    val alt: Boolean = false,
    val meta: Boolean = false,
) {
    companion object {
        val None = ModifierKeys()
        val Ctrl = ModifierKeys(ctrl = true)
        val Shift = ModifierKeys(shift = true)
        val Alt = ModifierKeys(alt = true)
        val CtrlShift = ModifierKeys(ctrl = true, shift = true)
        val CtrlAlt = ModifierKeys(ctrl = true, alt = true)
        val CtrlShiftAlt = ModifierKeys(ctrl = true, shift = true, alt = true)
    }
}

fun KeyEvent.modifierKeys(): ModifierKeys {
    return ModifierKeys(
        ctrl = isCtrlPressed,
        shift = isShiftPressed,
        alt = isAltPressed,
        meta = isMetaPressed,
    )
}
