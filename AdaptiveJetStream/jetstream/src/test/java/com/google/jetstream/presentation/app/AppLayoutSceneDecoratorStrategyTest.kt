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

package com.google.jetstream.presentation.app

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.presentation.Navigator
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.rememberNavigator
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class AppLayoutSceneDecoratorStrategyTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun compactAndMediumModes_displayNavigationAndSubNavigation() {
        var mode by mutableStateOf<EngagementMode>(EngagementMode.Compact())
        var selectedNav: JetStreamAppNavigation? = null

        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(LocalEngagementMode provides mode) {
                    selectedNav = selectAppNavigation()
                    DecoratedTestHost(initialDestination = Destination.Home)
                }
            }
        }

        // Compact mode: uses DefaultNavigation and shows both Navigation and SubNavigation
        assertSame(DefaultNavigation, selectedNav)
        composeTestRule.onNodeWithText("PrimaryNavigation").assertIsDisplayed()
        composeTestRule.onNodeWithText("SubNavigation").assertIsDisplayed()
        composeTestRule.onNodeWithText("HomeContent").assertIsDisplayed()

        // Medium mode: uses DefaultNavigation and shows both Navigation and SubNavigation
        composeTestRule.runOnIdle {
            mode = EngagementMode.Medium()
        }
        composeTestRule.waitForIdle()
        assertSame(DefaultNavigation, selectedNav)
        composeTestRule.onNodeWithText("PrimaryNavigation").assertIsDisplayed()
        composeTestRule.onNodeWithText("SubNavigation").assertIsDisplayed()
    }

    @Test
    fun leanbackWorkstationAndCabinModes_displayTopBarNavigationWithoutSubNavigation() {
        val topBarModes =
            listOf(
                EngagementMode.Leanback(),
                EngagementMode.Workstation(),
                EngagementMode.Cabin(hasPointingDevice = true),
            )
        var mode by mutableStateOf<EngagementMode>(topBarModes.first())
        var selectedNav: JetStreamAppNavigation? = null

        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(LocalEngagementMode provides mode) {
                    selectedNav = selectAppNavigation()
                    DecoratedTestHost(initialDestination = Destination.Home)
                }
            }
        }

        for (targetMode in topBarModes) {
            composeTestRule.runOnIdle {
                mode = targetMode
            }
            composeTestRule.waitForIdle()

            assertSame(TopBarNavigation, selectedNav)
            composeTestRule.onNodeWithText("PrimaryNavigation").assertIsDisplayed()
            composeTestRule.onNodeWithText("SubNavigation").assertDoesNotExist()
            composeTestRule.onNodeWithText("HomeContent").assertIsDisplayed()
        }
    }

    @Test
    fun overlayPresentationType_hidesNavigationAndSubNavigation() {
        lateinit var navigator: Navigator

        composeTestRule.setContent {
            JetStreamTheme {
                CompositionLocalProvider(LocalEngagementMode provides EngagementMode.Compact()) {
                    navigator = rememberNavigator(initialDestination = Destination.Home)
                    DecoratedTestHost(navigator = navigator)
                }
            }
        }

        composeTestRule.onNodeWithText("PrimaryNavigation").assertIsDisplayed()
        composeTestRule.onNodeWithText("SubNavigation").assertIsDisplayed()

        composeTestRule.runOnIdle {
            navigator.navigate(Destination.MovieDetails(movieId = "1"))
        }
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("OverlayContent").assertIsDisplayed()
        composeTestRule.onNodeWithText("PrimaryNavigation").assertDoesNotExist()
        composeTestRule.onNodeWithText("SubNavigation").assertDoesNotExist()
    }
}

@Composable
private fun DecoratedTestHost(
    initialDestination: Destination = Destination.Home,
    navigator: Navigator = rememberNavigator(initialDestination = initialDestination),
) {
    NavDisplay(
        backStack = navigator.backStack,
        sceneDecoratorStrategies =
            listOf(
                rememberAppLayoutSceneDecorator(
                    navigation = { Text("PrimaryNavigation") },
                    subNavigation = { Text("SubNavigation") },
                ),
            ),
        entryProvider =
            entryProvider<NavKey> {
                entry<Destination.Home>(
                    metadata =
                        metadata {
                            put(PresentationType.PresentationTypeKey, PresentationType.SinglePane)
                        },
                ) {
                    Text("HomeContent")
                }
                entry<Destination.MovieDetails>(
                    metadata =
                        metadata {
                            put(PresentationType.PresentationTypeKey, PresentationType.Overlay)
                        },
                ) {
                    Text("OverlayContent")
                }
            },
    )
}
