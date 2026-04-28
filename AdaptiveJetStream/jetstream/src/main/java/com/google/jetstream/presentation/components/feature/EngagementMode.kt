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

@file:OptIn(ExperimentalMediaQueryApi::class)

package com.google.jetstream.presentation.components.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalMediaQueryApi
import androidx.compose.ui.UiMediaScope

sealed interface EngagementMode {
    val isBackButtonRequired: Boolean
    val hasImmersiveMode: Boolean

    data class Compact(
        override val isBackButtonRequired: Boolean = false,
        override val hasImmersiveMode: Boolean = true,
    ) : EngagementMode

    data class Medium(
        override val isBackButtonRequired: Boolean = false,
        override val hasImmersiveMode: Boolean = true,
    ) : EngagementMode

    data class Workstation(
        override val isBackButtonRequired: Boolean = true,
        override val hasImmersiveMode: Boolean = true,
    ) : EngagementMode

    data object Leanback : EngagementMode {
        override val isBackButtonRequired: Boolean = false
        override val hasImmersiveMode: Boolean = false
    }

    data object Cabin : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = true
    }

    data object Enclosed : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = false
    }

    data object Spatial : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = false
    }
}

@Composable
fun currentEngagementMode(): State<EngagementMode> {
    return JetStreamUiMedia.map {
        when {
            isSpatialUiEnabled -> {
                EngagementMode.Spatial
            }

            hasXrSpatialFeature -> {
                EngagementMode.Enclosed
            }

            windowSizeClass.isIWidthCompact() -> {
                EngagementMode.Compact(
                    isBackButtonRequired = inputModality == InputModality.PointingDevice,
                )
            }

            viewingDistance == UiMediaScope.ViewingDistance.Medium -> {
                EngagementMode.Cabin
            }

            viewingDistance == UiMediaScope.ViewingDistance.Far -> {
                EngagementMode.Leanback
            }

            windowSizeClass.isWidthAtLeastLarge() -> {
                EngagementMode.Workstation(
                    isBackButtonRequired = inputModality == InputModality.PointingDevice,
                )
            }

            else -> {
                EngagementMode.Medium(
                    isBackButtonRequired = inputModality == InputModality.PointingDevice,
                )
            }
        }
    }
}

val LocalEngagementMode: ProvidableCompositionLocal<EngagementMode> =
    compositionLocalOf { EngagementMode.Compact() }

@Composable
fun ProvideLocalEngagementMode(content: @Composable () -> Unit) {
    ProvideJetStreamUiMediaContext {
        val currentEngagementMode by currentEngagementMode()
        CompositionLocalProvider(
            LocalEngagementMode provides currentEngagementMode,
            content = content,
        )
    }
}
