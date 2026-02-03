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

package com.google.jetstream.presentation.screens.shows

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.theme.JetStreamTheme
import com.google.jetstream.util.MainDispatcherRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class ShowsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val testMovie = Movie(
        id = "1",
        sources = emptyMap(),
        subtitleUri = null,
        posterUri = "",
        name = "Show Name",
        description = "Show Description"
    )
    private val tvShowList = List(5) { testMovie.copy(id = "tv_$it", name = "TV Show $it") }
    private val bingeWatchDramaList = List(5) { testMovie.copy(id = "drama_$it", name = "Drama $it") }

    @Test
    fun showsScreen_displaysBingeWatchDramasSection() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    tvShowList = tvShowList,
                    bingeWatchDramaList = bingeWatchDramaList,
                    onTVShowClick = { _ -> },
                    onScroll = { _ -> },
                    isTopBarVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.BingeWatchDramasTitle)
            .assertIsDisplayed()
    }

    @Test
    fun showsScreen_displaysShowsInBingeWatchSection() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    tvShowList = tvShowList,
                    bingeWatchDramaList = bingeWatchDramaList,
                    onTVShowClick = { _ -> },
                    onScroll = { _ -> },
                    isTopBarVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithText("Drama 0")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Drama 4")
            .assertIsDisplayed()
    }

    @Test
    fun showsScreen_displaysShowsInTvShowSection() {
        composeTestRule.setContent {
            JetStreamTheme {
                Catalog(
                    tvShowList = tvShowList,
                    bingeWatchDramaList = bingeWatchDramaList,
                    onTVShowClick = { _ -> },
                    onScroll = { _ -> },
                    isTopBarVisible = true
                )
            }
        }

        composeTestRule
            .onNodeWithText("TV Show 0")
            .assertIsDisplayed()
    }

    @Test
    fun showsScreen_whenReady_displaysCatalog() {
        val repository = FakeMovieRepository()
        repository.setMovies(tvShowList)
        val viewModel = ShowScreenViewModel(repository)

        composeTestRule.setContent {
            JetStreamTheme {
                ShowsScreen(
                    onTVShowClick = {},
                    onScroll = {},
                    isTopBarVisible = true,
                    showScreenViewModel = viewModel
                )
            }
        }

        composeTestRule
            .onNodeWithText(StringConstants.Composable.BingeWatchDramasTitle)
            .assertIsDisplayed()
    }

    @Test
    fun loading_isDisplayed() {
        composeTestRule.setContent {
            JetStreamTheme {
                Loading()
            }
        }
        composeTestRule.onRoot().assertIsDisplayed()
    }
}
