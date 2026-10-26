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

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.StereoscopicVisionType
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class VideoPlayerScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testMovieDetails =
        MovieDetails(
            id = "movie-1",
            sources = emptyMap(),
            subtitleUri = null,
            posterUri = "",
            name = "Big Buck Bunny",
            description = "Test video description",
            pgRating = "PG",
            releaseDate = "2024",
            categories = listOf("Animation"),
            duration = "10m",
            director = "Test Director",
            screenplay = "Test Screenplay",
            music = "Test Music",
            castAndCrew = emptyList(),
            status = "Released",
            originalLanguage = "English",
            budget = "$1M",
            revenue = "$5M",
            similarMovies = emptyList(),
            reviewsAndRatings = emptyList(),
        )

    private val testNowPlayingInfo =
        NowPlayingInfo(
            movieDetails = testMovieDetails,
            stereoscopicVisionType = StereoscopicVisionType.Mono,
            videoSize = Size(1920f, 1080f),
        )

    @Test
    fun videoPlayerScreen_whenLoading_displaysLoadingIndicator() {
        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerScreen(
                    uiState = VideoPlayerScreenUiState.Loading,
                    onBackPressed = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Loading…").assertIsDisplayed()
    }

    @Test
    fun videoPlayerScreen_whenError_displaysErrorMessage() {
        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerScreen(
                    uiState = VideoPlayerScreenUiState.Error,
                    onBackPressed = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Whoops, something went wrong…").assertIsDisplayed()
    }

    @Test
    fun videoPlayerScreen_whenDone_displaysTitleControlsAndBackButton() {
        val fakePlayer = FakePlayer(initialIsPlaying = false)
        var backPressed = false

        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(
                    LocalEngagementMode provides EngagementMode.Workstation(),
                ) {
                    VideoPlayerScreen(
                        uiState =
                            VideoPlayerScreenUiState.Done(
                                nowPlayingInfo = testNowPlayingInfo,
                                player = fakePlayer.instance,
                                isReadyToPlay = true,
                            ),
                        onBackPressed = { backPressed = true },
                    )
                }
            }
        }

        composeTestRule.onNodeWithText(testMovieDetails.name).assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(StringConstants.Composable.VideoPlayerControlPlayPauseButton)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("Back from video player screen")
            .performClick()

        assertTrue(backPressed)
    }

    @Test
    fun videoPlayerScreen_whenDisposed_pausesPlayingPlayer() {
        val fakePlayer = FakePlayer(initialIsPlaying = true)
        var showPlayer by mutableStateOf(true)

        composeTestRule.setContent {
            JetStreamTheme {
                if (showPlayer) {
                    VideoPlayerScreen(
                        uiState =
                            VideoPlayerScreenUiState.Done(
                                nowPlayingInfo = testNowPlayingInfo,
                                player = fakePlayer.instance,
                                isReadyToPlay = true,
                            ),
                        onBackPressed = {},
                    )
                }
            }
        }

        assertTrue(fakePlayer.isPlayingState)

        composeTestRule.runOnIdle {
            showPlayer = false
        }
        composeTestRule.waitForIdle()

        assertFalse(fakePlayer.isPlayingState)
        assertTrue(fakePlayer.pauseCallCount >= 1)
    }
}
