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

package com.google.jetstream.presentation.components

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BackButtonAdaptiveTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun backButton_visibilityFollowsEngagementModeRequirement() {
        var mode by mutableStateOf<EngagementMode>(EngagementMode.Leanback())

        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(LocalEngagementMode provides mode) {
                    BackButton(onClick = {})
                }
            }
        }

        // Leanback mode does not require an on-screen back button
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()

        // Workstation mode requires an on-screen back button
        composeTestRule.runOnIdle {
            mode = EngagementMode.Workstation()
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        // Cabin mode requires an on-screen back button
        composeTestRule.runOnIdle {
            mode = EngagementMode.Cabin(hasPointingDevice = true)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()

        // Compact mode without pointing device hides back button; with pointing device shows it
        composeTestRule.runOnIdle {
            mode = EngagementMode.Compact(isBackButtonRequired = false)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").assertDoesNotExist()

        composeTestRule.runOnIdle {
            mode = EngagementMode.Compact(isBackButtonRequired = true)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }
}
