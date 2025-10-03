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

package com.google.jetstream.presentation.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.components.feature.isIWidthCompact
import com.google.jetstream.presentation.components.feature.isWidthMedium

val LocalFeaturedCarouselHeight: ProvidableCompositionLocal<Dp> = staticCompositionLocalOf {
    324.dp
}

val LocalVerticalCardAspectRatio: ProvidableCompositionLocal<Float> = staticCompositionLocalOf {
    10.5f / 16f
}

val LocalHorizontalCardAspectRatio: ProvidableCompositionLocal<Float> = staticCompositionLocalOf {
    16f / 9f
}

val LocalCardWidth: ProvidableCompositionLocal<Dp> = staticCompositionLocalOf {
    126.dp
}

val LocalProminentCardSize: ProvidableCompositionLocal<DpSize> = staticCompositionLocalOf {
    DpSize(432.dp, 216.dp)
}

@Composable
fun rememberFeaturedCarouselHeight(
    navigationComponentType: NavigationComponentType = rememberNavigationComponentType()
): Dp {
    return remember(navigationComponentType) {
        when (navigationComponentType) {
            NavigationComponentType.TopBar -> 324.dp
            else -> 385.dp
        }
    }
}

@Composable
fun rememberVerticalCardAspectRatio(
    navigationComponentType: NavigationComponentType = rememberNavigationComponentType()
): Float {
    return remember(navigationComponentType) {
        when (navigationComponentType) {
            NavigationComponentType.TopBar -> 0.65625f // 10.5f / 16f
            else -> 0.67021f
        }
    }
}

@Composable
fun rememberCardWidth(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
): Dp {
    return remember(windowSizeClass) {
        windowSizeClass.cardWidth()
    }
}

private fun WindowSizeClass.cardWidth(): Dp {
    return when {
        isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> {
            165.dp
        }
        else -> {
            126.dp
        }
    }
}

@Composable
fun rememberProminentCardSize(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
): DpSize {
    return remember(windowSizeClass) {
        when {
            !windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND
            ) -> {
                DpSize(360.dp, 360.dp)
            }
            !windowSizeClass.isWidthAtLeastBreakpoint(
                WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
            ) -> {
                DpSize(540.dp, 304.dp)
            }
            else -> {
                DpSize(432.dp, 216.dp)
            }
        }
    }
}

@Composable
fun rememberCategoryGridColumns(
    navigationComponentType: NavigationComponentType = rememberNavigationComponentType()
): GridCells {
    return remember(navigationComponentType) {
        when (navigationComponentType) {
            NavigationComponentType.TopBar -> GridCells.Fixed(4)
            else -> GridCells.Fixed(3)
        }
    }
}

@Composable
fun rememberCategoryCardAspectRatio(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
): Float {
    return when {
        windowSizeClass.isIWidthCompact() -> 1f
        windowSizeClass.isWidthMedium() -> 1.4471f
        else -> 1.7777f // 16:9
    }
}
