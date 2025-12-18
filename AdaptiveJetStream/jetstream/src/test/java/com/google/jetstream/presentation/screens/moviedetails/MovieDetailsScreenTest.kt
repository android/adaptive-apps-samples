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

package com.google.jetstream.presentation.screens.moviedetails

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MovieDetailsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMovieDetails = MovieDetails(
        id = "1",
        sources = emptyMap(),
        subtitleUri = "",
        posterUri = "",
        name = "Test Movie Title",
        description = "Detailed description",
        pgRating = "PG-13",
        releaseDate = "2023",
        categories = listOf("Action"),
        duration = "2h",
        director = "Director",
        screenplay = "Screenplay",
        music = "Music",
        castAndCrew = emptyList(),
        status = "Released",
        originalLanguage = "English",
        budget = "$10M",
        revenue = "$50M",
        similarMovies = emptyList(),
        reviewsAndRatings = emptyList()
    )

    @Test
    fun movieDetails_displaysTitleAndDescription() {
        composeTestRule.setContent {
            JetStreamTheme {
                Details(
                    movieDetails = testMovieDetails,
                    goToMoviePlayer = {},
                    onBackPressed = {},
                    refreshScreenWithNewMovie = {}
                )
            }
        }

        composeTestRule.onNodeWithText(testMovieDetails.name).assertIsDisplayed()
        composeTestRule.onNodeWithText(testMovieDetails.description).assertIsDisplayed()
    }
}
