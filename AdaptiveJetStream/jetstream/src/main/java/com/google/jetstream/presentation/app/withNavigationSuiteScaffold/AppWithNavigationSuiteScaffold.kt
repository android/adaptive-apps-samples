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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldValue
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.platform.LocalSpatialConfiguration
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationTree
import com.google.jetstream.presentation.app.updateTopBarVisibility
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AppWithNavigationSuiteScaffold(
    appState: AppState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
    val screensInGlobalNavigation = remember {
        Screens.entries.filter { it.isMainNavigation }
    }

    val xrSession = LocalSession.current
    val isSpatialUiEnabled = LocalSpatialCapabilities.current.isSpatialUiEnabled
    val spatialConfiguration = LocalSpatialConfiguration.current
    val navigationType =
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())

    LaunchedEffect(appState.isNavigationVisible) {
        if (appState.isNavigationVisible) {
            navigationSuiteScaffoldState.show()
        } else {
            navigationSuiteScaffoldState.hide()
        }
    }

    val paddingTop = remember(xrSession) {
        if (xrSession != null) {
            32.dp
        } else {
            0.dp
        }
    }

    val shouldShowTopBar =
        navigationSuiteScaffoldState.currentValue == NavigationSuiteScaffoldValue.Visible

    NavigationSuiteScaffold(
        modifier = modifier.fillMaxSize(),
        state = navigationSuiteScaffoldState,
        navigationItems = {
            AdaptiveAppNavigationItems(appState.selectedScreen, screensInGlobalNavigation) {
                if (it != appState.selectedScreen) {
                    navController.navigate(it())
                }
            }
            if (xrSession != null) {
                ToggleFullSpaceModeItem(
                    xrSession = xrSession,
                    isSpatialUiEnabled = isSpatialUiEnabled,
                    spatialConfiguration = spatialConfiguration
                )
            }
        },
        navigationItemVerticalArrangement = Arrangement.Center
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AnimatedVisibility(
                    visible = shouldShowTopBar,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    val horizontalPadding = remember(navigationType) {
                        when (navigationType) {
                            NavigationSuiteType.NavigationRail -> 32.dp
                            else -> 8.dp
                        }
                    }
                    TopBar(
                        appState = appState,
                        navController = navController,
                        modifier = Modifier.padding(
                            start = horizontalPadding,
                            end = horizontalPadding,
                            top = paddingTop
                        )
                    )
                }
            }
        ) { padding ->
            NavigationTree(
                navController = navController,
                isTopBarVisible = appState.isTopBarVisible,
                modifier = modifier.padding(padding),
                onScroll = { updateTopBarVisibility(appState, it) }
            )
        }
    }
}
