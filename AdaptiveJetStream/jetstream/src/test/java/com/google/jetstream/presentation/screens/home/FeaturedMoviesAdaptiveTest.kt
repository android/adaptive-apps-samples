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

package com.google.jetstream.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.ComposeUiFlags
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.entities.Movie
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.components.feature.ProvideJetStreamUiMediaContext
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalFoundationStyleApi::class, ExperimentalComposeUiApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class FeaturedMoviesAdaptiveTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        ComposeUiFlags.isMediaQueryIntegrationEnabled = true
    }

    private val testMovies =
        listOf(
            Movie(
                id = "1",
                sources = emptyMap(),
                subtitleUri = null,
                posterUri = "",
                name = "Featured Movie Alpha",
                description = "Alpha Description",
            ),
            Movie(
                id = "2",
                sources = emptyMap(),
                subtitleUri = null,
                posterUri = "",
                name = "Featured Movie Beta",
                description = "Beta Description",
            ),
        )

    @Test
    fun featuredMovieCarouselType_switchesByEngagementMode() {
        var mode by mutableStateOf<EngagementMode>(EngagementMode.Compact())
        var resolvedType: FeaturedMovieCarouselType? = null

        composeTestRule.setContent {
            ProvideJetStreamUiMediaContext {
                CompositionLocalProvider(LocalEngagementMode provides mode) {
                    JetStreamTheme {
                        resolvedType = featuredMovieCarouselType()
                        FeaturedMovies(
                            movieList = testMovies,
                            onMovieSelected = {},
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(324.dp),
                        )
                    }
                }
            }
        }

        // Compact & Medium use Hero carousel
        assertEquals(FeaturedMovieCarouselType.Hero, resolvedType)
        composeTestRule.onNodeWithText("Featured Movie Alpha").assertIsDisplayed()
        composeTestRule
            .onNodeWithContentDescription(StringConstants.Composable.ContentDescription.MoviesCarousel)
            .assertDoesNotExist()

        composeTestRule.runOnIdle {
            mode = EngagementMode.Medium()
        }
        composeTestRule.waitForIdle()
        assertEquals(FeaturedMovieCarouselType.Hero, resolvedType)

        // Leanback, Workstation, and Cabin use Featured carousel
        val featuredModes =
            listOf(
                EngagementMode.Leanback(),
                EngagementMode.Workstation(),
                EngagementMode.Cabin(hasPointingDevice = true),
            )
        for (targetMode in featuredModes) {
            composeTestRule.runOnIdle {
                mode = targetMode
            }
            composeTestRule.waitForIdle()
            assertEquals(FeaturedMovieCarouselType.Featured, resolvedType)
            composeTestRule
                .onNodeWithContentDescription(StringConstants.Composable.ContentDescription.MoviesCarousel)
                .assertIsDisplayed()
            composeTestRule.onNodeWithText("Featured Movie Alpha").assertIsDisplayed()
        }
    }
}
