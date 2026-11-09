/*
 * Copyright 2024 Google LLC
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

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.then
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.jetstream.R
import com.google.jetstream.presentation.components.shim.styleable.StyleableBox

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun Loading(
    modifier: Modifier = Modifier,
    style: Style = Style(),
) {
    val textStyle = MaterialTheme.typography.displayMedium
    StyleableBox(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        style =
            Style {
                textStyle(textStyle)
                fillSize()
            } then style,
    ) {
        Text(text = stringResource(id = R.string.message_loading))
    }
}
