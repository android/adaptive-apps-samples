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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.google.jetstream.presentation.components.feature.isWidthAtLeastExpanded
import com.google.jetstream.presentation.components.feature.isWidthAtLeastLarge

val LocalListItemGap: ProvidableCompositionLocal<Dp> = staticCompositionLocalOf {
    20.dp
}
val LocalProminentListItemGap: ProvidableCompositionLocal<Dp> = staticCompositionLocalOf {
    32.dp
}

@Immutable
data class Padding(
    val start: Dp = 0.dp,
    val top: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp,
) {
    constructor(horizontal: Dp = 0.dp, vertical: Dp = 0.dp) :
        this(horizontal, vertical, horizontal, vertical)
    constructor(all: Dp) : this(all, all, all, all)

    fun intoPaddingValues(): PaddingValues {
        return PaddingValues(start, top, end, bottom)
    }
}

private enum class ContentPadding(val value: Padding) {
    Compact(Padding(horizontal = 16.dp, vertical = 16.dp)),
    Medium(Padding(horizontal = 32.dp, vertical = 16.dp)),
    Expanded(Padding(horizontal = 64.dp, vertical = 16.dp));
}

@Composable
fun rememberContentPadding(
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
): Padding {
    return when {
        windowSizeClass.isWidthAtLeastLarge() -> {
            ContentPadding.Expanded.value
        }

        windowSizeClass.isWidthAtLeastExpanded() -> {
            ContentPadding.Medium.value
        }

        else -> {
            ContentPadding.Compact.value
        }
    }
}

val LocalContentPadding: ProvidableCompositionLocal<Padding> = staticCompositionLocalOf {
    ContentPadding.Expanded.value
}
