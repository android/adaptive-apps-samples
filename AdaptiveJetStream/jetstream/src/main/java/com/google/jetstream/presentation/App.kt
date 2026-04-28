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

package com.google.jetstream.presentation

import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldScope
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.app.PresentationType
import com.google.jetstream.presentation.app.rememberAppLayoutSceneDecorator
import com.google.jetstream.presentation.app.selectAppNavigation
import com.google.jetstream.presentation.screens.categories.CategoriesScreen
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreenViewModel
import com.google.jetstream.presentation.screens.favourites.FavouritesScreen
import com.google.jetstream.presentation.screens.home.HomeScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreenViewModel
import com.google.jetstream.presentation.screens.movies.MoviesScreen
import com.google.jetstream.presentation.screens.profile.ProfileScreen
import com.google.jetstream.presentation.screens.profile.section.AboutSection
import com.google.jetstream.presentation.screens.profile.section.AccountsSection
import com.google.jetstream.presentation.screens.profile.section.HelpAndSupportSection
import com.google.jetstream.presentation.screens.profile.section.LanguageSection
import com.google.jetstream.presentation.screens.profile.section.SearchHistorySection
import com.google.jetstream.presentation.screens.profile.section.SubtitlesSection
import com.google.jetstream.presentation.screens.search.SearchScreen
import com.google.jetstream.presentation.screens.shows.ShowsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreenViewModel

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    val backStack = rememberNavBackStack(Destination.Home)
    val appNavigation = selectAppNavigation()
    var isTopbarVisible by rememberSaveable { mutableStateOf(true) }

    Surface {
        NavDisplay(
            backStack = backStack,
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            sceneStrategies =
                listOf(
                    rememberListDetailSceneStrategy(),
                ),
            sceneDecoratorStrategies =
                listOf(
                    rememberAppLayoutSceneDecorator(
                        navigation = {
                            appNavigation.Navigation(
                                current = backStack.lastOrNull() as? Destination,
                                onNavigation = backStack::add,
                                isVisible = isTopbarVisible,
                            )
                        },
                        subNavigation = {
                            appNavigation.SubNavigation(
                                current = backStack.lastOrNull() as? Destination,
                                onNavigation = backStack::add,
                            )
                        },
                    ),
                ),
            entryProvider =
                entryProvider {
                    entry<Destination.Home>(
                        metadata = Destination.Home.navEntryMetadata(),
                    ) {
                        HomeScreen(
                            onMovieClick = {
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            goToVideoPlayer = {
                                backStack.add(Destination.VideoPlayer(movieId = it.id))
                            },
                            onScroll = {
                                isTopbarVisible = it
                            },
                            isTopBarVisible = true,
                        )
                    }
                    entry<Destination.Categories>(
                        metadata = Destination.Categories.navEntryMetadata(),
                    ) {
                        CategoriesScreen(
                            onCategoryClick = {
                                backStack.add(Destination.CategoryMovieList(categoryId = it))
                            },
                        )
                    }
                    entry<Destination.Movies>(
                        metadata = Destination.Movies.navEntryMetadata(),
                    ) {
                        MoviesScreen(
                            onMovieClick = {
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            onScroll = {
                                isTopbarVisible = it
                            },
                            isTopBarVisible = true,
                        )
                    }
                    entry<Destination.Shows>(
                        metadata = Destination.Shows.navEntryMetadata(),
                    ) {
                        ShowsScreen(
                            onTVShowClick = {
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            onScroll = {
                                isTopbarVisible = it
                            },
                            isTopBarVisible = true,
                        )
                    }
                    entry<Destination.Favourites>(
                        metadata = Destination.Favourites.navEntryMetadata(),
                    ) {
                        FavouritesScreen(
                            onMovieClick = {
                                backStack.add(Destination.MovieDetails(movieId = it))
                            },
                            onScroll = {
                                isTopbarVisible = it
                            },
                            isTopBarVisible = true,
                        )
                    }
                    entry<Destination.Search>(
                        metadata = Destination.Search.navEntryMetadata(),
                    ) {
                        SearchScreen(
                            onMovieClick = {
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            onScroll = {
                                isTopbarVisible = it
                            },
                        )
                    }
                    entry<Destination.MovieDetails>(
                        metadata = Destination.MovieDetails.navEntryMetadata(),
                    ) { destination ->
                        val viewModel =
                            hiltViewModel<MovieDetailsScreenViewModel, MovieDetailsScreenViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(destination.movieId)
                                },
                            )
                        MovieDetailsScreen(
                            goToMoviePlayer = {
                                backStack.add(Destination.VideoPlayer(movieId = it.id))
                            },
                            refreshScreenWithNewMovie = {
                                backStack.removeLastOrNull()
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            onBackPressed = backStack::removeLastOrNull,
                            movieDetailsScreenViewModel = viewModel,
                        )
                    }
                    entry<Destination.CategoryMovieList>(
                        metadata = Destination.CategoryMovieList.navEntryMetadata(),
                    ) {
                        val viewModel =
                            hiltViewModel<CategoryMovieListScreenViewModel, CategoryMovieListScreenViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(it.categoryId)
                                },
                            )
                        CategoryMovieListScreen(
                            onBackPressed = backStack::removeLastOrNull,
                            onMovieSelected = {
                                backStack.add(Destination.MovieDetails(movieId = it.id))
                            },
                            categoryMovieListScreenViewModel = viewModel,
                        )
                    }
                    entry<Destination.VideoPlayer>(
                        metadata = Destination.VideoPlayer.navEntryMetadata(),
                    ) { destination ->
                        val viewModel =
                            hiltViewModel<VideoPlayerScreenViewModel, VideoPlayerScreenViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(destination.movieId)
                                },
                            )
                        VideoPlayerScreen(
                            onBackPressed = backStack::removeLastOrNull,
                            videoPlayerScreenViewModel = viewModel,
                        )
                    }
                    entry<Destination.Profile>(
                        metadata =
                            Destination.Profile.navEntryMetadata {
                                AboutSection()
                            },
                    ) {
                        ProfileScreen(
                            onDestinationSelected = {
                                backStack.add(it)
                            },
                        )
                    }
                    entry<Destination.About>(
                        metadata = Destination.About.navEntryMetadata(),
                    ) {
                        AboutSection()
                    }
                    entry<Destination.Accounts>(
                        metadata = Destination.Accounts.navEntryMetadata(),
                    ) {
                        AccountsSection()
                    }
                    entry<Destination.Subtitles>(
                        metadata = Destination.Subtitles.navEntryMetadata(),
                    ) {
                        var isSubtitleChecked by rememberSaveable { mutableStateOf(false) }
                        SubtitlesSection(isSubtitlesChecked = isSubtitleChecked) {
                            isSubtitleChecked = it
                        }
                    }
                    entry<Destination.Language>(
                        metadata = Destination.Language.navEntryMetadata(),
                    ) {
                        var selectedLanguageIndex by rememberSaveable {
                            mutableIntStateOf(0)
                        }
                        LanguageSection(selectedIndex = selectedLanguageIndex) {
                            selectedLanguageIndex = it
                        }
                    }
                    entry<Destination.SearchHistory>(
                        metadata = Destination.SearchHistory.navEntryMetadata(),
                    ) {
                        SearchHistorySection()
                    }
                    entry<Destination.HelpAndSupport>(
                        metadata = Destination.HelpAndSupport.navEntryMetadata(),
                    ) {
                        HelpAndSupportSection()
                    }
                },
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun Destination.navEntryMetadata(
    detailsPlaceHolder: @Composable ThreePaneScaffoldScope.() -> Unit = {},
): Map<String, Any> {
    return metadata {
        put(Destination.MetadataKey, presentationType)
    } +
        when (presentationType) {
            PresentationType.ListDetailChild -> {
                ListDetailSceneStrategy.detailPane()
            }

            PresentationType.ListDetailParent -> {
                ListDetailSceneStrategy.listPane(
                    detailPlaceholder = detailsPlaceHolder,
                )
            }

            else -> {
                mapOf()
            }
        }
}

private fun Destination.MovieDetails.Companion.navEntryMetadata(): Map<String, Any> {
    return metadata {
        put(Destination.MetadataKey, presentationType)
    }
}

private fun Destination.CategoryMovieList.Companion.navEntryMetadata(): Map<String, Any> {
    return metadata {
        put(Destination.MetadataKey, presentationType)
    }
}

private fun Destination.VideoPlayer.Companion.navEntryMetadata(): Map<String, Any> {
    return metadata {
        put(Destination.MetadataKey, presentationType)
    }
}
