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

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import androidx.compose.ui.test.withKeyDown
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.components.handleKeyboardShortcuts
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class AppKeyboardShortcutsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun globalKeyboardShortcuts_navigateToExpectedDestinations() {
        lateinit var navigator: Navigator

        composeTestRule.setContent {
            navigator = rememberNavigator(initialDestination = Destination.Home)
            val shortcuts = rememberAppKeyboardShortcuts(navigator)
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            JetStreamTheme {
                Box(
                    modifier =
                        Modifier
                            .testTag("root")
                            .handleKeyboardShortcuts(shortcuts)
                            .focusRequester(focusRequester)
                            .focusable(),
                ) {
                    Text("Current: ${navigator.current}")
                }
            }
        }

        val rootNode = composeTestRule.onNodeWithTag("root")

        // Ctrl+Alt+M -> Movies
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.M)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Movies, navigator.current)

        // Ctrl+Alt+T -> Shows
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.T)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Shows, navigator.current)

        // Ctrl+Alt+F -> Favourites
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.F)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Favourites, navigator.current)

        // Ctrl+Alt+S -> Search
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.S)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Search, navigator.current)

        // Ctrl+, -> Profile
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                pressKey(Key.Comma)
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Profile, navigator.current)

        // Ctrl+Alt+H -> Home
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.H)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Home, navigator.current)

        // Slash (/) -> Search
        rootNode.performKeyInput {
            pressKey(Key.Slash)
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Search, navigator.current)

        // Ctrl+Alt+P -> Profile
        rootNode.performKeyInput {
            withKeyDown(Key.CtrlLeft) {
                withKeyDown(Key.AltLeft) {
                    pressKey(Key.P)
                }
            }
        }
        composeTestRule.waitForIdle()
        assertEquals(Destination.Profile, navigator.current)
    }
}
