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

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.NavigationTree
import com.google.jetstream.presentation.app.rememberAppState
import com.google.jetstream.presentation.app.rememberKeyboardShortcuts
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.app.updateTopBarVisibility
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AppWithNavigationSuiteScaffold
import com.google.jetstream.presentation.app.withSpatialNavigation.AppWithSpatialNavigation
import com.google.jetstream.presentation.app.withTopBarNavigation.AppWithTopBarNavigation

@Composable
fun App(
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppState(),
) {
    val navController = rememberNavController()

    val navigationComponentType = rememberNavigationComponentType()

    val keyboardShortcuts =
        rememberKeyboardShortcuts(
            onSelectScreen = { screen ->
                if (appState.selectedScreen != screen) {
                    navController.navigate(screen())
                }
            },
        )

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            appState.updateSelectedScreen(destination)
        }
    }

    LaunchedEffect(navigationComponentType) {
        appState.updateNavigationComponentType(navigationComponentType)
    }

    // The main content that is displayed on every screen
    val mainContent = @Composable { padding: PaddingValues ->
        NavigationTree(
            navController = navController,
            isTopBarVisible = appState.isTopBarVisible,
            modifier = Modifier.padding(padding),
            onScroll = { updateTopBarVisibility(appState, it) },
        )
    }

    when (navigationComponentType) {
        NavigationComponentType.Spatial -> {
            // Android XR 3D environment, also known as Full Space mode.
            AppWithSpatialNavigation(
                appState = appState,
                navController = navController,
                keyboardShortcuts = keyboardShortcuts,
                modifier = modifier,
            ) { paddingValues ->
                mainContent(paddingValues)
            }
        }

        NavigationComponentType.TopBar -> {
            // TV, Automotive, Large windows on desktop and XR 2D environment (Home space mode).
            AppWithTopBarNavigation(
                appState = appState,
                navController = navController,
                keyboardShortcuts = keyboardShortcuts,
                onActivityBackPressed = onActivityBackPressed,
                modifier = modifier,
            ) { paddingValues ->
                mainContent(paddingValues)
            }
        }

        else -> {
            // All other form factors (phone, tablet, foldable etc).
            AppWithNavigationSuiteScaffold(
                appState = appState,
                navController = navController,
                keyboardShortcuts = keyboardShortcuts,
                modifier = modifier,
            ) { paddingValues ->
                mainContent(paddingValues)
            }
        }
    }
}
