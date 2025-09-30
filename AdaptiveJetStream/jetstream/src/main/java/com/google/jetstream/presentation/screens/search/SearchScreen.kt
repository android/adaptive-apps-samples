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

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.presentation.components.MoviesRow
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

@Composable
fun SearchScreen(
    onMovieClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    searchScreenViewModel: SearchScreenViewModel = hiltViewModel(),
) {
    val lazyColumnState = rememberLazyListState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            lazyColumnState.firstVisibleItemIndex == 0 &&
                lazyColumnState.firstVisibleItemScrollOffset < 100
        }
    }

    val searchState by searchScreenViewModel.searchState.collectAsStateWithLifecycle()

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    when (val s = searchState) {
        is SearchState.Searching -> {
            Text(text = "Searching...")
        }

        is SearchState.Ready -> {
            val movieList = s.movieList
            SearchResult(
                searchText = s.textFieldValue,
                movieList = movieList,
                searchMovies = searchScreenViewModel::query,
                updateSearchText = searchScreenViewModel::updateSearchText,
                onMovieClick = onMovieClick
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchResult(
    searchText: TextFieldValue,
    movieList: MovieList,
    searchMovies: () -> Unit,
    updateSearchText: (TextFieldValue) -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    lazyColumnState: LazyListState = rememberLazyListState(),
    contentPadding: Padding = LocalContentPadding.current
) {
    val focusManager = LocalFocusManager.current
    val searchResult = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier,
        state = lazyColumnState
    ) {
        item {
            TextField(
                value = searchText,
                onValueChange = updateSearchText,
                placeholder = {
                    Text(text = stringResource(R.string.search_screen_et_placeholder))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = contentPadding.start,
                        end = contentPadding.end,
                        top = 12.dp
                    )
                    .onPreviewKeyEvent {
                        if (it.type == KeyEventType.KeyUp) {
                            when (it.key) {
                                Key.DirectionUp -> {
                                    focusManager.moveFocus(FocusDirection.Up)
                                    true
                                }

                                Key.DirectionDown -> {
                                    focusManager.moveFocus(FocusDirection.Down)
                                    true
                                }

                                Key.Back -> {
                                    focusManager.moveFocus(FocusDirection.Exit)
                                }

                                else -> false
                            }
                        } else {
                            false
                        }
                    },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        searchMovies()
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ),
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        item {
            MoviesRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = contentPadding.top * 2)
                    .focusRequester(searchResult)
                    .onPlaced {
                        if (movieList.isNotEmpty()) {
                            searchResult.tryRequestFocus()
                        }
                    },
                movieList = movieList
            ) { selectedMovie -> onMovieClick(selectedMovie) }
        }
    }
}
