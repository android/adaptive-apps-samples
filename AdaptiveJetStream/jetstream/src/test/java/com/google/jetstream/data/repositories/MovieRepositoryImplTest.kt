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

import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.util.FakeAssetReader
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MovieRepositoryImplTest {

    private lateinit var fakeAssetReader: FakeAssetReader
    private lateinit var repository: MovieRepositoryImpl

    @Before
    fun setUp() {
        fakeAssetReader = FakeAssetReader()
        val movieDataSource = MovieDataSource(fakeAssetReader)
        val tvDataSource = TvDataSource(fakeAssetReader)
        val movieCastDataSource = MovieCastDataSource(fakeAssetReader)
        val movieCategoryDataSource = MovieCategoryDataSource(fakeAssetReader)
        
        repository = MovieRepositoryImpl(
            movieDataSource,
            tvDataSource,
            movieCastDataSource,
            movieCategoryDataSource
        )
        
        // Setup minimal valid JSONs
        fakeAssetReader.setResponse(StringConstants.Assets.Top250Movies, "[]")
        fakeAssetReader.setResponse(StringConstants.Assets.MostPopularMovies, "[]")
        fakeAssetReader.setResponse(StringConstants.Assets.InTheaters, "[]")
        fakeAssetReader.setResponse(StringConstants.Assets.MostPopularTVShows, "[]")
        fakeAssetReader.setResponse(StringConstants.Assets.MovieCast, "[]")
        fakeAssetReader.setResponse(StringConstants.Assets.MovieCategories, "[]")
    }

    @Test
    fun getMovies_initiallyEmpty() = runTest {
        val movies = repository.getMovies().first()
        assertEquals(0, movies.size)
    }

    @Test
    fun searchMovies_returnsFilteredResults() = runTest {
        val json = """
            [
              {
                "id": "1",
                "sources": [],
                "subtitleUri": "",
                "rank": 1,
                "rankUpDown": "",
                "title": "Batman",
                "fullTitle": "Batman",
                "year": 2021,
                "releaseDate": "",
                "image_16_9": "",
                "image_2_3": "",
                "runtimeMins": 120,
                "runtimeStr": "",
                "plot": "",
                "contentRating": "",
                "rating": 8.0,
                "ratingCount": 1000,
                "metaCriticRating": 80,
                "genres": "",
                "directors": "",
                "stars": ""
              },
              {
                "id": "2",
                "sources": [],
                "subtitleUri": "",
                "rank": 2,
                "rankUpDown": "",
                "title": "Superman",
                "fullTitle": "Superman",
                "year": 2021,
                "releaseDate": "",
                "image_16_9": "",
                "image_2_3": "",
                "runtimeMins": 120,
                "runtimeStr": "",
                "plot": "",
                "contentRating": "",
                "rating": 8.0,
                "ratingCount": 1000,
                "metaCriticRating": 80,
                "genres": "",
                "directors": "",
                "stars": ""
              }
            ]
        """.trimIndent()
        fakeAssetReader.setResponse(StringConstants.Assets.Top250Movies, json)

        val results = repository.searchMovies("Bat")
        assertEquals(1, results.size)
        assertEquals("Batman", results[0].name)
    }

    @Test
    fun getMovieDetails_returnsMovieDetails() = runTest {
        val movieItem = """
              {
                "id": "123",
                "sources": [],
                "subtitleUri": "",
                "rank": 1,
                "rankUpDown": "",
                "title": "Test Movie",
                "fullTitle": "Test Movie",
                "year": 2021,
                "releaseDate": "",
                "image_16_9": "",
                "image_2_3": "",
                "runtimeMins": 120,
                "runtimeStr": "",
                "plot": "Test Plot",
                "contentRating": "",
                "rating": 8.0,
                "ratingCount": 1000,
                "metaCriticRating": 80,
                "genres": "",
                "directors": "",
                "stars": ""
              }
        """.trimIndent()
        // Need at least 2 movies for getMovieDetails to succeed (shuffled subList(0, 2))
        val moviesJson = "[$movieItem, $movieItem]"
        fakeAssetReader.setResponse(StringConstants.Assets.Top250Movies, moviesJson)
        fakeAssetReader.setResponse(StringConstants.Assets.MovieCast, "[]")

        val details = repository.getMovieDetails("123")
        assertEquals("123", details.id)
        assertEquals("Test Movie", details.name)
        assertEquals("Test Plot", details.description)
    }
}
