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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel
    @Inject
    constructor(
        private val movieRepository: MovieRepository,
    ) : ViewModel() {
        private var searchText = TextFieldValue("")
        private var searchJob: Job? = null

        private val _searchState =
            MutableStateFlow<SearchState>(SearchState.Ready(TextFieldValue(""), emptyList()))
        val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

        fun query() {
            val currentText = searchText
            searchJob?.cancel()
            if (currentText.text.isBlank()) {
                _searchState.value = SearchState.Ready(currentText, emptyList())
                return
            }
            searchJob =
                viewModelScope.launch {
                    _searchState.value = SearchState.Searching
                    val result = movieRepository.searchMovies(query = currentText.text)
                    _searchState.value = SearchState.Ready(currentText, result)
                }
        }

        fun updateSearchText(textFieldValue: TextFieldValue) {
            searchJob?.cancel()
            searchText = textFieldValue
            _searchState.value = SearchState.Ready(textFieldValue, emptyList())
        }
    }

sealed interface SearchState {
    data object Searching : SearchState

    data class Ready(val textFieldValue: TextFieldValue, val movieList: List<Movie>) : SearchState
}
