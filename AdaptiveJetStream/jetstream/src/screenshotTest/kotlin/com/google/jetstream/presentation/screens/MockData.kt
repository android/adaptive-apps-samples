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

import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.MovieCategoryDetails

val TestMovie = Movie(
    id = "1",
    sources = emptyMap(),
    subtitleUri = null,
    posterUri = "",
    name = "Movie Name",
    description = "Movie Description"
)

val TestMovieList = List(10) { TestMovie.copy(id = it.toString(), name = "Movie $it") }

val TestMovieDetails = MovieDetails(
    id = "1",
    sources = emptyMap(),
    subtitleUri = "",
    posterUri = "",
    name = "Movie Name",
    description = "This is a detailed description of the movie. It is quite long to test how it looks in the UI.",
    pgRating = "PG-13",
    releaseDate = "2023-10-27",
    categories = listOf("Action", "Drama"),
    duration = "2h 15m",
    director = "Director Name",
    screenplay = "Screenplay Writer",
    music = "Composer Name",
    castAndCrew = emptyList(),
    status = "Released",
    originalLanguage = "English",
    budget = "$100M",
    revenue = "$500M",
    similarMovies = TestMovieList,
    reviewsAndRatings = emptyList()
)

val TestCategoryDetails = MovieCategoryDetails(
    id = "action",
    name = "Action Movies",
    movies = TestMovieList
)
