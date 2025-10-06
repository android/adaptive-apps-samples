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

package com.google.jetstream.presentation.app

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.jetstream.presentation.components.feature.hasXrSpatialFeature
import com.google.jetstream.presentation.components.feature.isAutomotiveEnabled
import com.google.jetstream.presentation.components.feature.isLeanbackEnabled
import com.google.jetstream.presentation.components.feature.isSpatialUiEnabled
import com.google.jetstream.presentation.components.feature.isWidthAtLeastLarge

enum class NavigationComponentType {
    NavigationSuiteScaffold,
    TopBar,
    Spatial
}

@Composable
fun rememberNavigationComponentType(): NavigationComponentType {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isLeanbackEnabled = isLeanbackEnabled()
    val isAutomotiveEnabled = isAutomotiveEnabled()
    val isSpatialUiEnabled = hasXrSpatialFeature() && isSpatialUiEnabled()

    return remember(isLeanbackEnabled, isAutomotiveEnabled, windowSizeClass, isSpatialUiEnabled) {
        selectNavigationComponentType(
            isLeanbackEnabled = isLeanbackEnabled,
            isAutomotiveEnabled = isAutomotiveEnabled,
            isLargeWindow = windowSizeClass.isWidthAtLeastLarge(),
            isSpatialUiEnabled = isSpatialUiEnabled
        )
    }
}

// Select the navigation component type based on the available input devices.
private fun selectNavigationComponentType(
    isLeanbackEnabled: Boolean,
    isAutomotiveEnabled: Boolean,
    isLargeWindow: Boolean,
    isSpatialUiEnabled: Boolean,
): NavigationComponentType {
    return when {
        isSpatialUiEnabled -> NavigationComponentType.Spatial
        isLeanbackEnabled -> NavigationComponentType.TopBar
        isAutomotiveEnabled -> NavigationComponentType.TopBar
        isLargeWindow -> NavigationComponentType.TopBar
        else -> NavigationComponentType.NavigationSuiteScaffold
    }
}
