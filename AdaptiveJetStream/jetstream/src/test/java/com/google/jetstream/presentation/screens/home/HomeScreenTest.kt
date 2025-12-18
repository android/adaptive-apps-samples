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

package com.google.jetstream.presentation.screens.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h2000dp")
class HomeScreenTest {

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

    @Test
    fun homeScreen_displaysTrendingSection() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    featuredMovies = testMovieList,
                    trendingMovies = testMovieList,
                    top10Movies = testMovieList,
                    nowPlayingMovies = testMovieList,
                    onMovieClick = {},
                    onScroll = {},
                    goToVideoPlayer = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.HomeScreenTrendingTitle)
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysNowPlayingSection() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    featuredMovies = testMovieList,
                    trendingMovies = testMovieList,
                    top10Movies = testMovieList,
                    nowPlayingMovies = testMovieList,
                    onMovieClick = {},
                    onScroll = {},
                    goToVideoPlayer = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.HomeScreenNowPlayingMoviesTitle)
            .assertIsDisplayed()
    }
}
