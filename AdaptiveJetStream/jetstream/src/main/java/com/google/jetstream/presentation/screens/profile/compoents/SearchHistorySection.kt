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

package com.google.jetstream.presentation.screens.profile.compoents

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.theme.JetStreamTheme

@Composable
fun SearchHistorySection(isExpanded: Boolean = true) {
    val padding = if (isExpanded) 72.dp else 16.dp
    with(StringConstants.Composable.Placeholders) {
        LazyColumn(modifier = Modifier.padding(horizontal = padding)) {
            if (isExpanded) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = SearchHistorySectionTitle,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        ClearHistoryButton()
                    }
                }
            }
            items(SampleSearchHistory.size) { index ->
                ListItem(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { },
                    headlineContent = {
                        Text(
                            text = SampleSearchHistory[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                )
            }
            if (!isExpanded) {
                item {
                    ClearHistoryButton(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun ClearHistoryButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Clear search history */ },
        modifier = modifier.clickable { }
    ) {
        Text(text = StringConstants.Composable.Placeholders.SearchHistoryClearAll)
    }
}

@PhonePreview
@FoldablePreview
@Composable
fun SearchHistorySectionCompactPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            SearchHistorySection(isExpanded = false)
        }
    }
}

@TvPreview
@Composable
fun SearchHistorySectionExpandedPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            SearchHistorySection(isExpanded = true)
        }
    }
}
