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

package com.google.jetstream.presentation.screens.moviedetails

import androidx.lifecycle.SavedStateHandle
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val movieRepository = FakeMovieRepository()

    @Test
    fun uiState_whenMovieIdMissing_isError() = runTest {
        val savedStateHandle = SavedStateHandle()
        val viewModel = MovieDetailsScreenViewModel(savedStateHandle, movieRepository)
        
        val state = viewModel.uiState.first { it !is MovieDetailsScreenUiState.Loading }
        assertEquals(MovieDetailsScreenUiState.Error, state)
    }

    @Test
    fun uiState_whenMovieIdPresent_isDone() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf(MovieDetailsScreen.MOVIE_ID_BUNDLE_KEY to "123"))
        val viewModel = MovieDetailsScreenViewModel(savedStateHandle, movieRepository)
        
        val state = viewModel.uiState.first { it is MovieDetailsScreenUiState.Done }
        assertTrue(state is MovieDetailsScreenUiState.Done)
        assertEquals("123", (state as MovieDetailsScreenUiState.Done).movieDetails.id)
    }
}
