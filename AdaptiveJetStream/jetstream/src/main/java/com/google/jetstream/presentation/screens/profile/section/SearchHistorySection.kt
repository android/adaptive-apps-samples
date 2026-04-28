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

package com.google.jetstream.presentation.screens.profile.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.data.util.StringConstants.Composable.Placeholders.SampleSearchHistory
import com.google.jetstream.presentation.screens.profile.compoents.ProfileSectionTitle
import com.google.jetstream.presentation.screens.profile.compoents.SettingItemValue
import com.google.jetstream.presentation.theme.LocalContentPadding

@Composable
fun SearchHistorySection() {
    LazyColumn(
        contentPadding = LocalContentPadding.current.intoPaddingValues(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            ProfileSectionTitle(
                title = StringConstants.Composable.Placeholders.SearchHistorySectionTitle,
            )
        }
        items(SampleSearchHistory) { history ->
            SettingItemValue(history)
        }
        item {
            ClearHistoryButton()
        }
    }
}

@Composable
private fun ClearHistoryButton(modifier: Modifier = Modifier) {
    Button(
        onClick = { /* Clear search history */ },
        modifier = modifier,
    ) {
        Text(text = StringConstants.Composable.Placeholders.SearchHistoryClearAll)
    }
}
