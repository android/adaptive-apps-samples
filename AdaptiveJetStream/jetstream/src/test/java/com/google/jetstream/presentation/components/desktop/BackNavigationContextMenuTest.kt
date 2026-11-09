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

package com.google.jetstream.presentation.components.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.MouseButton
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performMouseInput
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
class BackNavigationContextMenuTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun secondaryMouseClick_opensBackContextMenuAndInvokesCallback() {
        var backPressed = false

        composeTestRule.setContent {
            JetStreamTheme {
                BackNavigationContextMenu(
                    onBackPressed = { backPressed = true },
                    modifier =
                        Modifier
                            .size(200.dp)
                            .testTag("context_area"),
                ) {
                    Box(modifier = Modifier.size(200.dp)) {
                        Text("Content Area")
                    }
                }
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.ContextMenu.Back)
            .assertDoesNotExist()

        // Perform right-click (secondary mouse button click)
        composeTestRule.onNodeWithTag("context_area").performMouseInput {
            click(button = MouseButton.Secondary)
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.ContextMenu.Back)
            .assertIsDisplayed()
            .performClick()

        assertTrue(backPressed)
        composeTestRule
            .onNodeWithText(StringConstants.Composable.ContextMenu.Back)
            .assertDoesNotExist()
    }
}
