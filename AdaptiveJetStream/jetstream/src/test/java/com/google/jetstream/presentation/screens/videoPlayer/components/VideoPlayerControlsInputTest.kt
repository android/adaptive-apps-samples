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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.pressKey
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.data.entities.StereoscopicVisionType
import com.google.jetstream.presentation.screens.videoPlayer.FakePlayer
import com.google.jetstream.presentation.screens.videoPlayer.NowPlayingInfo
import com.google.jetstream.presentation.screens.videoPlayer.VideoPlayerScreenContent
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalTestApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class VideoPlayerControlsInputTest {
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
    fun keyboardShortcuts_kJLEscape_controlPlaybackAndBackNavigation() {
        val fakePlayer = FakePlayer(initialIsPlaying = true)
        var backPressed = false

        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerScreenContent(
                    nowPlayingInfo = testNowPlayingInfo,
                    player = fakePlayer.instance,
                    isReadyToPlay = true,
                    onBackPressed = { backPressed = true },
                )
            }
        }

        assertTrue(fakePlayer.isPlayingState)

        // K pauses playback
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.K)
        }
        composeTestRule.waitForIdle()
        assertFalse(fakePlayer.isPlayingState)

        // K resumes playback
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.K)
        }
        composeTestRule.waitForIdle()
        assertTrue(fakePlayer.isPlayingState)

        // L seeks forward 10 seconds
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.L)
        }
        composeTestRule.waitForIdle()
        assertEquals(1, fakePlayer.seekForwardCallCount)
        assertEquals(10_000L, fakePlayer.currentPositionMs)

        // J seeks backward 10 seconds
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.J)
        }
        composeTestRule.waitForIdle()
        assertEquals(1, fakePlayer.seekBackCallCount)
        assertEquals(0L, fakePlayer.currentPositionMs)

        // Escape triggers onBackPressed
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.Escape)
        }
        composeTestRule.waitForIdle()
        assertTrue(backPressed)
    }

    @Test
    fun spaceBarAndDpadSeek_controlPlaybackAndSeeker() {
        val fakePlayer = FakePlayer(initialIsPlaying = true)

        composeTestRule.setContent {
            JetStreamTheme {
                VideoPlayerScreenContent(
                    nowPlayingInfo = testNowPlayingInfo,
                    player = fakePlayer.instance,
                    isReadyToPlay = true,
                    onBackPressed = {},
                )
            }
        }

        // Space bar pauses playback
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.Spacebar)
        }
        composeTestRule.waitForIdle()
        assertFalse(fakePlayer.isPlayingState)

        // Space bar resumes playback
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.Spacebar)
        }
        composeTestRule.waitForIdle()
        assertTrue(fakePlayer.isPlayingState)

        // D-Pad Center on the focused seeker indicator enters seek mode,
        // D-Pad Right advances seek progress by 10%, and D-Pad Center confirms the seek
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.DirectionCenter)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.DirectionRight)
        }
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().performKeyInput {
            pressKey(Key.DirectionCenter)
        }
        composeTestRule.waitForIdle()
        assertEquals(12_000L, fakePlayer.currentPositionMs)
    }
}
