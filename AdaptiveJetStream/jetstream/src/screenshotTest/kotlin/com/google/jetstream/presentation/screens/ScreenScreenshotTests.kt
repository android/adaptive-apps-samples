/*
 * Copyright 2025 Google LLC
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

package com.google.jetstream.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.tools.screenshot.PreviewTest
import com.google.jetstream.presentation.components.AdaptivePreview
import com.google.jetstream.presentation.components.JetStreamPreview
import com.google.jetstream.presentation.screens.home.Catalog as HomeCatalog
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsList
import com.google.jetstream.presentation.screens.categories.CategoryDetails
import com.google.jetstream.presentation.screens.shows.Catalog as ShowsCatalog
import com.google.jetstream.presentation.screens.movies.Catalog as MoviesCatalog

@PreviewTest
@AdaptivePreview
@Composable
fun HomeScreenScreenshot() {
    JetStreamPreview {
        Surface {
            HomeCatalog(
                featuredMovies = TestMovieList,
                trendingMovies = TestMovieList,
                top10Movies = TestMovieList,
                nowPlayingMovies = TestMovieList,
                onMovieClick = { _ -> },
                onScroll = { _ -> },
                goToVideoPlayer = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun MovieDetailsScreenScreenshot() {
    JetStreamPreview {
        Surface {
            MovieDetailsList(
                movieDetails = TestMovieDetails,
                goToMoviePlayer = { _ -> },
                refreshScreenWithNewMovie = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun CategoryMovieListScreenScreenshot() {
    JetStreamPreview {
        Surface {
            CategoryDetails(
                categoryDetails = TestCategoryDetails,
                onBackPressed = {},
                onMovieSelected = { _ -> },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun ShowsScreenScreenshot() {
    JetStreamPreview {
        Surface {
            ShowsCatalog(
                tvShowList = TestMovieList,
                bingeWatchDramaList = TestMovieList,
                onTVShowClick = { _ -> },
                onScroll = { _ -> },
                isTopBarVisible = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@PreviewTest
@AdaptivePreview
@Composable
fun MoviesScreenScreenshot() {
    JetStreamPreview {
        Surface {
            MoviesCatalog(
                movieList = TestMovieList,
                popularFilmsThisWeek = TestMovieList,
                onMovieClick = { _ -> },
                onScroll = { _ -> },
                isTopBarVisible = true,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
