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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.NavigationTree
import com.google.jetstream.presentation.app.rememberAppState
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.app.updateTopBarVisibility
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AdaptiveAppNavigationItems
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AppWithNavigationSuiteScaffold
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.EnableProminentMovieListOverride
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.RequestFullSpaceModeItem
import com.google.jetstream.presentation.app.withSpatialNavigation.AppWithSpatialNavigation
import com.google.jetstream.presentation.app.withTopBarNavigation.AppWithTopBarNavigation
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.ModifierKey
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
    val navigationComponentType = rememberNavigationComponentType()

    // TODO: This could be moved into a separate function, and the keyboard shortcuts could be
    //  associated with each screen directly (maybe through an optional interface)
    val keyboardShortcuts = remember {
        listOf(
            KeyboardShortcut(
                key = Key.Comma,
                modifierKeys = setOf(ModifierKey.Ctrl),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        navController.navigate(Screens.Profile())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.P,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        navController.navigate(Screens.Profile())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.H,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Home) {
                        navController.navigate(Screens.Home())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.C,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Categories) {
                        navController.navigate(Screens.Categories())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.M,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Movies) {
                        navController.navigate(Screens.Movies())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.T,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Shows) {
                        navController.navigate(Screens.Shows())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.F,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Favourites) {
                        navController.navigate(Screens.Favourites())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.Slash,
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        navController.navigate(Screens.Search())
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.S,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        navController.navigate(Screens.Search())
                    }
                }
            ),
        )
    }

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
                            appState.updateSelectedScreen(screen)
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
                    navController = navController,
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

                // Workaround to make video player visible.
                val defaultContainerColor = MaterialTheme.colorScheme.background
                var containerColor by remember {
                    mutableStateOf(defaultContainerColor)
                }

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    val isVideoPlayer =
                        destination.route?.startsWith(Screens.VideoPlayer.name) ?: false
                    containerColor = if (isVideoPlayer) {
                        Color.Transparent
                    } else {
                        defaultContainerColor
                    }
                }

                AppWithSpatialNavigation(
                    selectedScreen = appState.selectedScreen,
                    isNavigationVisible = appState.isNavigationVisible,
                    isTopBarVisible = appState.isTopBarVisible,
                    onShowScreen = { screen ->
                        appState.updateSelectedScreen(screen)
                        navController.navigate(screen())
                    },
                    onTopBarFocusChanged = { appState.updateTopBarFocusState(it) },
                    containerColor = containerColor,
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
