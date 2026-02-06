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
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.rememberNavigationSuiteScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.feature.hasXrSpatialFeature
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AppWithNavigationSuiteScaffold(
    appState: AppState,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    keyboardShortcuts: List<KeyboardShortcut> = emptyList(),
    content: @Composable (PaddingValues) -> Unit
) {
    EnableProminentMovieListOverride {
        NavigationSuiteScaffoldLayout(
            keyboardShortcuts = keyboardShortcuts,
            modifier = modifier,
            isNavigationVisible = appState.isNavigationVisible,
            navigationItems = {
                AdaptiveAppNavigationItems(
                    currentScreen = appState.selectedScreen,
                    screens = Screens.mainNavigationScreens,
                    onSelectScreen = { screen ->
                        if (screen != appState.selectedScreen) {
                            navController.navigate(screen())
                        }
                    },
                )
                if (hasXrSpatialFeature()) {
                    RequestFullSpaceModeItem()
                }
            },
            content = content,
            topBar = {
                val hasXrSpatialFeature = hasXrSpatialFeature()

                // TODO: This is specific to XR home-space mode
                val topBarPaddingTop = remember(hasXrSpatialFeature) {
                    if (hasXrSpatialFeature) {
                        32.dp
                    } else {
                        0.dp
                    }
                }

                AnimatedVisibility(
                    visible = appState.isTopBarVisible,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    TopAppBar(
                        modifier = Modifier
                            .padding(
                                start = 24.dp,
                                end = 24.dp,
                                top = topBarPaddingTop
                            )
                            .onFocusChanged { appState.updateTopBarFocusState(it.hasFocus) },
                        selectedScreen = appState.selectedScreen,
                        showScreen = { screen ->
                            if (screen != appState.selectedScreen) {
                                navController.navigate(screen())
                            }
                        },
                    )
                }
            }
        )
    }
}

@Composable
fun NavigationSuiteScaffoldLayout(
    isNavigationVisible: Boolean,
    modifier: Modifier = Modifier,
    keyboardShortcuts: List<KeyboardShortcut> = emptyList(),
    navigationItems: @Composable () -> Unit,
    content: @Composable ((padding: PaddingValues) -> Unit),
    topBar: @Composable () -> Unit,
) {
    Surface {
        val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()
        LaunchedEffect(key1 = isNavigationVisible) {
            if (isNavigationVisible) {
                navigationSuiteScaffoldState.show()
            } else {
                navigationSuiteScaffoldState.hide()
            }
        }
        NavigationSuiteScaffold(
            modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
            state = navigationSuiteScaffoldState,
            navigationItemVerticalArrangement = Arrangement.Center,
            navigationItems = navigationItems
        ) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = topBar
            ) { paddingValues ->
                content(paddingValues)
            }
        }
    }
}

