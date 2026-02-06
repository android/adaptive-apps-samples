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

package com.google.jetstream.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import com.google.jetstream.presentation.app.AppState
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.AdaptiveAppNavigationItems
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.NavigationSuiteScaffoldLayout
import com.google.jetstream.presentation.app.withNavigationSuiteScaffold.TopAppBar
import com.google.jetstream.presentation.app.withTopBarNavigation.TopBarWithNavigationLayout
import com.google.jetstream.presentation.components.AdaptivePreview
import com.google.jetstream.presentation.components.JetStreamPreview
import com.google.jetstream.presentation.components.mockCategoryScreenState
import com.google.jetstream.presentation.screens.categories.CategoryDetails
import com.google.jetstream.presentation.screens.favourites.FavouriteScreenViewModel
import com.google.jetstream.presentation.screens.favourites.FilterList
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsList
import com.google.jetstream.presentation.screens.search.SearchResult
import com.google.jetstream.presentation.screens.categories.Catalog as CategoriesCatalog
import com.google.jetstream.presentation.screens.favourites.Catalog as FavouritesCatalog
import com.google.jetstream.presentation.screens.home.Catalog as HomeCatalog
import com.google.jetstream.presentation.screens.movies.Catalog as MoviesCatalog
import com.google.jetstream.presentation.screens.shows.Catalog as ShowsCatalog

@PreviewTest
@AdaptivePreview
@Composable
fun HomeScreenScreenshot() {
    JetStreamPreview {
        Surface {
            HomeCatalog(
                featuredMovies = TestMovieList,
                trendingMovies = TestMovieList,
                top10Movies = TestMovieList,
                nowPlayingMovies = TestMovieList,
                onMovieClick = { _ -> },
                onScroll = { _ -> },
                goToVideoPlayer = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun MovieDetailsScreenScreenshot() {
    JetStreamPreview {
        Surface {
            MovieDetailsList(
                movieDetails = TestMovieDetails,
                goToMoviePlayer = { _ -> },
                refreshScreenWithNewMovie = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun CategoryMovieListScreenScreenshot() {
    JetStreamPreview {
        Surface {
            CategoryDetails(
                categoryDetails = TestCategoryDetails,
                onBackPressed = {},
                onMovieSelected = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun ShowsScreenScreenshot() {
    JetStreamPreview {
        Surface {
            ShowsCatalog(
                tvShowList = TestMovieList,
                bingeWatchDramaList = TestMovieList,
                onTVShowClick = { _ -> },
                onScroll = { _ -> },
                isTopBarVisible = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun MoviesScreenScreenshot() {
    JetStreamPreview {
        Surface {
            MoviesCatalog(
                movieList = TestMovieList,
                popularFilmsThisWeek = TestMovieList,
                onMovieClick = { _ -> },
                onScroll = { _ -> },
                isTopBarVisible = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun CategoriesScreenScreenshot() {
    JetStreamPreview {
        Surface {
            CategoriesCatalog(
                movieCategories = mockCategoryScreenState.categoryList,
                onCategoryClick = { _ -> },
                onScroll = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun FavouritesScreenScreenshot() {
    JetStreamPreview {
        Surface {
            FavouritesCatalog(
                favouriteMovieList = TestMovieList,
                filterList = FavouriteScreenViewModel.filterList,
                selectedFilterList = FilterList(),
                onMovieClick = { _ -> },
                onScroll = { _ -> },
                onSelectedFilterListUpdated = { _ -> },
                isTopBarVisible = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun SearchScreenScreenshot() {
    JetStreamPreview {
        Surface {
            SearchResult(
                searchText = TextFieldValue(""),
                movieList = TestMovieList,
                searchMovies = {},
                updateSearchText = { _ -> },
                onMovieClick = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun NavigationSuiteScaffoldLayoutPreview() {
    val appState = AppState()

    NavigationSuiteScaffoldLayout(
        isNavigationVisible = true,
        navigationItems = {
            AdaptiveAppNavigationItems(
                currentScreen = Screens.Home,
                screens = Screens.entries.filter { it.isMainNavigation },
                onSelectScreen = {}
            )
        },
        content = { padding ->
            Text("Preview content", modifier = Modifier.padding(padding))
        },
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 0.dp
                    ),
                selectedScreen = appState.selectedScreen,
                showScreen = { },
            )
        }
    )
}


@PreviewTest
@AdaptivePreview
@Composable
fun TopBarWithNavigationLayoutPreview() {
    val appState = AppState()
    TopBarWithNavigationLayout(
        selectedScreen = appState.selectedScreen,
        isNavigationVisible = appState.isNavigationVisible,
        isTopBarVisible = appState.isNavigationVisible && appState.isTopBarVisible,
        isTopBarFocussed = appState.isTopBarFocused,
        onTopBarFocusChanged = { hasFocus ->
            appState.updateTopBarFocusState(hasFocus)
        },
        onTopBarVisible = { appState.showTopBar() },
        onActivityBackPressed = { },
        onShowScreen = {},
    ) {
        Text("Preview content")
    }
}
