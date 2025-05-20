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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.theme.JetStreamTheme

@Composable
fun LanguageSection(
    selectedIndex: Int = 0,
    isExpanded: Boolean = true,
    onSelectedIndexChange: (currentIndex: Int) -> Unit = {}
) {
    val padding = if (isExpanded) 72.dp else 16.dp
    with(StringConstants.Composable.Placeholders) {
        LazyColumn(modifier = Modifier.padding(horizontal = padding)) {
            if (isExpanded) {
                item {
                    Text(
                        text = LanguageSectionTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            items(LanguageSectionItems.size) { index ->
                ListItem(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple()
                        ) {
                            onSelectedIndexChange(index)
                        },
                    trailingContent = {
                        if (selectedIndex == index) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = stringResource(
                                    id =
                                    R.string.language_section_listItem_icon_content_description,
                                    LanguageSectionItems[index]
                                )
                            )
                        }
                    },
                    headlineContent = {
                        Text(
                            text = LanguageSectionItems[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                )
            }
        }
    }
}

@PhonePreview
@FoldablePreview
@Composable
fun LanguageScreenCompactPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            LanguageSection(isExpanded = false) { }
        }
    }
}

@TvPreview
@Composable
fun LanguageScreenExpandedPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            LanguageSection(isExpanded = true) { }
        }
    }
}
