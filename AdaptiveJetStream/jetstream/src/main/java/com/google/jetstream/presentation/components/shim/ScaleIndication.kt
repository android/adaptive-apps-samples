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

package com.google.jetstream.presentation.components.shim

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.launch

fun Modifier.scaleIndication(
    interactionSource: InteractionSource,
    focus: Float = 1.05f,
    press: Float = 0.9f,
    hover: Float = focus,
    drag: Float = press,
): Modifier {
    return indication(interactionSource, scaleIndication(focus, press, hover, drag))
}

fun scaleIndication(
    focus: Float = 1.05f,
    press: Float = 0.9f,
    hover: Float = focus,
    drag: Float = press,
): IndicationNodeFactory {
    return ScaleIndicationNodeFactory(focus, press, hover, drag)
}

private class ScaleIndicationNodeFactory(
    val focus: Float,
    val press: Float,
    val hover: Float,
    val drag: Float,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndication(focus, press, hover, drag, interactionSource)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ScaleIndicationNodeFactory) return false
        return focus == other.focus &&
            press == other.press &&
            hover == other.hover &&
            drag == other.drag
    }

    override fun hashCode(): Int {
        var result = focus.hashCode()
        result = 31 * result + press.hashCode()
        result = 31 * result + hover.hashCode()
        result = 31 * result + drag.hashCode()
        return result
    }
}

private class ScaleIndication(
    val focus: Float,
    val press: Float,
    val hover: Float,
    val drag: Float,
    val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode {

    var scale = Animatable(1f)

    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect {
                val target = when (it) {
                    is PressInteraction.Press -> press
                    is FocusInteraction.Focus -> focus
                    is HoverInteraction.Enter -> hover
                    is DragInteraction.Start -> drag
                    else -> 1f
                }
                scale.animateTo(target)
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(scale.value) {
            this@draw.drawContent()
        }
    }
}
