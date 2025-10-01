package com.google.sample.adaptivenavigationsample

/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.google.sample.adaptivenavigationsample.ui.theme.AdaptiveNavigationSampleTheme

enum class AppDestinations(
    @StringRes val label: Int,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    @StringRes val contentDescription: Int
) {
    HOME(R.string.home, Icons.Outlined.Home, Icons.Filled.Home, R.string.home), FAVORITES(
        R.string.favorites, Icons.Outlined.Favorite, Icons.Filled.Favorite, R.string.favorites
    ),
    SHOPPING(
        R.string.shopping, Icons.Outlined.ShoppingCart, Icons.Filled.ShoppingCart, R.string.shopping
    ),
    PROFILE(
        R.string.profile, Icons.Outlined.AccountBox, Icons.Filled.AccountBox, R.string.profile
    ),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AdaptiveNavigationSampleTheme {
                NavigationSample()
            }
        }
    }
}

@Composable
fun NavigationSample() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val options = listOf(
        "Top" to Arrangement.Top, "Center" to Arrangement.Center, "Bottom" to Arrangement.Bottom
    )

    val (verticalArrangement, onOptionSelected) = remember { mutableStateOf(options[1]) }

    NavigationSuiteScaffold(
        modifier = Modifier.fillMaxSize(),
        navigationItems = {
            AppDestinations.entries.forEachIndexed { index, item ->
                NavigationSuiteItem(
                    icon = {
                        val vector = if (index == selectedItem) {
                            item.iconSelected
                        } else {
                            item.icon
                        }
                        Icon(
                            vector, contentDescription = stringResource(item.contentDescription)
                        )
                    },
                    label = { Text(stringResource(item.label)) },
                    selected = index == selectedItem,
                    onClick = { selectedItem = index })
            }
        },
        navigationItemVerticalArrangement = verticalArrangement.second
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("NavRail icons arrangement", style = MaterialTheme.typography.headlineSmall)
            options.forEach { item ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (item == verticalArrangement), onClick = {
                                onOptionSelected(item)
                            }, role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (item == verticalArrangement), onClick = null
                    )
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun NavigationSamplePreview() {
    AdaptiveNavigationSampleTheme {
        NavigationSample()
    }
}