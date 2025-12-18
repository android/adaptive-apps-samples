/*
 * Copyright 2024 Google LLC
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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VideoPlayerMediaTitleTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun defaultType_showsTitleAndSubtitle() {
        val title = "Test Movie"
        val secondary = "2023"
        val tertiary = "Action"

        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerMediaTitle(
                    title = title,
                    secondaryText = secondary,
                    tertiaryText = tertiary,
                    type = VideoPlayerMediaTitleType.DEFAULT
                )
            }
        }

        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText("$secondary • $tertiary").assertIsDisplayed()
    }

    @Test
    fun adType_showsAdBadge() {
        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerMediaTitle(
                    title = "Ad Title",
                    secondaryText = "Secondary",
                    tertiaryText = "Tertiary",
                    type = VideoPlayerMediaTitleType.AD
                )
            }
        }

        composeTestRule.onNodeWithText("Ad").assertIsDisplayed()
    }

    @Test
    fun liveType_showsLiveBadge() {
        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerMediaTitle(
                    title = "Live Title",
                    secondaryText = "Secondary",
                    tertiaryText = "Tertiary",
                    type = VideoPlayerMediaTitleType.LIVE
                )
            }
        }

        composeTestRule.onNodeWithText("Live").assertIsDisplayed()
    }
}
