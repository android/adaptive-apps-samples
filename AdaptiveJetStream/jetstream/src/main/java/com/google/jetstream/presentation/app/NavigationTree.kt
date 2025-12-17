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

package com.google.jetstream.presentation.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.presentation.screens.Screens
import com.google.jetstream.presentation.screens.categories.CategoriesScreen
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.favourites.FavouritesScreen
import com.google.jetstream.presentation.screens.home.HomeScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.movies.MoviesScreen
import com.google.jetstream.presentation.screens.profile.ProfileScreen
import com.google.jetstream.presentation.screens.search.SearchScreen
import com.google.jetstream.presentation.screens.shows.ShowsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen

@Composable
fun NavigationTree(
    navigator: Navigator,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    onScroll: (Boolean) -> Unit = {}
) {
    val entryProvider: (NavKey) -> NavEntry<NavKey> = entryProvider {
        entry<Screens.CategoryMovieList> {
            CategoryMovieListScreen(
                onBackPressed = navigator::goBack,
                onMovieSelected = { movie -> navigator.navigate(Screens.MovieDetails(movie.id)) }
            )
        }
        entry<Screens.MovieDetails> {key ->
            MovieDetailsScreen(
                goToMoviePlayer = { navigator.navigate(Screens.VideoPlayer(key.movieId)) },
                refreshScreenWithNewMovie = { movie ->
                    navigator.navigate(Screens.MovieDetails(movie.id))
                },
                onBackPressed = navigator::goBack,
            )
        }
        entry<Screens.VideoPlayer> {
            VideoPlayerScreen(
                onBackPressed = navigator::goBack,
            )
        }
        entry<Screens.Profile> {
            ProfileScreen()
        }
        entry<Screens.Home> {
            HomeScreen(
                onMovieClick = { movie -> navigator.navigate(Screens.MovieDetails(movie.id)) },
                goToVideoPlayer = { movie -> navigator.navigate(Screens.VideoPlayer(movie.id)) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        entry<Screens.Categories> {
            CategoriesScreen(
                onCategoryClick = { categoryId ->
                    navigator.navigate(Screens.CategoryMovieList(categoryId))
                },
                onScroll = onScroll,
            )
        }
        entry<Screens.Movies> {
            MoviesScreen(
                onMovieClick = { movie -> navigator.navigate(Screens.MovieDetails(movie.id)) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        entry<Screens.Shows> {
            ShowsScreen(
                onTVShowClick = { movie -> navigator.navigate(Screens.MovieDetails(movie.id)) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        entry<Screens.Favourites> {
            FavouritesScreen(
                onMovieClick = { movieId -> navigator.navigate(Screens.MovieDetails(movieId)) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        entry<Screens.Search> {
            SearchScreen(
                onMovieClick = { movie -> navigator.navigate(Screens.MovieDetails(movie.id)) },
                onScroll = onScroll,
            )
        }
    }

    NavDisplay(
        entries = navigator.state.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        sceneStrategy = remember { DialogSceneStrategy() },
        modifier = modifier
    )
}
