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

package com.google.jetstream.presentation.screens.profile.section

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class ProfileSectionsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun subtitlesSection_clickingSwitch_togglesSubtitlesCallback() {
        var isChecked by mutableStateOf(false)

        composeTestRule.setContent {
            JetStreamTheme {
                SubtitlesSection(
                    isSubtitlesChecked = isChecked,
                    onSubtitleCheckChange = { isChecked = it },
                )
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.SubtitlesSectionSubtitlesItem)
            .performClick()

        assertTrue(isChecked)

        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.SubtitlesSectionSubtitlesItem)
            .performClick()

        assertFalse(isChecked)
    }

    @Test
    fun languageSection_selectingLanguage_invokesOnSelectedIndexChange() {
        var selectedIndex by mutableIntStateOf(0)

        composeTestRule.setContent {
            JetStreamTheme {
                LanguageSection(
                    selectedIndex = selectedIndex,
                    onSelectedIndexChange = { selectedIndex = it },
                )
            }
        }

        val targetLanguage = StringConstants.Composable.Placeholders.LanguageSectionItems[2]
        composeTestRule.onNodeWithText(targetLanguage).performClick()

        assertEquals(2, selectedIndex)
    }

    @Test
    fun accountsSection_clickingDeleteAccount_opensAndDismissesDeleteDialog() {
        composeTestRule.setContent {
            JetStreamTheme {
                AccountsSection()
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.AccountsSelectionDeleteAccountTitle)
            .performClick()

        composeTestRule
            .onNodeWithText("No, keep it")
            .assertIsDisplayed()
            .performClick()

        composeTestRule.onNodeWithText("No, keep it").assertDoesNotExist()
    }
}
