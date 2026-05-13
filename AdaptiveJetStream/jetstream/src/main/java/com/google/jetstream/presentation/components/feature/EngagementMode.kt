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
    val hasPointingDevice: Boolean

    data class Compact(
        override val isBackButtonRequired: Boolean = false,
        override val hasImmersiveMode: Boolean = true,
        override val hasPointingDevice: Boolean = false,
    ) : EngagementMode

    data class Medium(
        override val isBackButtonRequired: Boolean = false,
        override val hasImmersiveMode: Boolean = true,
        override val hasPointingDevice: Boolean = false,
    ) : EngagementMode

    data class Workstation(
        override val isBackButtonRequired: Boolean = true,
        override val hasImmersiveMode: Boolean = true,
        override val hasPointingDevice: Boolean = false,
    ) : EngagementMode

    data class Leanback(
        override val hasPointingDevice: Boolean = false,
    ) : EngagementMode {
        override val isBackButtonRequired: Boolean = false
        override val hasImmersiveMode: Boolean = false
    }

    data class Cabin(
        override val hasPointingDevice: Boolean,
    ) : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = true
    }

    data class Enclosed(
        override val hasPointingDevice: Boolean,
    ) : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = false
    }

    data class Spatial(
        override val hasPointingDevice: Boolean,
    ) : EngagementMode {
        override val isBackButtonRequired: Boolean = true
        override val hasImmersiveMode: Boolean = false
    }
}

@Composable
fun currentEngagementMode(): State<EngagementMode> {
    return JetStreamUiMedia.query {
        when {
            isSpatialUiEnabled -> {
                EngagementMode.Spatial(
                    hasPointingDevice = hasPointingDevice,
                )
            }

            hasXrSpatialFeature -> {
                EngagementMode.Enclosed(
                    hasPointingDevice = hasPointingDevice,
                )
            }

            windowSizeClass.isIWidthCompact() -> {
                EngagementMode.Compact(
                    isBackButtonRequired = hasPointingDevice,
                    hasPointingDevice = hasPointingDevice,
                )
            }

            viewingDistance == UiMediaScope.ViewingDistance.Medium && !isMultiWindowMode -> {
                EngagementMode.Cabin(
                    hasPointingDevice = hasPointingDevice,
                )
            }

            viewingDistance == UiMediaScope.ViewingDistance.Far -> {
                EngagementMode.Leanback(
                    hasPointingDevice = hasPointingDevice,
                )
            }

            windowSizeClass.isWidthAtLeastLarge() -> {
                EngagementMode.Workstation(
                    isBackButtonRequired = hasPointingDevice,
                    hasPointingDevice = hasPointingDevice,
                )
            }

            else -> {
                EngagementMode.Medium(
                    isBackButtonRequired = hasPointingDevice,
                    hasPointingDevice = hasPointingDevice,
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
