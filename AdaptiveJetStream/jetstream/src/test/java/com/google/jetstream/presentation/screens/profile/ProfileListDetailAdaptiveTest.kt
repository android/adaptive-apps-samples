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

package com.google.jetstream.presentation.screens.profile

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.ui.NavDisplay
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.Navigator
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.app.PresentationType
import com.google.jetstream.presentation.rememberNavigator
import com.google.jetstream.presentation.screens.profile.section.AboutSection
import com.google.jetstream.presentation.screens.profile.section.AccountsSection
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@RunWith(RobolectricTestRunner::class)
class ProfileListDetailAdaptiveTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    @Config(qualifiers = "w400dp-h800dp")
    fun compactWidth_displaysSinglePaneAndNavigatesToDetail() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(400.dp, 800.dp)),
            ) {
                JetStreamTheme {
                    ProfileListDetailTestHost()
                }
            }
        }

        // In compact width, only the Profile list pane is visible initially
        composeTestRule.onNodeWithText("Accounts").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.AboutSectionTitle)
            .assertDoesNotExist()

        // Clicking Accounts navigates to the Accounts detail pane
        composeTestRule.onNodeWithText("Accounts").performClick()

        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsTitle)
            .assertIsDisplayed()
    }

    @Test
    @Config(qualifiers = "w1280dp-h800dp")
    fun expandedWidth_displaysListAndDetailPanesSimultaneously() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(1280.dp, 800.dp)),
            ) {
                JetStreamTheme {
                    ProfileListDetailTestHost()
                }
            }
        }

        // In expanded width, both ProfileScreen and the detail placeholder (AboutSection) are visible
        composeTestRule.onNodeWithText("Accounts").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.AboutSectionTitle)
            .assertIsDisplayed()

        // Selecting Accounts updates the detail pane while keeping the list pane visible
        composeTestRule.onNodeWithText("Accounts").performClick()

        composeTestRule.onNodeWithText("Accounts").assertIsDisplayed()
        composeTestRule
            .onNodeWithText(StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsTitle)
            .assertIsDisplayed()
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun ProfileListDetailTestHost(
    navigator: Navigator = rememberNavigator(initialDestination = Destination.Profile),
) {
    NavDisplay(
        backStack = navigator.backStack,
        sceneStrategies = listOf(rememberListDetailSceneStrategy()),
        entryProvider =
            entryProvider<NavKey> {
                entry<Destination.Profile>(
                    metadata =
                        metadata {
                            put(PresentationType.PresentationTypeKey, PresentationType.ListDetailParent)
                        } +
                            ListDetailSceneStrategy.listPane(
                                detailPlaceholder = {
                                    AboutSection()
                                },
                            ),
                ) {
                    ProfileScreen(onDestinationSelected = navigator::navigate)
                }

                entry<Destination.About>(
                    metadata =
                        metadata {
                            put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
                        } + ListDetailSceneStrategy.detailPane(),
                ) {
                    AboutSection()
                }

                entry<Destination.Accounts>(
                    metadata =
                        metadata {
                            put(PresentationType.PresentationTypeKey, PresentationType.ListDetailChild)
                        } + ListDetailSceneStrategy.detailPane(),
                ) {
                    AccountsSection()
                }
            },
    )
}
