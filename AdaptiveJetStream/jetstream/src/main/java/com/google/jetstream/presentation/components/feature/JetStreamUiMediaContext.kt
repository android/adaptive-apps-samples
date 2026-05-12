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

@file:OptIn(ExperimentalMediaQueryApi::class)

package com.google.jetstream.presentation.components.feature

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalMediaQueryApi
import androidx.compose.ui.LocalUiMediaScope
import androidx.compose.ui.UiMediaScope
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.mediaQuery
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration

interface JetStreamUiMediaContext : UiMediaScope {
    val inputModality: InputModality
    val isSpatialUiEnabled: Boolean
    val hasXrSpatialFeature: Boolean
    val windowSizeClass: WindowSizeClass
    val isMultiWindowMode: Boolean
}

private class JetStreamUiMediaContextImpl(
    private val uiMediaScope: UiMediaScope,
    override val inputModality: InputModality,
    override val isSpatialUiEnabled: Boolean,
    override val hasXrSpatialFeature: Boolean,
    override val isMultiWindowMode: Boolean,
) : JetStreamUiMediaContext, UiMediaScope by uiMediaScope {
    override val windowSizeClass: WindowSizeClass
        get() {
            return WindowSizeClass(widthDp = windowWidth.value, heightDp = windowHeight.value)
        }
}

internal object JetStreamUiMedia {
    @Composable
    fun <T> query(mapper: JetStreamUiMediaContext.() -> T): State<T> {
        val currentMapper by rememberUpdatedState(mapper)
        val context = LocalJetStreamUiMediaContext.current
        return remember(context) {
            derivedStateOf {
                context.currentMapper()
            }
        }
    }

    @Composable
    internal fun current(
        inputModality: InputModality = InputModality.select(),
        hasXrSpatialFeature: Boolean = LocalSpatialConfiguration.current.hasXrSpatialFeature,
        isSpatialUiEnabled: Boolean = LocalSpatialCapabilities.current.isSpatialUiEnabled,
        uiMediaScope: UiMediaScope = LocalUiMediaScope.current,
        isMultiWindowMode: Boolean = LocalActivity.current?.isInMultiWindowMode ?: false,
    ): JetStreamUiMediaContext {
        return remember(
            inputModality,
            isSpatialUiEnabled,
            uiMediaScope,
            isMultiWindowMode,
        ) {
            JetStreamUiMediaContextImpl(
                inputModality = inputModality,
                isSpatialUiEnabled = isSpatialUiEnabled,
                uiMediaScope = uiMediaScope,
                hasXrSpatialFeature = hasXrSpatialFeature,
                isMultiWindowMode = isMultiWindowMode,
            )
        }
    }

    internal fun default(): JetStreamUiMediaContext {
        return JetStreamUiMediaContextImpl(
            inputModality = InputModality.Touch,
            isSpatialUiEnabled = false,
            hasXrSpatialFeature = false,
            isMultiWindowMode = false,
            uiMediaScope =
                object : UiMediaScope {
                    override val windowPosture = UiMediaScope.Posture.Flat
                    override val windowWidth = 0.dp
                    override val windowHeight = 0.dp
                    override val pointerPrecision = UiMediaScope.PointerPrecision.Coarse
                    override val keyboardKind = UiMediaScope.KeyboardKind.None
                    override val hasMicrophone = false
                    override val hasCamera = false
                    override val viewingDistance = UiMediaScope.ViewingDistance.Near
                },
        )
    }
}

val LocalJetStreamUiMediaContext = compositionLocalOf { JetStreamUiMedia.default() }

@Composable
fun ProvideJetStreamUiMediaContext(
    jetStreamUiMediaContext: JetStreamUiMediaContext = JetStreamUiMedia.current(),
    content: @Composable () -> Unit = {},
) {
    CompositionLocalProvider(
        LocalJetStreamUiMediaContext provides jetStreamUiMediaContext,
        content = content,
    )
}

sealed interface InputModality {
    data object Touch : InputModality

    data object PointingDevice : InputModality

    data object Dpad : InputModality

    companion object {
        @Composable
        fun select(): InputModality {
            return when {
                isDpadAvailable() -> Dpad

                mediaQuery {
                    pointerPrecision == UiMediaScope.PointerPrecision.Fine ||
                        pointerPrecision == UiMediaScope.PointerPrecision.Blunt
                } -> PointingDevice

                else -> Touch
            }
        }

        @Composable
        private fun isDpadAvailable(
            inputMode: InputMode = LocalInputModeManager.current.inputMode,
            hasNoKeyboard: Boolean = mediaQuery { keyboardKind == UiMediaScope.KeyboardKind.None },
        ): Boolean {
            return remember(inputMode, hasNoKeyboard) {
                inputMode == InputMode.Keyboard && hasNoKeyboard
            }
        }
    }
}
