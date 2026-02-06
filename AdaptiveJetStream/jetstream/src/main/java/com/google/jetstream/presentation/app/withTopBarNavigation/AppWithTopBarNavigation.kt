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

package com.google.jetstream.presentation.app.withTopBarNavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.components.onBackButtonPressed
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AppWithTopBarNavigation(
    appState: AppState,
    navController: NavHostController,
    keyboardShortcuts: List<KeyboardShortcut>,
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ((padding: PaddingValues) -> Unit)
) {
    Surface {
        TopBarWithNavigationLayout(
            selectedScreen = appState.selectedScreen,
            isNavigationVisible = appState.isNavigationVisible,
            isTopBarVisible = appState.isNavigationVisible && appState.isTopBarVisible,
            isTopBarFocussed = appState.isTopBarFocused,
            onTopBarFocusChanged = { hasFocus ->
                appState.updateTopBarFocusState(hasFocus)
            },
            onTopBarVisible = { appState.showTopBar() },
            onActivityBackPressed = onActivityBackPressed,
            onShowScreen = { screen ->
                navController.navigate(screen())
            },
            modifier = modifier.handleKeyboardShortcuts(keyboardShortcuts),
        ) {
            // TODO: This is to keep things consistent with the other layouts, however,
            //  we should consider whether it's necessary to always apply padding to the
            //  main content
            content(PaddingValues(0.dp))
        }
    }
}

@Composable
fun TopBarWithNavigationLayout(
    selectedScreen: Screens,
    isNavigationVisible: Boolean,
    isTopBarVisible: Boolean,
    isTopBarFocussed: Boolean,
    onTopBarVisible: () -> Unit,
    onTopBarFocusChanged: (Boolean) -> Unit,
    onShowScreen: (Screens) -> Unit,
    onActivityBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val topBar = remember { FocusRequester() }

    Column(
        modifier = modifier.onBackButtonPressed {
            when {
                // TODO: This logic is difficult to understand and should be refactored
                // The VideoPlayer screen doesn't have any navigation
                // The MovieDetails screen doesn't have any navigation when it's displayed in a
                // TopBar layout.
                // These are the only two scenarios where appState.isNavigationVisible is false
                !isNavigationVisible -> {
                    onActivityBackPressed()
                }

                // If the top bar isn't visible then show it - my guess is this is to handle
                // the case where the user has scrolled down and the top menu has disappeared.
                // When testing this on the TV emulator, the app just quits when I tap back.
                !isTopBarVisible -> {
                    onTopBarVisible()
                    topBar.tryRequestFocus()
                }

                // If the top bar isn't focussed then focus it
                !isTopBarFocussed -> {
                    topBar.tryRequestFocus()
                }

                // It feels strange to be doing conditional navigation here
                selectedScreen != Screens.Home -> {
                    onShowScreen(Screens.Home)
                }

                else -> {
                    onActivityBackPressed()
                }
            }
        }
    ) {
        // TODO: Consider refactoring this into a slot
        AnimatedVisibility(isTopBarVisible) {
            TopBar(
                Screens.tabScreens,
                selectedScreen,
                {
                    if (it != selectedScreen) {
                        onShowScreen(it)
                    }
                },
                modifier = Modifier
                    .padding(
                        vertical = 16.dp,
                        horizontal = 74.dp,
                    )
                    .focusRequester(topBar)
                    .onFocusChanged { onTopBarFocusChanged(it.hasFocus) }
            )
        }
        content()
    }
}
