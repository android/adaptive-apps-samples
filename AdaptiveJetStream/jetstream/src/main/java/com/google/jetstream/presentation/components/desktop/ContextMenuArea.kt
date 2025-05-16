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

package com.google.jetstream.presentation.components.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerType
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.isTertiaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun ContextMenuArea(
    items: List<ContextMenuItem>,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    var rightClickOffset by remember { mutableStateOf<Offset?>(null) }

    val isMenuVisible = items.isNotEmpty() && rightClickOffset != null

    val offset = with(LocalDensity.current) {
        if (rightClickOffset == null) {
            DpOffset(0.dp, 0.dp)
        } else {
            DpOffset(rightClickOffset!!.x.toDp(), rightClickOffset!!.y.toDp())
        }
    }

    Box(
        modifier = Modifier
            .rightClickDetector {
                rightClickOffset = it
            }
            .then(modifier)
    ) {
        content()
        AnimatedVisibility(isMenuVisible) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = { rightClickOffset = null },
                offset = offset
            ) {
                items.forEach {
                    DropdownMenuItem(
                        text = { Text(it.label) },
                        onClick = {
                            it.action()
                            rightClickOffset = null
                        }
                    )
                }
            }
        }
    }
}

private fun Modifier.rightClickDetector(
    onRightClick: (Offset) -> Unit
): Modifier =
    pointerInput(onRightClick) {
        awaitEachGesture {
            val event = awaitPointerEvent()
            if (
                event.type == PointerEventType.Press &&
                !event.buttons.isPrimaryPressed &&
                event.buttons.isSecondaryPressed &&
                !event.buttons.isTertiaryPressed
            ) {
                val change = event.changes.find {
                    it.type == PointerType.Mouse && it.pressed
                }
                if (change != null) {
                    onRightClick(change.position)
                }
                event.changes.forEach {
                    it.consume()
                }
            }
        }
    }

data class ContextMenuItem(
    val label: String,
    val action: () -> Unit = {},
)
