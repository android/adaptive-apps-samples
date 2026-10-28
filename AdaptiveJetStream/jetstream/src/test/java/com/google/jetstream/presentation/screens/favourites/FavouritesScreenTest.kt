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

package com.google.jetstream.presentation.screens.favourites

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class FavouritesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMovies =
        List(29) { index ->
            Movie(
                id = "movie-$index",
                sources = emptyMap(),
                subtitleUri = null,
                posterUri = "",
                name = "Favourite Movie $index",
                description = "Description $index",
            )
        }

    @Test
    fun favouritesScreen_displaysAllFilterChips() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    favouriteMovieList = testMovies,
                    filterList = FavouriteScreenViewModel.filterList,
                    selectedFilterList = FilterList(),
                    onMovieClick = {},
                    onScroll = {},
                    onSelectedFilterListUpdated = {},
                    isTopBarVisible = true,
                )
            }
        }

        composeTestRule.onNodeWithText("Movies").assertIsDisplayed()
        composeTestRule.onNodeWithText("TV Shows").assertIsDisplayed()
        composeTestRule.onNodeWithText("Added Last Week").assertIsDisplayed()
        composeTestRule.onNodeWithText("Available in 4K").assertIsDisplayed()
    }

    @Test
    fun favouritesScreen_clickingFilterChip_updatesSelectedFilterList() {
        val repository = FakeMovieRepository()
        repository.setMovies(testMovies)
        val viewModel = FavouriteScreenViewModel(repository)

        composeTestRule.setContent {
            JetStreamTheme {
                FavouritesScreen(
                    onMovieClick = {},
                    onScroll = {},
                    isTopBarVisible = true,
                    favouriteScreenViewModel = viewModel,
                )
            }
        }

        composeTestRule.onNodeWithText("TV Shows").performClick()
        composeTestRule.waitForIdle()

        val readyState = viewModel.uiState.value as FavouriteScreenUiState.Ready
        assertTrue(readyState.selectedFilterList.items.contains(FilterCondition.TvShows))
        assertEquals(FilterCondition.TvShows.idList.size, readyState.favouriteMovieList.size)
    }

    @Test
    fun favouritesScreen_clickingMovieCard_invokesOnMovieClick() {
        var clickedMovieId: String? = null

        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    favouriteMovieList = testMovies.take(2),
                    filterList = FavouriteScreenViewModel.filterList,
                    selectedFilterList = FilterList(),
                    onMovieClick = { clickedMovieId = it },
                    onScroll = {},
                    onSelectedFilterListUpdated = {},
                    isTopBarVisible = true,
                )
            }
        }

        composeTestRule
            .onNodeWithContentDescription(
                StringConstants.Composable.ContentDescription.moviePoster("Favourite Movie 0"),
            )
            .performClick()

        assertEquals("movie-0", clickedMovieId)
    }
}
