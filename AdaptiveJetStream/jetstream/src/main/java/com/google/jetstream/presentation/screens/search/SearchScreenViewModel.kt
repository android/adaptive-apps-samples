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
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.data.repositories.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val searchCommandFlow = MutableSharedFlow<SearchCommand>()
    private var searchText = TextFieldValue("")

    private val internalSearchState = searchCommandFlow.map { command ->
        when (command) {
            is SearchCommand.StartSearch -> SearchState.Searching
            is SearchCommand.UpdateSearchText -> {
                searchText = command.textFieldValue
                SearchState.Ready(searchText, emptyList())
            }

            is SearchCommand.SearchDone -> {
                SearchState.Ready(searchText, command.movieList)
            }
        }
    }

    fun query() {
        viewModelScope.launch { postQuery(searchText) }
    }

    fun updateSearchText(textFieldValue: TextFieldValue) {
        viewModelScope.launch {
            searchCommandFlow.emit(SearchCommand.UpdateSearchText(textFieldValue))
        }
    }

    private suspend fun postQuery(textFieldValue: TextFieldValue) {
        searchCommandFlow.emit(SearchCommand.StartSearch)
        val result = movieRepository.searchMovies(query = textFieldValue.text)
        searchCommandFlow.emit(SearchCommand.SearchDone(result))
    }

    val searchState = internalSearchState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SearchState.Ready(TextFieldValue(""), emptyList())
    )
}

sealed interface SearchState {
    data object Searching : SearchState
    data class Ready(val textFieldValue: TextFieldValue, val movieList: MovieList) : SearchState
}

sealed interface SearchCommand {
    data object StartSearch : SearchCommand
    data class UpdateSearchText(val textFieldValue: TextFieldValue) : SearchCommand
    data class SearchDone(val movieList: MovieList) : SearchCommand
}
