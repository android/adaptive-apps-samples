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

package com.google.jetstream.presentation.screens.search

import androidx.compose.ui.text.input.TextFieldValue
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchScreenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val movieRepository = FakeMovieRepository()

    @Test
    fun searchState_initiallyReady() = runTest {
        val viewModel = SearchScreenViewModel(movieRepository)
        assertTrue(viewModel.searchState.value is SearchState.Ready)
        assertEquals("", (viewModel.searchState.value as SearchState.Ready).textFieldValue.text)
    }

    @Test
    fun updateSearchText_updatesState() = runTest {
        val viewModel = SearchScreenViewModel(movieRepository)
        val newText = TextFieldValue("Action")
        
        // Start collecting to trigger WhileSubscribed
        val collectJob = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.searchState.collect()
        }

        viewModel.updateSearchText(newText)
        
        val state = viewModel.searchState.first { it is SearchState.Ready && it.textFieldValue.text == "Action" }
        assertEquals("Action", (state as SearchState.Ready).textFieldValue.text)
        
        collectJob.cancel()
    }
}
