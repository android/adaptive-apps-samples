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

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.jetstream.R
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.app.rememberNavigationComponentType
import com.google.jetstream.presentation.components.BackButton
import com.google.jetstream.presentation.components.Error
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.components.MoviesRow
import com.google.jetstream.presentation.components.desktop.BackNavigationContextMenu
import com.google.jetstream.presentation.components.feature.isBackButtonRequired
import com.google.jetstream.presentation.screens.moviedetails.components.CastAndCrewList
import com.google.jetstream.presentation.screens.moviedetails.components.MovieDetails
import com.google.jetstream.presentation.screens.moviedetails.components.MovieReviews
import com.google.jetstream.presentation.screens.moviedetails.components.TitleValueText
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

object MovieDetailsScreen {
    const val MOVIE_ID_BUNDLE_KEY = "movieId"
}

val movieDetailsScreenArguments = listOf(
    navArgument(MovieDetailsScreen.MOVIE_ID_BUNDLE_KEY) {
        type = NavType.StringType
    }
)

@Composable
fun MovieDetailsScreen(
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
    refreshScreenWithNewMovie: (Movie) -> Unit,
    movieDetailsScreenViewModel: MovieDetailsScreenViewModel = hiltViewModel()
) {
    val uiState by movieDetailsScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        is MovieDetailsScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is MovieDetailsScreenUiState.Error -> {
            Error(modifier = Modifier.fillMaxSize())
        }

        is MovieDetailsScreenUiState.Done -> {
            Details(
                movieDetails = s.movieDetails,
                goToMoviePlayer = goToMoviePlayer,
                onBackPressed = onBackPressed,
                refreshScreenWithNewMovie = refreshScreenWithNewMovie,
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            )
        }
    }
}

@Composable
private fun Details(
    movieDetails: MovieDetails,
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
    refreshScreenWithNewMovie: (Movie) -> Unit,
    modifier: Modifier = Modifier,
) {
    val navigationComponentType = rememberNavigationComponentType()
    val isBackButtonRequired =
        isBackButtonRequired() && navigationComponentType == NavigationComponentType.TopBar

    val lazyListState = rememberLazyListState()
    val isBackButtonVisible by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                lazyListState.firstVisibleItemScrollOffset < 100
        }
    }

    BackHandler(onBack = onBackPressed)

    BackNavigationContextMenu(onBackPressed) {
        MovieDetailsList(
            movieDetails = movieDetails,
            goToMoviePlayer = goToMoviePlayer,
            refreshScreenWithNewMovie = refreshScreenWithNewMovie,
            modifier = modifier,
            state = lazyListState
        )
        if (isBackButtonRequired) {
            AnimatedVisibility(isBackButtonVisible, enter = fadeIn(), exit = fadeOut()) {
                BackButton(
                    onClick = onBackPressed,
                    description = stringResource(R.string.back_from_movie_details),
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 52.dp)
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsList(
    movieDetails: MovieDetails,
    goToMoviePlayer: () -> Unit,
    refreshScreenWithNewMovie: (Movie) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: Padding = LocalContentPadding.current,
) {

    LazyColumn(
        state = state,
        contentPadding = PaddingValues(bottom = 135.dp),
        modifier = modifier,
    ) {
        item {
            MovieDetails(
                movieDetails = movieDetails,
                goToMoviePlayer = goToMoviePlayer
            )
        }

        item {
            CastAndCrewList(
                castAndCrew = movieDetails.castAndCrew
            )
        }

        item {
            MoviesRow(
                title = StringConstants
                    .Composable
                    .movieDetailsScreenSimilarTo(movieDetails.name),
                titleStyle = MaterialTheme.typography.titleMedium,
                movieList = movieDetails.similarMovies,
                onMovieSelected = refreshScreenWithNewMovie
            )
        }

        item {
            MovieReviews(
                modifier = Modifier.padding(top = contentPadding.top),
                reviewsAndRatings = movieDetails.reviewsAndRatings
            )
        }

        item {
            Box(
                modifier = Modifier
                    .padding(horizontal = contentPadding.start)
                    .padding(BottomDividerPadding)
                    .fillMaxWidth()
                    .height(1.dp)
                    .alpha(0.15f)
                    .background(MaterialTheme.colorScheme.onSurface)
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = contentPadding.start),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val itemModifier = Modifier.width(192.dp)

                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.status),
                    value = movieDetails.status
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.original_language),
                    value = movieDetails.originalLanguage
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.budget),
                    value = movieDetails.budget
                )
                TitleValueText(
                    modifier = itemModifier,
                    title = stringResource(R.string.revenue),
                    value = movieDetails.revenue
                )
            }
        }
    }
}

private val BottomDividerPadding = PaddingValues(vertical = 48.dp)
