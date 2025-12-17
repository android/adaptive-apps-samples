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

import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.screens.Screens

internal fun Navigator.openMovieDetailScreen(movie: Movie) =
    openMovieDetailsScreen(movieId = movie.id)

internal fun Navigator.openMovieDetailsScreen(movieId: String) {
    navigate(
        Screens.MovieDetails(movieId)
    )
}

internal fun Navigator.openVideoPlayer(movieId: String) {
    navigate(Screens.VideoPlayer(movieId))
}

internal fun Navigator.openCategoryMovieList() = { categoryId: String ->
    navigate(
        Screens.CategoryMovieList(categoryId)
    )
}

internal fun updateTopBarVisibility(appState: AppState, updatedVisibility: Boolean) {
    if (updatedVisibility) {
        appState.showTopBar()
    } else {
        appState.hideTopBar()
    }
}
