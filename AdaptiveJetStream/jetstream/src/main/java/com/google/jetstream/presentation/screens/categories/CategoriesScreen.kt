/*
 * Copyright 2023 Google LLC
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

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.jetstream.data.entities.MovieCategory
import com.google.jetstream.data.entities.MovieCategoryList
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.JetStreamPreview
import com.google.jetstream.presentation.components.Loading
import com.google.jetstream.presentation.components.MovieCard
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.components.mockCategoryScreenState
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.LocalListItemGap
import com.google.jetstream.presentation.theme.Padding

val LocalCategoryCardAspectRatio: ProvidableCompositionLocal<Float> = staticCompositionLocalOf {
    1.7777f // 16:9
}
val LocalCategoryGridGridCells: ProvidableCompositionLocal<GridCells> = staticCompositionLocalOf {
    GridCells.Fixed(4)
}

@Composable
fun CategoriesScreen(
    onCategoryClick: (categoryId: String) -> Unit = {},
    onScroll: (isTopBarVisible: Boolean) -> Unit = {},
    categoriesScreenViewModel: CategoriesScreenViewModel = hiltViewModel()
) {
    val uiState by categoriesScreenViewModel.uiState.collectAsStateWithLifecycle()
    when (val s = uiState) {
        CategoriesScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is CategoriesScreenUiState.Ready -> {
            Catalog(
                movieCategories = s.categoryList,
                onCategoryClick = onCategoryClick,
                onScroll = onScroll,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun Catalog(
    movieCategories: MovieCategoryList,
    modifier: Modifier = Modifier,
    gridCells: GridCells = LocalCategoryGridGridCells.current,
    contentPadding: Padding = LocalContentPadding.current,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
) {
    val lazyGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0 &&
                lazyGridState.firstVisibleItemScrollOffset < 100
        }
    }
    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    LazyVerticalGrid(
        state = lazyGridState,
        modifier = modifier,
        contentPadding = contentPadding.intoPaddingValues(),
        columns = gridCells,
        horizontalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
        verticalArrangement = Arrangement.spacedBy(LocalListItemGap.current),
    ) {
        items(movieCategories) { movieCategory ->
            CategoryCard(
                movieCategory = movieCategory,
                onCategoryClick = onCategoryClick,
                modifier = Modifier.aspectRatio(LocalCategoryCardAspectRatio.current)
            )
        }
    }
}

@Composable
private fun CategoryCard(
    movieCategory: MovieCategory,
    onCategoryClick: (categoryId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val itemAlpha by animateFloatAsState(
        targetValue = if (isFocused) .6f else 0.2f,
        label = ""
    )

    MovieCard(
        onClick = {
            onCategoryClick(movieCategory.id)
        },
        interactionSource = interactionSource,
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.alpha(itemAlpha)) {
                GradientBg()
            }
            Text(
                text = movieCategory.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@PhonePreview
@Composable
private fun CategoriesScreenPhonePreview() {
    JetStreamPreview {
        CompositionLocalProvider(
            LocalCategoryGridGridCells provides GridCells.Fixed(3),
            LocalCategoryCardAspectRatio provides 1f,
            LocalListItemGap provides 8.dp,
        ) {
            Catalog(
                movieCategories = mockCategoryScreenState.categoryList,
                onCategoryClick = {},
                onScroll = {}
            )
        }
    }
}

@FoldablePreview
@Composable
private fun CategoriesScreenFoldablePreview() {
    JetStreamPreview {
        CompositionLocalProvider(
            LocalCategoryGridGridCells provides GridCells.Fixed(3),
            LocalCategoryCardAspectRatio provides 1.5f,
            LocalListItemGap provides 8.dp,
        ) {
            Catalog(
                movieCategories = mockCategoryScreenState.categoryList,
                onCategoryClick = {},
                onScroll = {}
            )
        }
    }
}

@TvPreview
@Composable
private fun CategoriesScreenTvPreview() {
    JetStreamPreview {
        Catalog(
            movieCategories = mockCategoryScreenState.categoryList,
            onCategoryClick = {},
            onScroll = {}
        )
    }
}
