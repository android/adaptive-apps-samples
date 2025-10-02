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

package com.google.jetstream.presentation.screens

import androidx.annotation.DrawableRes
import androidx.annotation.IntRange
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.jetstream.R
import com.google.jetstream.data.convert.TryFrom
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen

enum class Screens(
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val isMainNavigation: Boolean = false,
    val tabIcon: ImageVector? = null,
    val navigationVisibility: NavigationVisibility = NavigationVisibility.Visible,
    @DrawableRes val navIcon: Int = 0
) {
    Profile,
    Home(isTabItem = true, isMainNavigation = true, navIcon = R.drawable.ic_home),
    Categories(isTabItem = true, isMainNavigation = true, navIcon = R.drawable.ic_category),
    Movies(isTabItem = true, isMainNavigation = true, navIcon = R.drawable.ic_movies),
    Shows(isTabItem = true, isMainNavigation = true, navIcon = R.drawable.ic_shows),
    Favourites(isTabItem = true, isMainNavigation = true, navIcon = R.drawable.ic_favorites),
    Search(isTabItem = true, tabIcon = Icons.Default.Search, navIcon = R.drawable.ic_search),
    CategoryMovieList(listOf(CategoryMovieListScreen.CategoryIdBundleKey)),
    MovieDetails(
        listOf(MovieDetailsScreen.MOVIE_ID_BUNDLE_KEY),
        navigationVisibility = NavigationVisibility.VisibleInNavigationSuite
    ),
    VideoPlayer(
        listOf(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY),
        navigationVisibility = NavigationVisibility.Hidden
    );

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }

    fun toIndex(): Int {
        return entries.indexOf(this)
    }

    companion object : TryFrom<String, Screens?> {
        fun fromIndex(@IntRange(from = 0) index: Int): Screens? {
            return when {
                index < 0 -> null
                index >= entries.size -> null
                else -> entries[index]
            }
        }

        override fun tryFrom(from: String): Screens? {
            return when (from) {
                Profile() -> Profile
                Home() -> Home
                Categories() -> Categories
                Movies() -> Movies
                Shows() -> Shows
                Favourites() -> Favourites
                Search() -> Search
                CategoryMovieList() -> CategoryMovieList
                MovieDetails() -> MovieDetails
                VideoPlayer() -> VideoPlayer
                else -> null
            }
        }
    }
}

sealed interface NavigationVisibility {
    val isVisibleInNavigationSuite: Boolean
    val isVisibleInTopBar: Boolean

    data object Visible : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInTopBar = true
    }
    data object Hidden : NavigationVisibility {
        override val isVisibleInNavigationSuite = false
        override val isVisibleInTopBar = false
    }
    data object VisibleInNavigationSuite : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInTopBar = false
    }
}
