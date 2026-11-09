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

package com.google.jetstream.presentation.screens.moviedetails.components

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.requestFocus
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.MovieCast
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalFoundationStyleApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class CastAndCrewListFocusTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCast =
        listOf(
            MovieCast(
                id = "cast-1",
                characterName = "Hero",
                realName = "Actor One",
                avatarUrl = "",
            ),
            MovieCast(
                id = "cast-2",
                characterName = "Villain",
                realName = "Actor Two",
                avatarUrl = "",
            ),
        )

    @Test
    fun lastCastItem_focusDown_escapesRowToElementBelow() {
        var isBelowElementFocused = false
        var focusManager: FocusManager? = null

        composeTestRule.setContent {
            focusManager = LocalFocusManager.current
            JetStreamTheme {
                Column {
                    CastAndCrewList(castAndCrew = testCast)
                    Box(
                        modifier =
                            Modifier
                                .size(100.dp)
                                .onFocusChanged { isBelowElementFocused = it.isFocused }
                                .focusable(),
                    ) {
                        Text("Below Section")
                    }
                }
            }
        }

        // Focus the last item in CastAndCrewList
        composeTestRule.onNodeWithText("Actor Two").assertIsDisplayed().requestFocus()
        composeTestRule.waitForIdle()

        // Move focus down and verify focus is not trapped in the horizontal row
        composeTestRule.runOnIdle {
            focusManager?.moveFocus(FocusDirection.Down)
        }
        composeTestRule.waitForIdle()

        assertTrue(isBelowElementFocused)
    }
}
