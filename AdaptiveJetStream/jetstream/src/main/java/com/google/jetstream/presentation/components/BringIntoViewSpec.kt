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

package com.google.jetstream.presentation.components

import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.runtime.Composable
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import kotlin.math.abs

val NoScrollSpec =
    object : BringIntoViewSpec {
        override fun calculateScrollDistance(
            offset: Float,
            size: Float,
            containerSize: Float,
        ): Float {
            return 0f
        }
    }

@Composable
fun defaultBringIntoViewSpec(): BringIntoViewSpec {
    return when (LocalEngagementMode.current) {
        EngagementMode.Leanback -> PivotBringIntoViewSpec
        else -> DefaultBringIntoViewSpec
    }
}

private val DefaultBringIntoViewSpec = object : BringIntoViewSpec {}

private val PivotBringIntoViewSpec =
    object : BringIntoViewSpec {
        val parentFraction = 0.3f
        val childFraction = 0f

        override fun calculateScrollDistance(
            offset: Float,
            size: Float,
            containerSize: Float,
        ): Float {
            val leadingEdgeOfItemRequestingFocus = offset
            val trailingEdgeOfItemRequestingFocus = offset + size

            val sizeOfItemRequestingFocus =
                abs(trailingEdgeOfItemRequestingFocus - leadingEdgeOfItemRequestingFocus)
            val childSmallerThanParent = sizeOfItemRequestingFocus <= containerSize
            val initialTargetForLeadingEdge =
                parentFraction * containerSize - (childFraction * sizeOfItemRequestingFocus)
            val spaceAvailableToShowItem = containerSize - initialTargetForLeadingEdge

            val targetForLeadingEdge =
                if (
                    childSmallerThanParent && spaceAvailableToShowItem < sizeOfItemRequestingFocus
                ) {
                    containerSize - sizeOfItemRequestingFocus
                } else {
                    initialTargetForLeadingEdge
                }

            return leadingEdgeOfItemRequestingFocus - targetForLeadingEdge
        }
    }
