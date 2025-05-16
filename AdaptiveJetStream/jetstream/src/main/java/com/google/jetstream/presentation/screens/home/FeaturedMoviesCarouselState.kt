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

package com.google.jetstream.presentation.screens.home

import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.setValue
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalTvMaterial3Api::class)
internal class FeaturedMoviesCarouselState(
    internal val itemCount: Int,
    initialActiveIndex: Int = 0,
    initialWatchNowButtonVisibility: Boolean = false,
) {
    var carouselState by mutableStateOf(CarouselState(initialActiveIndex))
        private set

    val activeItemIndex: Int
        get() = carouselState.activeItemIndex

    var watchNowButtonVisibility by mutableStateOf(initialWatchNowButtonVisibility)
        private set

    fun moveToNextItem() {
        val updatedIndex = (activeItemIndex + 1) % itemCount
        carouselState = CarouselState(updatedIndex)
    }

    fun moveToPreviousItem() {
        val updatedIndex = (activeItemIndex - 1 + itemCount) % itemCount
        carouselState = CarouselState(updatedIndex)
    }

    fun updateWatchNowButtonVisibility(isVisible: Boolean) {
        watchNowButtonVisibility = isVisible
    }

    companion object {
        val Saver =
            Saver<FeaturedMoviesCarouselState, FeaturedMoviesCarouselSnapshot>(
                save = { FeaturedMoviesCarouselSnapshot.from(it) },
                restore = { it.into() }
            )
    }
}

@Parcelize
internal data class FeaturedMoviesCarouselSnapshot(
    val itemCount: Int,
    val activeItemIndex: Int,
    val watchButtonVisibility: Boolean,
) : Parcelable {
    fun into(): FeaturedMoviesCarouselState =
        FeaturedMoviesCarouselState(
            itemCount = itemCount,
            initialActiveIndex = activeItemIndex,
            initialWatchNowButtonVisibility = watchButtonVisibility
        )

    companion object {
        fun from(state: FeaturedMoviesCarouselState) =
            FeaturedMoviesCarouselSnapshot(
                itemCount = state.itemCount,
                activeItemIndex = state.activeItemIndex,
                watchButtonVisibility = state.watchNowButtonVisibility
            )
    }
}
