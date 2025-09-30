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

package com.google.jetstream.presentation.screens.profile.compoents

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.components.feature.isWidthAtLeastLarge

enum class ProfileScreenLayoutType {
    FullyExpanded,
    ListDetail,
}

@Composable
fun rememberProfileScreenLayoutType(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
    navigationComponentType: NavigationComponentType = rememberNavigationComponentType(),
): ProfileScreenLayoutType {
    return when (navigationComponentType) {
        NavigationComponentType.TopBar -> ProfileScreenLayoutType.FullyExpanded
        else -> {
            when {
                windowSizeClass.isWidthAtLeastLarge() -> ProfileScreenLayoutType.FullyExpanded
                else -> ProfileScreenLayoutType.ListDetail
            }
        }
    }
}
