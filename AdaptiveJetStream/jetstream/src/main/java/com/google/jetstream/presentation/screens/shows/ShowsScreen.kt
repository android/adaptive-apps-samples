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

package com.google.jetstream.presentation.screens.shows

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieList
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.components.MoviesRow
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.movies.components.ProminentMovieList
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

@Composable
fun ShowsScreen(
    onTVShowClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    showScreenViewModel: ShowScreenViewModel = hiltViewModel(),
) {
    val uiState = showScreenViewModel.uiState.collectAsStateWithLifecycle()
    when (val currentState = uiState.value) {
        is ShowScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is ShowScreenUiState.Ready -> {
            Catalog(
                tvShowList = currentState.tvShowList,
                bingeWatchDramaList = currentState.bingeWatchDramaList,
                onTVShowClick = onTVShowClick,
                onScroll = onScroll,
                isTopBarVisible = isTopBarVisible,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun Catalog(
    tvShowList: MovieList,
    bingeWatchDramaList: MovieList,
    onTVShowClick: (movie: Movie) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    isTopBarVisible: Boolean,
    modifier: Modifier = Modifier,
    contentPadding: Padding = LocalContentPadding.current
) {
    val lazyListState = rememberLazyListState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                lazyListState.firstVisibleItemScrollOffset < 50
        }
    }

    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }
    LaunchedEffect(isTopBarVisible) {
        if (isTopBarVisible) lazyListState.animateScrollToItem(0)
    }

    val featured = remember { FocusRequester() }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(top = contentPadding.top, bottom = 104.dp),
        modifier = modifier
            .focusProperties {
                onEnter = {
                    featured.tryRequestFocus()
                }
            }
    ) {
        item {
            ProminentMovieList(
                movieList = tvShowList,
                onMovieClick = onTVShowClick,
                modifier = Modifier.focusRequester(featured)
            )
        }
        item {
            MoviesRow(
                modifier = Modifier.padding(top = contentPadding.top),
                title = StringConstants.Composable.BingeWatchDramasTitle,
                movieList = bingeWatchDramaList,
                onMovieSelected = onTVShowClick
            )
        }
    }
}
