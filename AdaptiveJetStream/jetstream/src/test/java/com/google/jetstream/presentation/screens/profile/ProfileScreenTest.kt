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

package com.google.jetstream.presentation.screens.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.presentation.screens.profile.compoents.ProfileScreenLayoutType
import com.google.jetstream.presentation.screens.profile.compoents.ProfileScreens
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun largeProfileScreen_displaysNavigationItems() {
        composeTestRule.setContent {
            JetStreamTheme {
                ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.FullyExpanded)
            }
        }

        ProfileScreens.entries.forEach { screen ->
            composeTestRule.onNodeWithText(screen.tabTitle).assertIsDisplayed()
        }
    }

    @Test
    fun largeProfileScreen_navigatesToSections() {
        composeTestRule.setContent {
            JetStreamTheme {
                ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.FullyExpanded)
            }
        }

        // Initially on Accounts section
        composeTestRule.onNodeWithText("Switch accounts").assertIsDisplayed()

        // Navigate to About
        composeTestRule.onNodeWithText(ProfileScreens.About.tabTitle).performClick()
        composeTestRule.onNodeWithText("About JetStream").assertIsDisplayed()

        // Navigate to Help and Support
        composeTestRule.onNodeWithText(ProfileScreens.HelpAndSupport.tabTitle).performClick()
        composeTestRule.onNodeWithText("FAQ's").assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp")
    fun compactProfileScreen_displaysList() {
        composeTestRule.setContent {
            JetStreamTheme {
                ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.ListDetail)
            }
        }

        ProfileScreens.entries.forEach { screen ->
            composeTestRule.onNodeWithText(screen.tabTitle).assertIsDisplayed()
        }
    }

    @Test
    @Config(qualifiers = "w360dp-h640dp")
    fun compactProfileScreen_navigatesToDetail() {
        composeTestRule.setContent {
            JetStreamTheme {
                ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.ListDetail)
            }
        }

        // Click on About
        composeTestRule.onNodeWithText(ProfileScreens.About.tabTitle).performClick()

        // Detail pane should show "About JetStream"
        composeTestRule.onNodeWithText("About JetStream").assertIsDisplayed()
    }
}
