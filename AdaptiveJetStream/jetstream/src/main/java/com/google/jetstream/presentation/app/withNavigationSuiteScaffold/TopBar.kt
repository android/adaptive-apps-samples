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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.navigation.NavController
import com.google.jetstream.presentation.app.AppState

@Composable
internal fun TopBar(
    appState: AppState,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        appState.isTopBarVisible,
        enter = slideInVertically(),
        exit = slideOutVertically()
    ) {
        TopAppBar(
            modifier = modifier
                .onFocusChanged {
                    appState.updateTopBarFocusState(it.hasFocus)
                },
            selectedScreen = appState.selectedScreen,
            showScreen = {
                appState.updateSelectedScreen(it)
                navController.navigate(it())
            },
        )
    }
}
