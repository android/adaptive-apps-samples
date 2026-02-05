/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.NavigationTree
import com.google.jetstream.presentation.app.rememberAppState
import com.google.jetstream.presentation.app.rememberKeyboardShortcuts
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.app.updateTopBarVisibility
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AdaptiveAppNavigationItems
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AppWithNavigationSuiteScaffold
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.EnableProminentMovieListOverride
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.RequestFullSpaceModeItem
import com.google.jetstream.presentation.app.withSpatialNavigation.AppWithSpatialNavigation
import com.google.jetstream.presentation.app.withTopBarNavigation.AppWithTopBarNavigation
import com.google.jetstream.presentation.components.feature.hasXrSpatialFeature
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.screens.Screens

@Composable
fun App(
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppState()
) {
    val navController = rememberNavController()

    // TODO: Rather than starting our decision making with the navigation component type, we should
    //  start it with the layout. Then populate that layout with the correct composables.
    val navigationComponentType = rememberNavigationComponentType()

    val keyboardShortcuts = rememberKeyboardShortcuts(navController, appState.selectedScreen)

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            appState.updateSelectedScreen(destination)
        }
    }

    LaunchedEffect(navigationComponentType) {
        appState.updateNavigationComponentType(navigationComponentType)
    }

    when (navigationComponentType) {
        NavigationComponentType.NavigationSuiteScaffold -> {
            EnableProminentMovieListOverride {
                Surface {

                    val hasXrSpatialFeature = hasXrSpatialFeature()
                    val screensInGlobalNavigation = remember {
                        Screens.entries.filter { it.isMainNavigation }
                    }

                    // TODO: This is specific to XR home-space mode
                    val topBarPaddingTop = remember(hasXrSpatialFeature) {
                        if (hasXrSpatialFeature) {
                            32.dp
                        } else {
                            0.dp
                        }
                    }

                    AppWithNavigationSuiteScaffold(
                        selectedScreen = appState.selectedScreen,
                        isNavigationVisible = appState.isNavigationVisible,
                        isTopBarVisible = appState.isTopBarVisible,
                        topBarPaddingTop = topBarPaddingTop,
                        onShowScreen = { screen ->
                            navController.navigate(screen())
                        },
                        onFocusChanged = { appState.updateTopBarFocusState(it) },
                        modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
                        navigationItems = {
                            AdaptiveAppNavigationItems(
                                currentScreen = appState.selectedScreen,
                                screens = screensInGlobalNavigation,
                                onSelectScreen = { screen ->
                                    if (screen != appState.selectedScreen) {
                                        navController.navigate(screen())
                                    }
                                },
                            )
                            if (hasXrSpatialFeature) {
                                RequestFullSpaceModeItem()
                            }
                        },
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
        }

        NavigationComponentType.TopBar -> {
            Surface {
                AppWithTopBarNavigation(
                    selectedScreen = appState.selectedScreen,
                    isNavigationVisible = appState.isNavigationVisible,
                    isTopBarVisible = appState.isNavigationVisible && appState.isTopBarVisible,
                    isTopBarFocussed = appState.isTopBarFocused,
                    onTopBarFocusChanged = { hasFocus ->
                        appState.updateTopBarFocusState(hasFocus)
                    },
                    onTopBarVisible = { appState.showTopBar() },
                    onActivityBackPressed = onActivityBackPressed,
                    showScreen = { screen ->
                        navController.navigate(screen())
                    },
                    modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
                ) {
                    NavigationTree(
                        navController = navController,
                        isTopBarVisible = appState.isTopBarVisible,
                        onScroll = { updateTopBarVisibility(appState, it) }
                    )
                }
            }
        }

        NavigationComponentType.Spatial -> {
            EnableProminentMovieListOverride {
                AppWithSpatialNavigation(
                    selectedScreen = appState.selectedScreen,
                    isNavigationVisible = appState.isNavigationVisible,
                    isTopBarVisible = appState.isTopBarVisible,
                    onShowScreen = { screen ->
                        navController.navigate(screen())
                    },
                    onTopBarFocusChanged = { appState.updateTopBarFocusState(it) },
                    containerColor = appState.selectedScreen.xrContainerColor(),
                ) { paddingValues ->
                    NavigationTree(
                        navController = navController,
                        isTopBarVisible = appState.isTopBarVisible,
                        modifier = Modifier
                            .handleKeyboardShortcuts(keyboardShortcuts)
                            .padding(paddingValues),
                        onScroll = { updateTopBarVisibility(appState, it) }
                    )
                }
            }
        }
    }
}


