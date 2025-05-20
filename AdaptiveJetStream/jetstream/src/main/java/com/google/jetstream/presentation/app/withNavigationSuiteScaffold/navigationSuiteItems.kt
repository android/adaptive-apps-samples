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
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.SpatialConfiguration
import androidx.xr.runtime.Session
import com.google.jetstream.R
import com.google.jetstream.presentation.screens.Screens

fun NavigationSuiteScope.navigationSuiteItems(
    currentScreen: Screens,
    screens: List<Screens>,
    onSelectScreen: (Screens) -> Unit,
) {
    screens.forEach { screen ->
        item(
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

fun NavigationSuiteScope.toggleFullSpaceMode(
    xrSession: Session?,
    spatialConfiguration: SpatialConfiguration,
    isSpatialUiEnabled: Boolean,
) {

    if (xrSession != null) {
        val icon = if (isSpatialUiEnabled) {
            R.drawable.ic_collapse_content
        } else {
            R.drawable.ic_expand_content
        }
        val description = if (isSpatialUiEnabled) {
            R.string.home_space_mode
        } else {
            R.string.full_space_mode
        }

        item(
            selected = false,
            onClick = {
                if (isSpatialUiEnabled) {
                    spatialConfiguration.requestHomeSpaceMode()
                } else {
                    spatialConfiguration.requestFullSpaceMode()
                }
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    modifier = Modifier.size(24.dp),
                    contentDescription = stringResource(description),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(stringResource(description), color = MaterialTheme.colorScheme.primary)
            }
        )
    }
}
