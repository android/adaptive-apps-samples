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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.jetstream.R
import kotlinx.serialization.Serializable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Transient

@Serializable
sealed class Screens(
    @Transient
    val tabIcon: ImageVector? = null,
    val navigationVisibility: NavigationVisibility = NavigationVisibility.Visible,
    @DrawableRes val navIcon: Int = 0
) {
    @Serializable
    data object Profile : Screens(), NavKey

    @Serializable
    data object Home : Screens(navIcon = R.drawable.ic_home), NavKey

    @Serializable
    data object Categories : Screens(navIcon = R.drawable.ic_category), NavKey

    @Serializable
    data object Movies : Screens(navIcon = R.drawable.ic_movies), NavKey

    @Serializable
    data object Shows : Screens(navIcon = R.drawable.ic_shows), NavKey

    @Serializable
    data object Favourites : Screens(navIcon = R.drawable.ic_favorites), NavKey

    @Serializable
    data object Search : Screens(tabIcon = Icons.Default.Search, navIcon = R.drawable.ic_search), NavKey

    @Serializable
    data class CategoryMovieList(val categoryId: String) : Screens(), NavKey

    @Serializable
    data class MovieDetails(val movieId: String) :
        Screens(navigationVisibility = NavigationVisibility.VisibleInNavigationSuite), NavKey

    @Serializable
    data class VideoPlayer(val movieId: String) :
        Screens(navigationVisibility = NavigationVisibility.Hidden), NavKey
}

sealed interface NavigationVisibility {
    val isVisibleInNavigationSuite: Boolean
    val isVisibleInCustomNavigation: Boolean

    data object Visible : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInCustomNavigation = true
    }
    data object Hidden : NavigationVisibility {
        override val isVisibleInNavigationSuite = false
        override val isVisibleInCustomNavigation = false
    }
    data object VisibleInNavigationSuite : NavigationVisibility {
        override val isVisibleInNavigationSuite = true
        override val isVisibleInCustomNavigation = false
    }
}
