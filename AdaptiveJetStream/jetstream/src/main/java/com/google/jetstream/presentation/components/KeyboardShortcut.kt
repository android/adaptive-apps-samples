/*
 * Copyright 2025 Google LLC
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

import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isAltPressed
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import com.google.jetstream.data.convert.Into

fun Modifier.handleKeyboardShortcuts(
    shortcuts: List<KeyboardShortcut>,
): Modifier {
    return Modifier
        .onPreviewKeyEvent { keyEvent ->
            keyEvent.type == KeyEventType.KeyUp && shortcuts.any { it.callActionIfMatch(keyEvent) }
        }
        .then(this)
}

data class KeyboardShortcut(
    private val key: Key,
    private val modifierKeys: Set<ModifierKey> = emptySet(),
    private val action: () -> Unit = {},
) {
    private val disAllowedModifierKey = ModifierKey.All - modifierKeys

    private fun match(keyEvent: KeyEvent): Boolean {
        return key == keyEvent.key &&
            !keyEvent.isMetaPressed &&
            disAllowedModifierKey.all { !it.isPressed(keyEvent) } &&
            modifierKeys.all { it.isPressed(keyEvent) }
    }

    fun callActionIfMatch(keyEvent: KeyEvent): Boolean {
        return if (match(keyEvent)) {
            action()
            true
        } else {
            false
        }
    }
}

sealed interface ModifierKey : IsPressed, Into<Int> {

    data object Ctrl : ModifierKey {
        override fun isPressed(keyEvent: KeyEvent): Boolean {
            return keyEvent.isCtrlPressed
        }

        override fun into(): Int {
            return android.view.KeyEvent.META_CTRL_ON
        }
    }

    data object Alt : ModifierKey {
        override fun isPressed(keyEvent: KeyEvent): Boolean {
            return keyEvent.isAltPressed
        }

        override fun into(): Int {
            return android.view.KeyEvent.META_ALT_ON
        }
    }

    data object Shift : ModifierKey {
        override fun isPressed(keyEvent: KeyEvent): Boolean {
            return keyEvent.isShiftPressed
        }

        override fun into(): Int {
            return android.view.KeyEvent.META_SHIFT_ON
        }
    }

    companion object {
        val All = setOf(Ctrl, Alt, Shift)
    }
}

interface IsPressed {
    fun isPressed(keyEvent: KeyEvent): Boolean
}
