/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.presentation.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.shim.Border
import com.google.jetstream.presentation.components.shim.borderIndication
import com.google.jetstream.presentation.components.shim.borderIndicationStyle
import com.google.jetstream.presentation.components.shim.scaleIndicationStyle

val JetStreamBorder
    @Composable get() =
        Border(
            stroke =
                BorderStroke(
                    width = JetStreamTokens.BorderWidth,
                    color = MaterialTheme.colorScheme.onSurface,
                ),
            shape = JetStreamTokens.CardShape,
        )

val jetStreamBorderIndication
    @Composable get() = borderIndication(focused = JetStreamBorder)

object JetStreamTokens {
    val CardShape = ShapeDefaults.ExtraSmall
    val ButtonShape = ShapeDefaults.ExtraSmall
    val IconSize = DpSize(20.dp, 20.dp)
    val BorderWidth = 3.dp

    /**
     * Space to be given below every Lazy (or scrollable) vertical list throughout the app
     */
    val VerticalListBottomPadding = 28.dp


    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun indication(): Style {
        return borderIndicationStyle(focused = BorderWidth) then scaleIndicationStyle()
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun buttonStyle(): Style {
        return Style {
            shape(ButtonShape)
            clip(true)
        } then indication()
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun cardStyle(): Style {
        return Style {
            shape(CardShape)
            clip(true)
        } then indication()
    }

}
