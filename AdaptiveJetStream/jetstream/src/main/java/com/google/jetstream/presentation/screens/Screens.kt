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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.jetstream.R
import com.google.jetstream.data.convert.TryFrom
import com.google.jetstream.presentation.app.NavigationComponentType
import com.google.jetstream.presentation.screens.categories.CategoryMovieListScreen
import com.google.jetstream.presentation.screens.moviedetails.MovieDetailsScreen
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreen

/**
 * Represents a screen.
 *
 * @param args - The navigation arguments for the screen.
 * @param isTabItem - indicates whether this screen's icon can appear in a tab.
 * @param isMainNavigation - indicates whether this is a top level screen that will appear in the
 * main navigation area.
 * @param tabIcon - The icon to be shown in the tab. Only applicable if this is a tab item.
 * @param shouldShowNavigation - A function that indicates whether the navigation area should be
 * shown for the given `NavigationComponentType`.
 * @param navIcon - The icon to be shown in the navigation area. Only applicable if this is a main
 * navigation item.
 * // TODO: Remove this XR-specific field if possible
 * @param xrContainerColor - Overrides the default screen container color on XR layouts
 */
enum class Screens(
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val isMainNavigation: Boolean = false,
    // TODO: Can we remove either tabIcon or navIcon?
    val tabIcon: ImageVector? = null,
    val shouldShowNavigation: (NavigationComponentType) -> Boolean = { true },
    @DrawableRes val navIcon: Int = 0,
    val xrContainerColor: Color? = null
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
        args = listOf(MovieDetailsScreen.MOVIE_ID_BUNDLE_KEY),
        // Don't show the navigation in the top bar
        shouldShowNavigation = { it != NavigationComponentType.TopBar }
    ),
    VideoPlayer(
        listOf(VideoPlayerScreen.MOVIE_ID_BUNDLE_KEY),
        shouldShowNavigation = { false },
        // Workaround to make video player visible.
        xrContainerColor = Color.Transparent
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

        val tabScreens: List<Screens> = Screens.entries.filter { it.isTabItem }
        val mainNavigationScreens: List<Screens> = Screens.entries.filter { it.isMainNavigation }
    }

    @Composable
    fun xrContainerColor() : Color = xrContainerColor ?: MaterialTheme.colorScheme.background

}
