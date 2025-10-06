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

package com.google.jetstream.presentation.app.withNavigationSuiteScaffold

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.platform.SpatialConfiguration
import com.google.jetstream.R
import com.google.jetstream.presentation.components.feature.hasXrSpatialFeature
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AdaptiveAppNavigationItems(
    currentScreen: Screens,
    screens: List<Screens>,
    onSelectScreen: (Screens) -> Unit,
) {
    screens.forEach { screen ->
        NavigationSuiteItem(
            selected = screen == currentScreen,
            onClick = {
                onSelectScreen(screen)
            },
            label = { Text(screen.name, color = MaterialTheme.colorScheme.primary) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(screen.navIcon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = screen.name,
                    tint = MaterialTheme.colorScheme.primary
                )
            },
        )
    }
}

@Composable
fun RequestFullSpaceModeItem(
    hasXrSpatialFeature: Boolean = hasXrSpatialFeature(),
    spatialConfiguration: SpatialConfiguration = LocalSpatialConfiguration.current
) {

    if (hasXrSpatialFeature) {
        NavigationSuiteItem(
            selected = false,
            onClick = spatialConfiguration::requestFullSpaceMode,
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_expand_content),
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(R.string.full_space_mode),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    stringResource(R.string.full_space_mode),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}
