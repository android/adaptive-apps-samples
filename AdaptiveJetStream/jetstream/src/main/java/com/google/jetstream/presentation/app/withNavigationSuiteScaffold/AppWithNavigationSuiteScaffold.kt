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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.screens.Screens

@Composable
fun AppWithNavigationSuiteScaffold(
    selectedScreen: Screens,
    isNavigationVisible: Boolean,
    isTopBarVisible: Boolean,
    topBarPaddingTop: Dp,
    onFocusChanged: (Boolean) -> Unit,
    onShowScreen: (Screens) -> Unit,
    navigationItems: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navigationSuiteScaffoldState = rememberNavigationSuiteScaffoldState()

    LaunchedEffect(isNavigationVisible) {
        if (isNavigationVisible) {
            navigationSuiteScaffoldState.show()
        } else {
            navigationSuiteScaffoldState.hide()
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
                    visible = isNavigationVisible,
                    enter = slideInVertically(),
                    exit = slideOutVertically()
                ) {
                    TopBar(
                        selectedScreen = selectedScreen,
                        isTopBarVisible = isTopBarVisible,
                        onFocusChanged = { onFocusChanged(it) },
                        onShowScreen = { screen -> onShowScreen(screen) },
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

