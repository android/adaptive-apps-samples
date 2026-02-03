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

package com.google.jetstream.presentation.screens.home

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
class HomeScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val movieRepository = FakeMovieRepository()

    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = HomeScreenViewModel(movieRepository)
        assertEquals(HomeScreenUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun uiState_whenDataLoaded_isReady() = runTest {
        val viewModel = HomeScreenViewModel(movieRepository)
        
        movieRepository.setMovies(emptyList())
        
        val state = viewModel.uiState.first { it is HomeScreenUiState.Ready }
        assertTrue(state is HomeScreenUiState.Ready)
    }
}
