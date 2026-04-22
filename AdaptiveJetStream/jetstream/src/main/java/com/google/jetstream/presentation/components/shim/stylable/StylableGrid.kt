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

@file:OptIn(ExperimentalFoundationStyleApi::class, ExperimentalGridApi::class)

package com.google.jetstream.presentation.components.shim.stylable

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridConfigurationScope
import androidx.compose.foundation.layout.GridScope
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A grid component that supports styling via the [Style] API.
 *
 * @param config The configuration for the grid (rows, columns, etc.).
 * @param modifier The modifier to be applied to the grid.
 * @param style The style to be applied to the grid.
 * @param content The content of the grid.
 */
@Composable
fun StylableGrid(
    config: GridConfigurationScope.() -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    content: @Composable GridScope.() -> Unit,
) {
    Grid(
        config = config,
        modifier = modifier.styleable(style = style),
        content = content,
    )
}
