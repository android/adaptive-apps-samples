/*
 * Copyright 2026 Google LLC
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

package com.google.jetstream.presentation.app

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Support
import androidx.compose.material.icons.filled.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.jetstream.R

sealed interface NavigationItem {
    val name: String
        @Composable get

    val icon: Painter
        @Composable get

    val destination: Destination

    data object Home : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_home)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_home)
        override val destination: Destination = Destination.Home
    }

    data object Categories : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_categories)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_category)
        override val destination: Destination = Destination.Categories
    }

    data object Movies : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_movies)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_movies)
        override val destination: Destination = Destination.Movies
    }

    data object Shows : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_shows)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_shows)
        override val destination: Destination = Destination.Shows
    }

    data object Favorites : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_favorites)
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_favorites)
        override val destination: Destination = Destination.Favourites
    }

    data object Search : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_search)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Search)
        override val destination: Destination = Destination.Search
    }

    data object Profile : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_profile)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Person)
        override val destination: Destination = Destination.Profile
    }

    data object About : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_about)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Info)
        override val destination: Destination = Destination.About
    }

    data object Accounts : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_accounts)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Person)
        override val destination: Destination = Destination.Accounts
    }

    data object Subtitles : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_subtitles)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Subtitles)
        override val destination: Destination = Destination.Subtitles
    }

    data object Language : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_language)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Translate)
        override val destination: Destination = Destination.Language
    }

    data object SearchHistory : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_search_history)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Search)
        override val destination: Destination = Destination.SearchHistory
    }

    data object HelpAndSupport : NavigationItem {
        override val name: String @Composable get() = stringResource(R.string.navigation_help_and_support)
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Support)
        override val destination: Destination = Destination.HelpAndSupport
    }

    companion object {
        val RootDestinations: List<NavigationItem>
            get() {
                return listOf(
                    Home,
                    Categories,
                    Movies,
                    Shows,
                    Favorites,
                )
            }

        val ProfileSettings: List<NavigationItem>
            get() {
                return listOf(
                    About,
                    Accounts,
                    Subtitles,
                    Language,
                    SearchHistory,
                    HelpAndSupport,
                )
            }
    }
}
