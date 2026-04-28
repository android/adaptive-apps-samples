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
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import com.google.jetstream.R
import kotlinx.serialization.Serializable

// ToDo: update names to read the resource file
@Serializable
sealed class Destination(
    val presentationType: PresentationType = PresentationType.SinglePane,
) : NavKey {
    open val name: String
        @Composable get() {
            return ""
        }
    open val icon: Painter?
        @Composable get() {
            return null
        }

    @Serializable
    data object Home : Destination() {
        override val name: String @Composable get() = "Home"
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_home)
    }

    @Serializable
    data object Categories : Destination() {
        override val name: String @Composable get() = "Categories"
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_category)
    }

    @Serializable
    data object Movies : Destination() {
        override val name: String @Composable get() = "Movies"
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_movies)
    }

    @Serializable
    data object Shows : Destination() {
        override val name: String @Composable get() = "Shows"
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_shows)
    }

    @Serializable
    data object Favourites : Destination() {
        override val name: String @Composable get() = "Favourites"
        override val icon: Painter @Composable get() = painterResource(R.drawable.ic_favorites)
    }

    @Serializable
    data object Search : Destination() {
        override val name: String @Composable get() = "Search"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Search)
    }

    @Serializable
    data object Profile : Destination(presentationType = PresentationType.ListDetailParent) {
        override val name: String @Composable get() = "Profile"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Person)
    }

    @Serializable
    data class CategoryMovieList(val categoryId: String) : Destination() {
        companion object {
            val presentationType = PresentationType.SinglePane
        }
    }

    @Serializable
    data class MovieDetails(val movieId: String) : Destination() {
        companion object {
            val presentationType = PresentationType.Overlay
        }
    }

    @Serializable
    data class VideoPlayer(val movieId: String) :
        Destination(presentationType = PresentationType.Overlay) {
        companion object {
            val presentationType = PresentationType.Overlay
        }
    }

    @Serializable
    data object About : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "About"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Info)
    }

    @Serializable
    data object Accounts : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "Accounts"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Person)
    }

    @Serializable
    data object Subtitles : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "Subtitles"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Subtitles)
    }

    @Serializable
    data object Language : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "Language"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Translate)
    }

    @Serializable
    data object SearchHistory : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "Search history"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Search)
    }

    @Serializable
    data object HelpAndSupport : Destination(presentationType = PresentationType.ListDetailChild) {
        override val name: String @Composable get() = "Help and Support"
        override val icon: Painter @Composable get() = rememberVectorPainter(Icons.Default.Support)
    }

    companion object {
        val MetadataKey = object : NavMetadataKey<PresentationType> {}
        val RootDestinations: List<Destination>
            get() {
                return listOf(
                    Home,
                    Categories,
                    Movies,
                    Shows,
                    Favourites,
                )
            }

        val ProfileSettings: List<Destination>
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

enum class PresentationType {
    SinglePane,
    ListDetailParent,
    ListDetailChild,
    Overlay,
}
