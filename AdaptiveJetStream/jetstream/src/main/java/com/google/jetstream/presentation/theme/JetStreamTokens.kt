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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.shim.indication.Border
import com.google.jetstream.presentation.components.shim.indication.borderIndication
import com.google.jetstream.presentation.components.shim.indication.contentColorIndication
import com.google.jetstream.presentation.components.shim.indication.scaleIndication

val JetStreamBorder
    @Composable get() =
        Border.current(width = JetStreamTokens.BorderWidth)

object JetStreamTokens {
    val ScreenOverScanMargin = PaddingValues(bottom = 108.dp)
    val SectionGap = 32.dp

    val CardShape = ShapeDefaults.ExtraSmall
    val ButtonShape = ShapeDefaults.ExtraSmall
    val IconSize = DpSize(20.dp, 20.dp)
    val BorderWidth = 3.dp

    /**
     * Space to be given below every Lazy (or scrollable) vertical list throughout the app
     */
    val VerticalListBottomPadding = 28.dp

    const val SUBTITLE_ALPHA = 0.6f
    const val DESCRIPTION_ALPHA = 0.8f

    // val TitleListGap = 16.dp
    val TitleListGap = 0.dp

    const val PORTRAIT_CARD_ASPECT_RATIO = 16f / 9f
    const val LANDSCAPE_CARD_ASPECT_RATIO = 10.5f / 16f
    val CardWidth = 126.dp
    val ImmersiveListCardWidth = 160.dp
    val PortraitCardSize = DpSize(CardWidth, CardWidth * PORTRAIT_CARD_ASPECT_RATIO)
    val LandscapeCardSize =
        DpSize(ImmersiveListCardWidth, ImmersiveListCardWidth * LANDSCAPE_CARD_ASPECT_RATIO)

    val LeanbackWindowSize = DpSize(960.dp, 540.dp)

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun indication(): Style {
        return borderIndication() then scaleIndication()
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun borderIndication(): Style {
        return borderIndication(focused = BorderWidth)
    }

    @OptIn(ExperimentalFoundationStyleApi::class)
    @Composable
    fun contentColorIndication(): Style {
        val contentColor =
            when (LocalEngagementMode.current) {
                EngagementMode.Leanback -> Color.Transparent
                else -> MaterialTheme.colorScheme.onSurface
            }

        return contentColorIndication(
            contentColor = contentColor,
            focusedContentColor = MaterialTheme.colorScheme.onSurface,
        )
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

    @Composable
    fun gridCells(): GridCells {
        val engagementMode = LocalEngagementMode.current
        return remember(engagementMode) {
            when (engagementMode) {
                is EngagementMode.Compact -> GridCells.Fixed(3)
                is EngagementMode.Medium -> GridCells.Fixed(4)
                else -> GridCells.Fixed(6)
            }
        }
    }
}
