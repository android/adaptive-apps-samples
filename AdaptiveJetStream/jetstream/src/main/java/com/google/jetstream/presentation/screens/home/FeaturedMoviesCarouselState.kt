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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.CarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.presentation.components.feature.rememberFormFactor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalMaterial3Api::class)
class FeaturedMoviesCarouselState(
    internal val itemCount: Int,
    initialActiveIndex: Int = 0,
    initialWatchNowButtonVisibility: Boolean = false,
    private val autoScrollDelay: Long = 10000L
) {
    val carouselState = CarouselState(initialActiveIndex) {
        itemCount
    }

    val currentItem: Int
        get() = carouselState.currentItem

    var watchNowButtonVisibility by mutableStateOf(initialWatchNowButtonVisibility)
        private set

    private val carouselStateUpdateRequestQueue =
        MutableStateFlow(CarouselStateUpdateRequest.None)

    fun nextItem() {
        carouselStateUpdateRequestQueue
            .tryEmit(CarouselStateUpdateRequest.ImmediateScrollToNextItem)
    }

    fun previousItem() {
        carouselStateUpdateRequestQueue
            .tryEmit(CarouselStateUpdateRequest.ImmediateScrollToPreviousItem)
    }

    private suspend fun scrollToNextItem() {
        val updatedIndex = (currentItem + 1) % itemCount
        carouselState.animateScrollToItem(updatedIndex)
    }

    private suspend fun scrollToPreviousItem() {
        val updatedIndex = (currentItem - 1 + itemCount) % itemCount
        carouselState.animateScrollToItem(updatedIndex)
    }

    fun updateWatchNowButtonVisibility(isVisible: Boolean) {
        watchNowButtonVisibility = isVisible
    }

    internal suspend fun startAutoScroll() {
        carouselStateUpdateRequestQueue.collectLatest { request ->
            when (request) {
                CarouselStateUpdateRequest.ImmediateScrollToNextItem -> {
                    scrollToNextItem()
                    carouselStateUpdateRequestQueue
                        .tryEmit(CarouselStateUpdateRequest.ScrollToNextItem)
                }

                CarouselStateUpdateRequest.ImmediateScrollToPreviousItem -> {
                    scrollToPreviousItem()
                    carouselStateUpdateRequestQueue
                        .tryEmit(CarouselStateUpdateRequest.ScrollToNextItem)
                }

                CarouselStateUpdateRequest.ScrollToNextItem -> {
                    delay(autoScrollDelay)
                    carouselStateUpdateRequestQueue
                        .tryEmit(CarouselStateUpdateRequest.ImmediateScrollToNextItem)
                }

                CarouselStateUpdateRequest.None -> {
                    carouselStateUpdateRequestQueue
                        .tryEmit(CarouselStateUpdateRequest.ScrollToNextItem)
                }
            }
        }
    }

    companion object {
        val Saver =
            Saver<FeaturedMoviesCarouselState, FeaturedMoviesCarouselSnapshot>(
                save = { FeaturedMoviesCarouselSnapshot.from(it) },
                restore = { it.into() }
            )
    }
}

private enum class CarouselStateUpdateRequest {
    ImmediateScrollToNextItem,
    ImmediateScrollToPreviousItem,
    ScrollToNextItem,
    None,
}

@Parcelize
data class FeaturedMoviesCarouselSnapshot(
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
                activeItemIndex = state.currentItem,
                watchButtonVisibility = state.watchNowButtonVisibility
            )
    }
}

@Composable
fun rememberFeaturedMoviesCarouselState(
    movies: List<Movie>,
    initialWatchNowButtonVisibility: Boolean = true,
): FeaturedMoviesCarouselState {
    val formFactor = rememberFormFactor()
    return rememberSaveable(
        movies,
        formFactor,
        saver = FeaturedMoviesCarouselState.Saver
    ) {
        FeaturedMoviesCarouselState(
            itemCount = movies.size,
            initialWatchNowButtonVisibility = initialWatchNowButtonVisibility
        )
    }.also {
        LaunchedEffect(it) {
            it.startAutoScroll()
        }
    }
}
