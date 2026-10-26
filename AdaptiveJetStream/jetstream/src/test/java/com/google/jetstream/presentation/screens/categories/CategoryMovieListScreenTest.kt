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

package com.google.jetstream.presentation.screens.categories

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryMovieListScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategoryDetails =
        MovieCategoryDetails(
            id = "1",
            name = "Action Movies",
            movies = emptyList(),
        )

    @Test
    fun categoryMovieList_displaysCategoryName() {
        composeTestRule.setContent {
            JetStreamTheme {
                CategoryDetails(
                    categoryDetails = testCategoryDetails,
                    onBackPressed = {},
                    onMovieSelected = {},
                )
            }
        }

        composeTestRule.onNodeWithText(testCategoryDetails.name).assertIsDisplayed()
    }

    @Test
    fun categoryMovieList_backButtonClick_invokesOnBackPressed() {
        var backPressed = false
        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(
                    LocalEngagementMode provides EngagementMode.Compact(isBackButtonRequired = true),
                ) {
                    CategoryDetails(
                        categoryDetails = testCategoryDetails,
                        onBackPressed = { backPressed = true },
                        onMovieSelected = {},
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Back")
            .performClick()

        assertTrue(backPressed)
    }

    @Test
    fun categoryMovieList_systemBack_invokesOnBackPressed() {
        var backPressed = false
        var dispatcherOwner: androidx.activity.OnBackPressedDispatcherOwner? = null
        composeTestRule.setContent {
            dispatcherOwner = LocalOnBackPressedDispatcherOwner.current
            JetStreamTheme {
                CategoryDetails(
                    categoryDetails = testCategoryDetails,
                    onBackPressed = { backPressed = true },
                    onMovieSelected = {},
                )
            }
        }

        composeTestRule.runOnIdle {
            dispatcherOwner?.onBackPressedDispatcher?.onBackPressed()
        }

        assertTrue(backPressed)
    }
}
