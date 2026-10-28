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

package com.google.jetstream.presentation.screens.categories

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.data.repositories.FakeMovieRepository
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.JetStreamTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(qualifiers = "w1280dp-h800dp")
class CategoriesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testCategories =
        listOf(
            MovieCategory(id = "cat-1", name = "Action"),
            MovieCategory(id = "cat-2", name = "Comedy"),
            MovieCategory(id = "cat-3", name = "Sci-Fi"),
        )

    @Test
    fun categoriesScreen_whenReady_displaysCategories() {
        val repository = FakeMovieRepository()
        repository.setCategories(testCategories)
        val viewModel = CategoriesScreenViewModel(repository)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalEngagementMode provides EngagementMode.Workstation()) {
                JetStreamTheme {
                    CategoriesScreen(
                        onCategoryClick = {},
                        onScroll = {},
                        categoriesScreenViewModel = viewModel,
                    )
                }
            }
        }

        testCategories.forEach { category ->
            composeTestRule.onNodeWithText(category.name).assertIsDisplayed()
        }
    }

    @Test
    fun catalog_categoryCardClick_invokesOnCategoryClick() {
        var clickedCategoryId: String? = null

        composeTestRule.setContent {
            CompositionLocalProvider(LocalEngagementMode provides EngagementMode.Workstation()) {
                JetStreamTheme {
                    Catalog(
                        movieCategories = testCategories,
                        onCategoryClick = { clickedCategoryId = it },
                        onScroll = {},
                    )
                }
            }
        }

        composeTestRule.onNodeWithText("Comedy").performClick()

        assertEquals("cat-2", clickedCategoryId)
    }
}
