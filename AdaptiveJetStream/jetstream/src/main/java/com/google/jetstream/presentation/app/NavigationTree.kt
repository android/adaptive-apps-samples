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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.screens.Screens.Categories
import com.google.jetstream.presentation.screens.Screens.CategoryMovieList
import com.google.jetstream.presentation.screens.Screens.Favourites
import com.google.jetstream.presentation.screens.Screens.Home
import com.google.jetstream.presentation.screens.Screens.MovieDetails
import com.google.jetstream.presentation.screens.Screens.Movies
import com.google.jetstream.presentation.screens.Screens.Profile
import com.google.jetstream.presentation.screens.Screens.Search
import com.google.jetstream.presentation.screens.Screens.Shows
import com.google.jetstream.presentation.screens.Screens.VideoPlayer
import com.google.jetstream.presentation.screens.categories.CategoriesScreen
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.categories.categoryMovieListScreenArguments
import com.google.jetstream.presentation.screens.favourites.FavouritesScreen
import com.google.jetstream.presentation.screens.home.HomeScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.moviedetails.movieDetailsScreenArguments
import com.google.jetstream.presentation.screens.movies.MoviesScreen
import com.google.jetstream.presentation.screens.profile.ProfileScreen
import com.google.jetstream.presentation.screens.search.SearchScreen
import com.google.jetstream.presentation.screens.shows.ShowsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen

@Composable
fun NavigationTree(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isTopBarVisible: Boolean = true,
    onScroll: (Boolean) -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Home(),
        modifier = modifier,
    ) {
        composable(
            route = CategoryMovieList(),
            arguments = categoryMovieListScreenArguments
        ) {
            CategoryMovieListScreen(
                onBackPressed = navController::navigateUp,
                onMovieSelected = { movie -> navController.openMovieDetailScreen(movie) }
            )
        }
        composable(
            route = MovieDetails(),
            arguments = movieDetailsScreenArguments
        ) {
            MovieDetailsScreen(
                goToMoviePlayer = { movieDetails ->
                    navController.openVideoPlayer(movieDetails.id)
                },
                refreshScreenWithNewMovie = { movie ->
                    navController.navigate(
                        MovieDetails.withArgs(movie.id)
                    ) {
                        popUpTo(MovieDetails()) {
                            inclusive = true
                        }
                    }
                },
                onBackPressed = navController::navigateUp,
            )
        }
        composable(route = VideoPlayer()) {
            VideoPlayerScreen(
                onBackPressed = navController::navigateUp,
            )
        }
        composable(Profile()) {
            ProfileScreen()
        }
        composable(Home()) {
            HomeScreen(
                onMovieClick = { movie -> navController.openMovieDetailsScreen(movie.id) },
                goToVideoPlayer = { movie: Movie -> navController.openVideoPlayer(movie.id) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        composable(Categories()) {
            CategoriesScreen(
                onCategoryClick = navController.openCategoryMovieList(),
                onScroll = onScroll,
            )
        }
        composable(Movies()) {
            MoviesScreen(
                onMovieClick = { movie -> navController.openMovieDetailScreen(movie) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        composable(Shows()) {
            ShowsScreen(
                onTVShowClick = { movie -> navController.openMovieDetailScreen(movie) },
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        composable(Favourites()) {
            FavouritesScreen(
                onMovieClick = navController::openMovieDetailsScreen,
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible
            )
        }
        composable(Search()) {
            SearchScreen(
                onMovieClick = { movie -> navController.openMovieDetailsScreen(movie.id) },
                onScroll = onScroll,
            )
        }
    }
}
