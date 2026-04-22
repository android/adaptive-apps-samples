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

package com.google.jetstream.presentation.components.shim

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.jetstream.presentation.components.shim.stylable.StylableCard

/**
 * A classic card component that displays an image, title, subtitle, and description.
 * It uses [StylableCard] as its base.
 *
 * @param onClick Called when the card is clicked.
 * @param image The composable for the image.
 * @param title The composable for the title.
 * @param modifier The modifier to be applied to the card.
 * @param style The style to be applied to the card.
 * @param subtitle The composable for the subtitle.
 * @param description The composable for the description.
 */
@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalFlexBoxApi::class)
@Composable
fun ClassicCard(
    onClick: () -> Unit,
    image: @Composable () -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    style: Style = Style,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
) {
    StylableCard(
        onClick = onClick,
        modifier = modifier,
        style = style,
    ) {
        Column(
            modifier = modifier,
        ) {
            image()
            title()
            subtitle()
            description()
        }
    }
}
