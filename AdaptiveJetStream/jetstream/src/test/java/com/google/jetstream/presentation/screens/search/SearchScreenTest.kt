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

package com.google.jetstream.presentation.screens.search

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.input.TextFieldValue
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.presentation.theme.JetStreamTheme
import com.google.jetstream.util.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testMovie = Movie(
        id = "1",
        sources = emptyMap(),
        subtitleUri = null,
        posterUri = "",
        name = "Movie Name",
        description = "Movie Description"
    )

    @Test
    fun searchResult_displaysPlaceholder() {
        composeTestRule.setContent {
            JetStreamTheme {
                SearchResult(
                    searchText = TextFieldValue(""),
                    movieList = emptyList(),
                    searchMovies = {},
                    updateSearchText = {},
                    onMovieClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Search for movies, shows and more…")
            .assertIsDisplayed()
    }

    @Test
    fun searchResult_displaysMovieList() {
        val movies = listOf(testMovie.copy(name = "Search Result 1"))
        composeTestRule.setContent {
            JetStreamTheme {
                SearchResult(
                    searchText = TextFieldValue("test"),
                    movieList = movies,
                    searchMovies = {},
                    updateSearchText = {},
                    onMovieClick = {}
                )
            }
        }

        composeTestRule
            .onNodeWithText("Search Result 1")
            .assertIsDisplayed()
    }

    @Test
    fun searchScreen_whenReady_displaysSearchResult() {
        val repository = FakeMovieRepository()
        val viewModel = SearchScreenViewModel(repository)

        composeTestRule.setContent {
            JetStreamTheme {
                SearchScreen(
                    onMovieClick = {},
                    onScroll = {},
                    searchScreenViewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText("Search for movies, shows and more…")
            .assertIsDisplayed()
    }
}
