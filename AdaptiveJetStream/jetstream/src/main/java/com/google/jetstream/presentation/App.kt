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

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.app.PresentationType
import com.google.jetstream.presentation.app.rememberAppLayoutSceneDecorator
import com.google.jetstream.presentation.app.selectAppNavigation
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.onBackButtonPressed
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

/**
 * Main entry point for the JetStream application UI.
 * This composable sets up the navigation backstack, global navigation components,
 * and defines the routing for all screens in the app using Navigation3.
 */
@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    val navigator = rememberNavigator(initialDestination = Destination.Home)

    // Selects the navigation implementation (Rail, Bar, or TopBar) based on device type/mode
    val appNavigation = selectAppNavigation()
    // Tracks visibility of the top bar/navigation rail for hide-on-scroll behavior
    var isTopbarVisible by rememberSaveable { mutableStateOf(true) }
    var isTopbarFocused by remember { mutableStateOf(true) }

    Surface(
        modifier =
            Modifier.leanbackBackHandler(
                navigator = navigator,
                isTopbarVisible = isTopbarVisible,
                isTopbarFocused = isTopbarFocused,
            ) {
                isTopbarVisible = true
            },
    ) {
        NavDisplay(
            backStack = navigator.backStack,
            modifier = modifier,
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            sceneStrategies =
                listOf(
                    // Strategy for List-Detail layouts (e.g., Profile screen)
                    rememberListDetailSceneStrategy(),
                ),
            sceneDecoratorStrategies =
                listOf(
                    // Custom strategy to wrap screens in the appropriate app-level navigation (Rail/Bar/TopBar)
                    rememberAppLayoutSceneDecorator(
                        navigation = {
                            Box(
                                modifier =
                                    Modifier
                                        .focusProperties {
                                            onExit = {
                                                isTopbarFocused = false
                                            }
                                            onEnter = {
                                                isTopbarFocused = true
                                            }
                                        }
                                        .focusGroup(),
                            ) {
                                appNavigation.Navigation(
                                    current = navigator.current,
                                    onNavigation = navigator::navigate,
                                    isVisible = isTopbarVisible,
                                )
                            }
                        },
                        subNavigation = {
                            appNavigation.SubNavigation(
                                current = navigator.current,
                                onNavigation = navigator::navigate,
                            )
                        },
                    ),
                ),
            entryProvider =
                entryProvider {
                    homeEntry(
                        navigator = navigator,
                        isTopbarVisible = isTopbarVisible,
                        onUpdateTopbarVisibility = {
                            isTopbarVisible = it
                        },
                    )

                    categoriesEntry(navigator)

                    moviesEntry(
                        navigator = navigator,
                        isTopbarVisible = isTopbarVisible,
                        onUpdateTopbarVisibility = {
                            isTopbarVisible = it
                        },
                    )

                    showsEntry(
                        navigator = navigator,
                        isTopbarVisible = isTopbarVisible,
                        onUpdateTopbarVisibility = { isTopbarVisible = it },
                    )

                    favoritesEntry(
                        navigator = navigator,
                        isTopbarVisible = isTopbarVisible,
                        onUpdateTopbarVisibility = { isTopbarVisible = it },
                    )

                    searchEntry(
                        navigator = navigator,
                        onUpdateTopbarVisibility = { isTopbarVisible = it },
                    )

                    moveDetailsEntry(navigator)

                    videoPlayerEntry(navigator)

                    profileEntries(navigator)
                },
        )
    }
}

private fun EntryProviderScope<NavKey>.homeEntry(
    navigator: Navigator,
    isTopbarVisible: Boolean,
    onUpdateTopbarVisibility: (Boolean) -> Unit,
) {
    entry<Destination.Home>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        HomeScreen(
            onMovieClick = {
                navigator.navigate(Destination.MovieDetails(movieId = it.id))
            },
            goToVideoPlayer = {
                navigator.navigate(Destination.VideoPlayer(movieId = it.id))
            },
            onScroll = onUpdateTopbarVisibility,
            isTopBarVisible = isTopbarVisible,
        )
    }
}

private fun EntryProviderScope<NavKey>.categoriesEntry(
    navigator: Navigator,
) {
    entry<Destination.Categories>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        CategoriesScreen(
            onCategoryClick = {
                navigator.navigate(Destination.CategoryMovieList(categoryId = it))
            },
        )
    }

    entry<Destination.CategoryMovieList>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.Overlay)
            },
    ) {
        val viewModel =
            hiltViewModel<CategoryMovieListScreenViewModel, CategoryMovieListScreenViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(it.categoryId)
                },
            )
        CategoryMovieListScreen(
            onBackPressed = navigator::goBack,
            onMovieSelected = { movie ->
                navigator.navigate(Destination.MovieDetails(movieId = movie.id))
            },
            categoryMovieListScreenViewModel = viewModel,
        )
    }
}

private fun EntryProviderScope<NavKey>.moviesEntry(
    navigator: Navigator,
    isTopbarVisible: Boolean,
    onUpdateTopbarVisibility: (Boolean) -> Unit,
) {
    entry<Destination.Movies>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        MoviesScreen(
            onMovieClick = {
                navigator.navigate(Destination.MovieDetails(movieId = it.id))
            },
            onScroll = onUpdateTopbarVisibility,
            isTopBarVisible = isTopbarVisible,
        )
    }
}

private fun EntryProviderScope<NavKey>.showsEntry(
    navigator: Navigator,
    isTopbarVisible: Boolean,
    onUpdateTopbarVisibility: (Boolean) -> Unit,
) {
    entry<Destination.Shows>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        ShowsScreen(
            onTVShowClick = {
                navigator.navigate(Destination.MovieDetails(movieId = it.id))
            },
            onScroll = onUpdateTopbarVisibility,
            isTopBarVisible = isTopbarVisible,
        )
    }
}

private fun EntryProviderScope<NavKey>.moveDetailsEntry(
    navigator: Navigator,
) {
    entry<Destination.MovieDetails>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.Overlay)
            },
    ) { destination ->
        val viewModel =
            hiltViewModel<MovieDetailsScreenViewModel, MovieDetailsScreenViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(destination.movieId)
                },
            )
        MovieDetailsScreen(
            goToMoviePlayer = {
                navigator.navigate(Destination.VideoPlayer(movieId = it.id))
            },
            refreshScreenWithNewMovie = {
                navigator.goBack()
                navigator.navigate(Destination.MovieDetails(movieId = it.id))
            },
            onBackPressed = navigator::goBack,
            movieDetailsScreenViewModel = viewModel,
        )
    }
}

private fun EntryProviderScope<NavKey>.favoritesEntry(
    navigator: Navigator,
    isTopbarVisible: Boolean,
    onUpdateTopbarVisibility: (Boolean) -> Unit,
) {
    entry<Destination.Favourites>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        FavouritesScreen(
            onMovieClick = {
                navigator.navigate(Destination.MovieDetails(movieId = it))
            },
            onScroll = onUpdateTopbarVisibility,
            isTopBarVisible = isTopbarVisible,
        )
    }
}

private fun EntryProviderScope<NavKey>.searchEntry(
    navigator: Navigator,
    onUpdateTopbarVisibility: (Boolean) -> Unit,
) {
    entry<Destination.Search>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
            },
    ) {
        SearchScreen(
            onMovieClick = {
                navigator.navigate(Destination.MovieDetails(movieId = it.id))
            },
            onScroll = onUpdateTopbarVisibility,
        )
    }
}

private fun EntryProviderScope<NavKey>.videoPlayerEntry(
    navigator: Navigator,
) {
    entry<Destination.VideoPlayer>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.Overlay)
            },
    ) { destination ->
        val viewModel =
            hiltViewModel<VideoPlayerScreenViewModel, VideoPlayerScreenViewModel.Factory>(
                creationCallback = { factory ->
                    factory.create(destination.movieId)
                },
            )
        VideoPlayerScreen(
            onBackPressed = navigator::goBack,
            videoPlayerScreenViewModel = viewModel,
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
private fun EntryProviderScope<NavKey>.profileEntries(
    navigator: Navigator,
) {
    entry<Destination.Profile>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailParent)
            } +
                ListDetailSceneStrategy.listPane(
                    detailPlaceholder = {
                        AboutSection()
                    },
                ),
    ) {
        ProfileScreen(
            onDestinationSelected = navigator::navigate,
        )
    }

    entry<Destination.About>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        AboutSection()
    }

    entry<Destination.Accounts>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        AccountsSection()
    }

    entry<Destination.Subtitles>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        var isSubtitleChecked by rememberSaveable { mutableStateOf(false) }
        SubtitlesSection(isSubtitlesChecked = isSubtitleChecked) {
            isSubtitleChecked = it
        }
    }

    entry<Destination.Language>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        var selectedLanguageIndex by rememberSaveable {
            mutableIntStateOf(0)
        }
        LanguageSection(selectedIndex = selectedLanguageIndex) {
            selectedLanguageIndex = it
        }
    }

    entry<Destination.SearchHistory>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        SearchHistorySection()
    }

    entry<Destination.HelpAndSupport>(
        metadata =
            metadata {
                put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
            } + ListDetailSceneStrategy.detailPane(),
    ) {
        HelpAndSupportSection()
    }
}

@Composable
private fun Modifier.leanbackBackHandler(
    navigator: Navigator,
    isTopbarVisible: Boolean,
    isTopbarFocused: Boolean,
    requestTopbarBecomeVisible: () -> Unit,
): Modifier {
    return when (LocalEngagementMode.current) {
        is EngagementMode.Leanback -> {
            val focusRequester = remember { FocusRequester() }
            onBackButtonPressed {
                when {
                    !isTopbarVisible -> {
                        requestTopbarBecomeVisible()
                        focusRequester.requestFocus()
                    }

                    !isTopbarFocused -> {
                        focusRequester.requestFocus()
                    }

                    else -> {
                        navigator.goBack()
                    }
                }
            }
                .focusRequester(focusRequester)
                .focusGroup()
        }

        else -> {
            this
        }
    }
}
