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

package com.google.jetstream.presentation.screens.profile.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SettingItem(
    name: String,
    value: String,
    modifier: Modifier = Modifier,
    colors: ListItemColors = SettingItemDefaults.colors,
) {
    SettingItem(
        title = { SettingItemName(name = name) },
        value = { SettingItemValue(value = value) },
        modifier = modifier,
        colors = colors,
    )
}

@Composable
fun SettingItem(
    name: String,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    colors: ListItemColors = SettingItemDefaults.colors,
) {
    SettingItem(
        title = { SettingItemName(name = name) },
        value = value,
        modifier = modifier,
        colors = colors,
    )
}

@Composable
fun SettingItem(
    title: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    colors: ListItemColors = SettingItemDefaults.colors,
) {
    ListItem(
        modifier = modifier,
        colors = colors,
        headlineContent = title,
        trailingContent = value,
    )
}

@Composable
fun SettingItemName(
    name: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
) {
    Text(
        text = name,
        style = textStyle,
        modifier = modifier,
    )
}

@Composable
fun SettingItemValue(
    value: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
) {
    Text(
        text = value,
        style = textStyle,
        modifier = modifier,
    )
}

object SettingItemDefaults {
    val colors: ListItemColors
        @Composable get() =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
            )
}
