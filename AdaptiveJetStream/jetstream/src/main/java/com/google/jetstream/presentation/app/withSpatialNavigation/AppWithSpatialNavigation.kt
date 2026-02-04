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

package com.google.jetstream.presentation.app.withSpatialNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.material3.NavigationRail
import androidx.xr.compose.platform.LocalSpatialConfiguration
import androidx.xr.compose.platform.SpatialConfiguration
import androidx.xr.compose.spatial.ApplicationSubspace
import androidx.xr.compose.subspace.MovePolicy
import androidx.xr.compose.subspace.ResizePolicy
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.width
import androidx.xr.compose.unit.DpVolumeSize
import com.google.jetstream.R
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.TopBar
import com.google.jetstream.presentation.screens.Screens

@OptIn(ExperimentalMaterial3XrApi::class)
@Composable
fun AppWithSpatialNavigation(
    selectedScreen: Screens,
    isNavigationVisible: Boolean,
    isTopBarVisible: Boolean,
    onShowScreen: (Screens) -> Unit,
    onTopBarFocusChanged : (Boolean) -> Unit,
    containerColor: Color,
    content: @Composable (PaddingValues) -> Unit
) {
    val resizePolicy = remember {
        ResizePolicy(minimumSize = DpVolumeSize(800.dp, 800.dp, 0.dp))
    }
    val dragPolicy = remember {
        MovePolicy()
    }

    // TODO: This code is repeated in several places
    val screensInGlobalNavigation = remember {
        Screens.entries.filter { it.isMainNavigation }
    }

    ApplicationSubspace {
        SpatialPanel(
            resizePolicy = resizePolicy,
            dragPolicy = dragPolicy,
            modifier = SubspaceModifier.width(1280.dp).height(900.dp)
        ) {
            Scaffold(
                topBar = {
                    AnimatedVisibility(
                        // TODO: Should this be isTopBarVisible?
                        visible = isNavigationVisible,
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ) {
                        TopBar(
                            selectedScreen = selectedScreen,
                            isTopBarVisible = isTopBarVisible,
                            onFocusChanged = { onTopBarFocusChanged(it) },
                            onShowScreen = { onShowScreen(it) },
                            modifier = Modifier.padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = 32.dp
                            )
                        )
                    }
                },
                containerColor = containerColor,
            ) {
                padding -> content(padding)
            }
            AnimatedVisibility(isNavigationVisible) {
                NavigationInObiter(
                    screens = screensInGlobalNavigation,
                    currentScreen = selectedScreen
                ) {
                    onShowScreen(it)
                }
            }
        }
    }
}

@Composable
private fun NavigationInObiter(
    screens: List<Screens>,
    currentScreen: Screens,
    spatialConfiguration: SpatialConfiguration = LocalSpatialConfiguration.current,
    onScreenSelected: (Screens) -> Unit = {}
) {
    NavigationRailInObiter(
        screens = screens,
        currentScreen = currentScreen,
        spatialConfiguration = spatialConfiguration,
        onScreenSelected = onScreenSelected
    )
}

@OptIn(ExperimentalMaterial3XrApi::class)
@Composable
private fun NavigationRailInObiter(
    screens: List<Screens>,
    currentScreen: Screens,
    spatialConfiguration: SpatialConfiguration = LocalSpatialConfiguration.current,
    onScreenSelected: (Screens) -> Unit = {}
) {
    NavigationRail {
        screens.forEach { screen ->
            val isSelected = screen == currentScreen
            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        onScreenSelected(screen)
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(screen.navIcon),
                        contentDescription = screen.name,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                label = {
                    Text(screen.name, color = MaterialTheme.colorScheme.primary)
                }
            )
        }
        NavigationRailItem(
            selected = false,
            onClick = spatialConfiguration::requestHomeSpaceMode,
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_collapse_content),
                    modifier = Modifier.size(48.dp),
                    contentDescription = stringResource(R.string.home_space_mode),
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            label = {
                Text(
                    stringResource(R.string.home_space_mode),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )
    }
}

// TODO: Is it possible to preview a composable that uses XR UI components?
@Preview
@Composable
fun AppWithSpatialNavigationPreview() {
    AppWithSpatialNavigation(
        selectedScreen = Screens.Home,
        isNavigationVisible = true,
        isTopBarVisible = true,
        onShowScreen = { },
        onTopBarFocusChanged = {  },
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Text("Preview content")
    }
}
