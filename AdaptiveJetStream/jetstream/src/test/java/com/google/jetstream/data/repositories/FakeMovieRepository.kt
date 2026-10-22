/*
 * Copyright 2023 Google LLC
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

package com.google.jetstream.data.repositories

import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.data.entities.MovieCategoryDetails
import com.google.jetstream.data.entities.MovieDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMovieRepository : MovieRepository {
    private val movieFlow = MutableStateFlow<List<Movie>>(emptyList())
    private val categoryFlow = MutableStateFlow<List<MovieCategory>>(emptyList())

    fun setMovies(movies: List<Movie>) {
        movieFlow.value = movies
    }

    override fun getFeaturedMovies(): Flow<List<Movie>> = movieFlow

    override fun getTrendingMovies(): Flow<List<Movie>> = movieFlow

    override fun getTop10Movies(): Flow<List<Movie>> = movieFlow

    override fun getNowPlayingMovies(): Flow<List<Movie>> = movieFlow

    override fun getMovieCategories(): Flow<List<MovieCategory>> = categoryFlow

    override suspend fun getMovieCategoryDetails(categoryId: String): MovieCategoryDetails {
        return MovieCategoryDetails(categoryId, "Category", emptyList())
    }

    override suspend fun getMovieDetails(movieId: String): MovieDetails {
        return MovieDetails(
            id = movieId,
            sources = emptyMap(),
            subtitleUri = "",
            posterUri = "",
            name = "Movie",
            description = "",
            pgRating = "",
            releaseDate = "",
            categories = emptyList(),
            duration = "",
            director = "",
            screenplay = "",
            music = "",
            castAndCrew = emptyList(),
            status = "",
            originalLanguage = "",
            budget = "",
            revenue = "",
            similarMovies = emptyList(),
            reviewsAndRatings = emptyList(),
        )
    }

    override suspend fun searchMovies(query: String): List<Movie> = emptyList()

    override fun getMoviesWithLongThumbnail(): Flow<List<Movie>> = movieFlow

    override fun getMovies(): Flow<List<Movie>> = movieFlow

    override fun getPopularFilmsThisWeek(): Flow<List<Movie>> = movieFlow

    override fun getTVShows(): Flow<List<Movie>> = movieFlow

    override fun getBingeWatchDramas(): Flow<List<Movie>> = movieFlow

    override fun getFavouriteMovies(): Flow<List<Movie>> = movieFlow
}
