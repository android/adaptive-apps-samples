/*
 * Copyright 2026 Google LLC
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

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.components.BackButton
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.onBackButtonPressed
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class BackNavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val topLevelDestinations =
        listOf(
            Destination.Categories,
            Destination.Movies,
            Destination.Shows,
            Destination.Favourites,
            Destination.Search,
            Destination.Profile,
        )

    private val overlayDestinations =
        listOf(
            Destination.CategoryMovieList(categoryId = "1"),
            Destination.MovieDetails(movieId = "1"),
            Destination.VideoPlayer(movieId = "1"),
        )

    private val profileSubDestinations =
        listOf(
            Destination.About,
            Destination.Accounts,
            Destination.Subtitles,
            Destination.Language,
            Destination.SearchHistory,
            Destination.HelpAndSupport,
        )

    @Test
    fun topLevelScreens_systemBack_returnsToHome() {
        lateinit var navigator: Navigator
        var dispatcherOwner: OnBackPressedDispatcherOwner? = null

        composeTestRule.setContent {
            dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                TestNavigationHost(navigator = navigator)
            }
        }

        for (destination in topLevelDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(destination)
            }
            composeTestRule.onNodeWithText("Screen: $destination").assertIsDisplayed()
            assertEquals(destination, navigator.current)

            composeTestRule.runOnIdle {
                dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
            }

            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
            assertEquals(Destination.Home, navigator.current)
        }
    }

    @Test
    fun switchingMultipleTopLevelScreens_singleBack_returnsDirectlyToHome() {
        lateinit var navigator: Navigator
        var dispatcherOwner: OnBackPressedDispatcherOwner? = null

        composeTestRule.setContent {
            dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                TestNavigationHost(navigator = navigator)
            }
        }

        composeTestRule.runOnIdle {
            topLevelDestinations.forEach { destination ->
                navigator.navigate(destination)
            }
        }
        composeTestRule.onNodeWithText("Screen: ${Destination.Profile}").assertIsDisplayed()
        assertEquals(Destination.Profile, navigator.current)

        composeTestRule.runOnIdle {
            dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
        }

        composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
        assertEquals(Destination.Home, navigator.current)
    }

    @Test
    fun overlayScreens_uiBackButtonClick_returnsToHome() {
        lateinit var navigator: Navigator

        composeTestRule.setContent {
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                CompositionLocalProvider(
                    LocalEngagementMode provides EngagementMode.Compact(isBackButtonRequired = true),
                ) {
                    TestNavigationHost(navigator = navigator)
                }
            }
        }

        for (destination in overlayDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(destination)
            }
            composeTestRule.onNodeWithText("Screen: $destination").assertIsDisplayed()
            assertEquals(destination, navigator.current)

            composeTestRule
                .onNodeWithContentDescription("Back")
                .performClick()

            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
            assertEquals(Destination.Home, navigator.current)
        }
    }

    @Test
    fun overlayScreens_systemBack_returnsToHome() {
        lateinit var navigator: Navigator
        var dispatcherOwner: OnBackPressedDispatcherOwner? = null

        composeTestRule.setContent {
            dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                TestNavigationHost(navigator = navigator)
            }
        }

        for (destination in overlayDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(destination)
            }
            composeTestRule.onNodeWithText("Screen: $destination").assertIsDisplayed()
            assertEquals(destination, navigator.current)

            composeTestRule.runOnIdle {
                dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
            }

            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
            assertEquals(Destination.Home, navigator.current)
        }
    }

    @Test
    fun profileSubScreens_backNavigation_returnsToHome() {
        lateinit var navigator: Navigator
        var dispatcherOwner: OnBackPressedDispatcherOwner? = null

        composeTestRule.setContent {
            dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                TestNavigationHost(navigator = navigator)
            }
        }

        for (subDestination in profileSubDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(Destination.Profile)
                navigator.navigate(subDestination)
            }
            composeTestRule.onNodeWithText("Screen: $subDestination").assertIsDisplayed()
            assertEquals(subDestination, navigator.current)

            // First back returns to Profile
            composeTestRule.runOnIdle {
                dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
            }
            assertEquals(Destination.Profile, navigator.current)

            // Second back returns to Home
            composeTestRule.runOnIdle {
                dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
            }
            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
            assertEquals(Destination.Home, navigator.current)
        }
    }

    @Test
    fun leanbackMode_keyBackFromTopLevelAndOverlayScreens_returnsToHome() {
        lateinit var navigator: Navigator

        composeTestRule.setContent {
            navigator = rememberNavigator(initialDestination = Destination.Home)
            JetStreamTheme {
                CompositionLocalProvider(LocalEngagementMode provides EngagementMode.Leanback()) {
                    TestLeanbackNavigationHost(navigator = navigator)
                }
            }
        }

        // 1. From each top-level screen: first Key.Back focuses TopBar, second Key.Back returns to Home
        for (destination in topLevelDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(destination)
            }
            composeTestRule.onNodeWithText("Screen: $destination").assertIsDisplayed()

            // First Key.Back moves focus from content to TopBar
            composeTestRule.onNodeWithTag("content").performKeyInput {
                pressKey(Key.Back)
            }
            composeTestRule.waitForIdle()
            assertEquals(destination, navigator.current)

            // Second Key.Back navigates back to Home
            composeTestRule.onNodeWithTag("topbar").performKeyInput {
                pressKey(Key.Back)
            }
            composeTestRule.waitForIdle()
            assertEquals(Destination.Home, navigator.current)
            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
        }

        // 2. From each overlay screen: single Key.Back immediately returns to Home
        for (destination in overlayDestinations) {
            composeTestRule.runOnIdle {
                navigator.navigate(destination)
            }
            composeTestRule.onNodeWithText("Screen: $destination").assertIsDisplayed()

            composeTestRule.onNodeWithTag("content").performKeyInput {
                pressKey(Key.Back)
            }
            composeTestRule.waitForIdle()
            assertEquals(Destination.Home, navigator.current)
            composeTestRule.onNodeWithText("Screen: ${Destination.Home}").assertIsDisplayed()
        }
    }
}

@Composable
private fun TestNavigationHost(navigator: Navigator) {
    NavDisplay(
        backStack = navigator.backStack,
        entryProvider =
            entryProvider<NavKey> {
                entry<Destination.Home> { TestScreen(Destination.Home, navigator::goBack) }
                entry<Destination.Categories> { TestScreen(Destination.Categories, navigator::goBack) }
                entry<Destination.Movies> { TestScreen(Destination.Movies, navigator::goBack) }
                entry<Destination.Shows> { TestScreen(Destination.Shows, navigator::goBack) }
                entry<Destination.Favourites> { TestScreen(Destination.Favourites, navigator::goBack) }
                entry<Destination.Search> { TestScreen(Destination.Search, navigator::goBack) }
                entry<Destination.Profile> { TestScreen(Destination.Profile, navigator::goBack) }
                entry<Destination.CategoryMovieList> { TestScreen(it, navigator::goBack, showBackButton = true) }
                entry<Destination.MovieDetails> { TestScreen(it, navigator::goBack, showBackButton = true) }
                entry<Destination.VideoPlayer> { TestScreen(it, navigator::goBack, showBackButton = true) }
                entry<Destination.About> { TestScreen(Destination.About, navigator::goBack) }
                entry<Destination.Accounts> { TestScreen(Destination.Accounts, navigator::goBack) }
                entry<Destination.Subtitles> { TestScreen(Destination.Subtitles, navigator::goBack) }
                entry<Destination.Language> { TestScreen(Destination.Language, navigator::goBack) }
                entry<Destination.SearchHistory> { TestScreen(Destination.SearchHistory, navigator::goBack) }
                entry<Destination.HelpAndSupport> { TestScreen(Destination.HelpAndSupport, navigator::goBack) }
            },
    )
}

@Composable
private fun TestScreen(
    destination: Destination,
    onBackPressed: () -> Unit,
    showBackButton: Boolean = false,
) {
    val contentFocusRequester = remember { FocusRequester() }
    LaunchedEffect(destination) {
        contentFocusRequester.requestFocus()
    }
    BackHandler(enabled = destination != Destination.Home, onBack = onBackPressed)
    Column {
        if (showBackButton) {
            BackButton(onClick = onBackPressed)
        }
        Box(
            modifier =
                Modifier
                    .testTag("content")
                    .focusRequester(contentFocusRequester)
                    .focusable(),
        ) {
            Text(text = "Screen: $destination")
        }
    }
}

@Composable
private fun TestLeanbackNavigationHost(navigator: Navigator) {
    var isTopbarVisible by remember { mutableStateOf(true) }
    var isTopbarFocused by remember { mutableStateOf(false) }
    val topbarFocusRequester = remember { FocusRequester() }
    val isOverlay =
        navigator.current is Destination.CategoryMovieList ||
            navigator.current is Destination.MovieDetails ||
            navigator.current is Destination.VideoPlayer

    Surface(
        modifier =
            Modifier.onBackButtonPressed(
                enabled =
                    (!isOverlay && (!isTopbarVisible || !isTopbarFocused)) ||
                        navigator.backStack.size > 1,
            ) {
                when {
                    isOverlay -> {
                        navigator.goBack()
                    }

                    !isTopbarVisible -> {
                        isTopbarVisible = true
                        topbarFocusRequester.requestFocus()
                    }

                    !isTopbarFocused -> {
                        topbarFocusRequester.requestFocus()
                    }

                    else -> {
                        navigator.goBack()
                    }
                }
            },
    ) {
        Column {
            if (!isOverlay && isTopbarVisible) {
                Box(
                    modifier =
                        Modifier
                            .focusRequester(topbarFocusRequester)
                            .onFocusChanged { isTopbarFocused = it.hasFocus }
                            .focusGroup(),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .testTag("topbar")
                                .focusable(),
                    ) {
                        Text("TopBar")
                    }
                }
            }
            TestNavigationHost(navigator = navigator)
        }
    }
}
