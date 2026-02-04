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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.jetstream.presentation.app.AppState

@Composable
fun AppWithNavigationSuiteScaffold(
    appState: AppState,
    navController: NavHostController,
    hasXrSpatialFeature: Boolean,
    navigationItems: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    LaunchedEffect(appState.isNavigationVisible) {
        if (appState.isNavigationVisible) {
            navigationSuiteScaffoldState.show()
        } else {
            navigationSuiteScaffoldState.hide()
        }
    }

    val topBarPaddingTop = remember(hasXrSpatialFeature) {
        if (hasXrSpatialFeature) {
            32.dp
        } else {
            0.dp
        }
    }

    NavigationSuiteScaffold(
        modifier = modifier,
        state = navigationSuiteScaffoldState,
        navigationItemVerticalArrangement = Arrangement.Center,
        navigationItems = navigationItems
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                AnimatedVisibility(
                    visible = appState.isNavigationVisible,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    TopBar(
                        selectedScreen = appState.selectedScreen,
                        isTopBarVisible = appState.isTopBarVisible,
                        onFocusChanged = { appState.updateTopBarFocusState(it) },
                        onShowScreen = { screen ->
                            appState.updateSelectedScreen(screen)
                            navController.navigate(screen)
                        },
                        modifier = Modifier.padding(
                            start = 24.dp,
                            end = 24.dp,
                            top = topBarPaddingTop
                        )
                    )
                }
            }
        ) { paddingValues -> content(paddingValues) }
    }
}

