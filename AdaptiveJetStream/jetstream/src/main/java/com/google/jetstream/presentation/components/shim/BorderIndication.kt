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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.ShapeTokens
import kotlinx.coroutines.launch

fun Modifier.borderIndication(
    interactionSource: InteractionSource,
    focused: Border = Border.None,
    pressed: Border = focused,
    hover: Border = focused,
    dragged: Border = pressed,
): Modifier =
    indication(interactionSource, borderIndication(focused, pressed, hover, dragged))

fun borderIndication(
    focused: Border = Border.None,
    pressed: Border = focused,
    hover: Border = focused,
    dragged: Border = pressed,
) = BorderIndicationNodeFactory(focused, pressed, hover, dragged)

class BorderIndicationNodeFactory(
    private val focused: Border = Border.None,
    private val pressed: Border = focused,
    private val hover: Border = focused,
    private val dragged: Border = pressed,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return BorderIndicationNode(interactionSource, focused, pressed, hover, dragged)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is BorderIndicationNodeFactory) return false
        return focused == other.focused &&
            pressed == other.pressed &&
            hover == other.hover &&
            dragged == other.dragged
    }

    override fun hashCode(): Int {
        return focused.hashCode() * 31 +
            pressed.hashCode() * 31 +
            hover.hashCode() * 31 +
            dragged.hashCode()
    }
}

private class BorderIndicationNode(
    private val interactionSource: InteractionSource,
    private val focused: Border = Border.None,
    private val pressed: Border = focused,
    private val hover: Border = focused,
    private val dragged: Border = pressed,
) : Modifier.Node(),
    DrawModifierNode {

    private var state: Border = Border.None

    // Snip onAttach method and attribute declarations
    override fun onAttach() {
        coroutineScope.launch {
            interactionSource.interactions.collect { interaction ->
                state = when (interaction) {
                    is PressInteraction.Press -> pressed
                    is FocusInteraction.Focus -> focused
                    is HoverInteraction.Enter -> hover
                    is DragInteraction.Start -> dragged
                    else -> Border.None
                }
                invalidateDraw()
            }
        }
    }

    override fun ContentDrawScope.draw() {
        drawContent()

        val border = state
        if (border.stroke.width.value > 0) {
            val outline = border.shape.createOutline(size, layoutDirection, this)
            inset(inset = -border.inset.toPx()) {
                drawOutline(
                    outline = outline,
                    brush = border.stroke.brush,
                    style = Stroke(width = border.stroke.width.toPx(), cap = StrokeCap.Round)
                )
            }
        }
    }
}

@Immutable
class Border(
    val stroke: BorderStroke,
    val inset: Dp = 0.dp,
    val shape: Shape = ShapeTokens.BorderDefaultShape
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Border

        if (stroke != other.stroke) return false
        if (inset != other.inset) return false
        if (shape != other.shape) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stroke.hashCode()
        result = 31 * result + inset.hashCode()
        result = 31 * result + shape.hashCode()
        return result
    }

    override fun toString(): String {
        return "Border(border=$stroke, inset=$inset, shape=$shape)"
    }

    fun copy(border: BorderStroke? = null, inset: Dp? = null, shape: Shape? = null): Border =
        Border(
            stroke = border ?: this.stroke,
            inset = inset ?: this.inset,
            shape = shape ?: this.shape
        )

    companion object {
        /**
         * Signifies the absence of a border. Use this if you do not want to display a border
         * indication in any of the TV Components.
         */
        val None =
            Border(
                stroke = BorderStroke(width = 0.dp, color = Color.Transparent),
                inset = 0.dp,
                shape = RectangleShape
            )
    }
}
