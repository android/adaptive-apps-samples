/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by a_i_generated_fs_write_error in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavKey
import com.google.jetstream.presentation.screens.Screens

class AppState internal constructor(
    initialTopBarVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) {
    var isTopBarVisible by mutableStateOf(initialTopBarVisibility)
        private set

    var selectedScreen by mutableStateOf(initialScreen)
        private set

    var isTopBarFocused by mutableStateOf(false)

    var isNavigationVisible by mutableStateOf(true)

    private var navigationComponentType
        by mutableStateOf(NavigationComponentType.NavigationSuiteScaffold)

    fun showTopBar() {
        isTopBarVisible = true
    }

    fun hideTopBar() {
        isTopBarVisible = false
    }

    fun updateTopBarFocusState(hasFocus: Boolean) {
        isTopBarFocused = hasFocus
    }

    fun updateSelectedScreen(navKey: NavKey) {
        if (navKey is Screens) {
            selectedScreen = navKey
            updateNavigationVisibility()
        }
    }

    fun updateNavigationComponentType(type: NavigationComponentType) {
        navigationComponentType = type
        updateNavigationVisibility()
    }

    private fun updateNavigationVisibility() {
        isNavigationVisible = when (navigationComponentType) {
            NavigationComponentType.NavigationSuiteScaffold -> {
                selectedScreen.navigationVisibility.isVisibleInNavigationSuite
            }

            NavigationComponentType.TopBar -> {
                selectedScreen.navigationVisibility.isVisibleInCustomNavigation
            }
        }
    }

    private fun snapshot(): Pair<Boolean, Screens> {
        return isTopBarVisible to selectedScreen
    }

    companion object {
        val Saver = Saver<AppState, Pair<Boolean, Screens>>(
            save = { it.snapshot() },
            restore = {
                AppState(it.first, it.second)
            }
        )
    }
}

@Composable
fun rememberAppState(
    initialIsVisibility: Boolean = true,
    initialScreen: Screens = Screens.Home,
) = rememberSaveable(saver = AppState.Saver) {
    AppState(initialIsVisibility, initialScreen)
}
