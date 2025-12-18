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

import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test

class CategoryMovieListScreenAdaptiveTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMovie = Movie(
        id = "1",
        sources = emptyMap(),
        subtitleUri = null,
        posterUri = "",
        name = "Movie Name",
        description = "Movie Description"
    )
    private val testMovieList = List(5) { testMovie.copy(id = it.toString(), name = "Movie $it") }

    private val testCategoryDetails = MovieCategoryDetails(
        id = "1",
        name = "Action Movies",
        movies = testMovieList
    )

    @Test
    fun categoryMovieList_displaysCategoryName_inLargeScreen() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(1280.dp, 800.dp))
            ) {
                JetStreamTheme {
                    CategoryDetails(
                        categoryDetails = testCategoryDetails,
                        onBackPressed = {},
                        onMovieSelected = {}
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(testCategoryDetails.name).assertIsDisplayed()
    }

    @Test
    fun categoryMovieList_displaysAllMovies() {
        composeTestRule.setContent {
            JetStreamTheme {
                CategoryDetails(
                    categoryDetails = testCategoryDetails,
                    onBackPressed = {},
                    onMovieSelected = {}
                )
            }
        }

        // Each MovieCard is clickable.
        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(testMovieList.size)
    }

    @Test
    fun categoryMovieList_stateRestoration() {
        val restorationTester = StateRestorationTester(composeTestRule)

        restorationTester.setContent {
            JetStreamTheme {
                CategoryDetails(
                    categoryDetails = testCategoryDetails,
                    onBackPressed = {},
                    onMovieSelected = {}
                )
            }
        }

        // Trigger a recreation
        restorationTester.emulateSavedInstanceStateRestore()

        // Verify that category name is still displayed
        composeTestRule.onNodeWithText(testCategoryDetails.name).assertIsDisplayed()
    }
}
