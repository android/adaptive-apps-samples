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

package com.google.jetstream.presentation.screens.videoPlayer

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.StereoscopicVisionType
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class VideoPlayerScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

    @Test
    fun uiState_withValidMovieId_transitionsToDoneAndPreparesPlayer() =
        runTest {
            val repository = FakeMovieRepository()
            val fakePlayer = FakePlayer()
            val viewModel = VideoPlayerScreenViewModel("movie-42", repository, context)

            viewModel.setPlayerForTesting(fakePlayer.instance, isReady = true)

            val state = viewModel.uiState.first { it is VideoPlayerScreenUiState.Done }
            advanceUntilIdle()
            val doneState = state as VideoPlayerScreenUiState.Done
            assertEquals("movie-42", doneState.nowPlayingInfo.movieDetails.id)
            assertTrue(doneState.isReadyToPlay)
            assertTrue(fakePlayer.isPrepared)
        }

    @Test
    fun uiState_withBlankOrMissingMovieId_transitionsToError() =
        runTest {
            val repository = FakeMovieRepository().apply { shouldThrowOnGetMovieDetails = true }
            val fakePlayer = FakePlayer()
            val viewModel = VideoPlayerScreenViewModel("non-existent", repository, context)

            viewModel.setPlayerForTesting(fakePlayer.instance, isReady = true)

            val state = viewModel.uiState.first { it is VideoPlayerScreenUiState.Error }
            assertEquals(VideoPlayerScreenUiState.Error, state)
        }

    @Test
    fun updateSpatialUiEnabled_selectsSideBySideWhenSourceAvailable() =
        runTest {
            val sbsMovieDetails =
                MovieDetails(
                    id = "sbs-1",
                    sources =
                        mapOf(
                            StereoscopicVisionType.Mono to Uri.parse("https://example.com/mono.mp4"),
                            StereoscopicVisionType.SideBySide to Uri.parse("https://example.com/sbs.mp4"),
                        ),
                    subtitleUri = "",
                    posterUri = "",
                    name = "3D Movie",
                    description = "",
                    pgRating = "",
                    releaseDate = "",
                    categories = emptyList(),
                    duration = "",
                    director = "",
                    screenplay = "",
                    music = "",
                    castAndCrew = emptyList(),
                    status = "",
                    originalLanguage = "",
                    budget = "",
                    revenue = "",
                    similarMovies = emptyList(),
                    reviewsAndRatings = emptyList(),
                )
            val repository =
                FakeMovieRepository().apply {
                    customMovieDetails = sbsMovieDetails
                }
            val fakePlayer = FakePlayer()
            val viewModel = VideoPlayerScreenViewModel("sbs-1", repository, context)
            viewModel.setPlayerForTesting(fakePlayer.instance, isReady = true)

            // Default (non-spatial) uses Mono
            val initialDone = viewModel.uiState.first { it is VideoPlayerScreenUiState.Done } as VideoPlayerScreenUiState.Done
            assertEquals(StereoscopicVisionType.Mono, initialDone.nowPlayingInfo.stereoscopicVisionType)

            // Enabling Spatial UI switches to SideBySide
            viewModel.updateSpatialUiEnabled(true)
            val spatialDone =
                viewModel.uiState.first {
                    it is VideoPlayerScreenUiState.Done &&
                        it.nowPlayingInfo.stereoscopicVisionType == StereoscopicVisionType.SideBySide
                } as VideoPlayerScreenUiState.Done
            assertEquals(StereoscopicVisionType.SideBySide, spatialDone.nowPlayingInfo.stereoscopicVisionType)
        }
}
