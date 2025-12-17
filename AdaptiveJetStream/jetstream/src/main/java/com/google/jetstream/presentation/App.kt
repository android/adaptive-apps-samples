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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.Navigator
import com.google.jetstream.presentation.app.rememberAppState
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationState
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AppWithNavigationSuiteScaffold
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.EnableProminentMovieListOverride
import com.google.jetstream.presentation.app.withTopBarNavigation.AppWithTopBarNavigation
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.ModifierKey
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.screens.Screens

@Composable
fun App(
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    appState: AppState = rememberAppState()
) {
    val topLevelRoutes = remember {
        listOf(
            Screens.Home,
            Screens.Categories,
            Screens.Movies,
            Screens.Shows,
            Screens.Favourites,
            Screens.Search
        )
    }
    val navigationState = rememberNavigationState(
        startRoute = Screens.Home,
        topLevelRoutes = topLevelRoutes.toSet()
    )
    val navigator = remember { Navigator(navigationState) }
    val navigationComponentType = rememberNavigationComponentType()

    val keyboardShortcuts = remember {
        listOf(
            KeyboardShortcut(
                key = Key.Comma,
                modifierKeys = setOf(ModifierKey.Ctrl),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        navigator.navigate(Screens.Profile)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.P,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Profile) {
                        navigator.navigate(Screens.Profile)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.H,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Home) {
                        navigator.navigate(Screens.Home)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.C,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Categories) {
                        navigator.navigate(Screens.Categories)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.M,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Movies) {
                        navigator.navigate(Screens.Movies)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.T,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Shows) {
                        navigator.navigate(Screens.Shows)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.F,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Favourites) {
                        navigator.navigate(Screens.Favourites)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.Slash,
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        navigator.navigate(Screens.Search)
                    }
                }
            ),
            KeyboardShortcut(
                key = Key.S,
                modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
                action = {
                    if (appState.selectedScreen != Screens.Search) {
                        navigator.navigate(Screens.Search)
                    }
                }
            ),
        )
    }

    LaunchedEffect(navigationState.topLevelRoute) {
        appState.updateSelectedScreen(navigationState.topLevelRoute)
    }

    LaunchedEffect(navigationComponentType) {
        appState.updateNavigationComponentType(navigationComponentType)
    }

    when (navigationComponentType) {
        NavigationComponentType.NavigationSuiteScaffold -> {
            EnableProminentMovieListOverride {
                AppWithNavigationSuiteScaffold(
                    appState = appState,
                    navigator = navigator,
                    modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
                    topLevelRoutes = topLevelRoutes
                )
            }
        }

        NavigationComponentType.TopBar -> {
            AppWithTopBarNavigation(
                appState = appState,
                onActivityBackPressed = onActivityBackPressed,
                navigator = navigator,
                modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
                topLevelRoutes = topLevelRoutes
            )
        }
    }
}
