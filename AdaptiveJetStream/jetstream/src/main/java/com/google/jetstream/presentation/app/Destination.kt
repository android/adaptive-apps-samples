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

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// ToDo: update names to read the resource file
@Serializable
sealed class Destination : NavKey {
    @Serializable
    data object Home : Destination()

    @Serializable
    data object Categories : Destination()

    @Serializable
    data object Movies : Destination()

    @Serializable
    data object Shows : Destination()

    @Serializable
    data object Favourites : Destination()

    @Serializable
    data object Search : Destination()

    @Serializable
    data object Profile : Destination()

    @Serializable
    data class CategoryMovieList(val categoryId: String) : Destination()

    @Serializable
    data class MovieDetails(val movieId: String) : Destination()

    @Serializable
    data class VideoPlayer(val movieId: String) : Destination()

    @Serializable
    data object About : Destination()

    @Serializable
    data object Accounts : Destination()

    @Serializable
    data object Subtitles : Destination()

    @Serializable
    data object Language : Destination()

    @Serializable
    data object SearchHistory : Destination()

    @Serializable
    data object HelpAndSupport : Destination()
}
