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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.theme.JetStreamTheme

@Composable
fun HelpAndSupportSection(isExpanded: Boolean = true) {

    val padding = if (isExpanded) 72.dp else 16.dp
    with(StringConstants.Composable.Placeholders) {
        Column(modifier = Modifier.padding(horizontal = padding)) {
            if (isExpanded) {
                Text(
                    text = HelpAndSupportSectionTitle,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            HelpAndSupportSectionItem(title = HelpAndSupportSectionFAQItem)
            HelpAndSupportSectionItem(title = HelpAndSupportSectionPrivacyItem)
            HelpAndSupportSectionItem(
                title = HelpAndSupportSectionContactItem,
                value = HelpAndSupportSectionContactValue,
                isExpanded = isExpanded
            )
        }
    }
}

@Composable
private fun HelpAndSupportSectionItem(
    title: String,
    value: String? = null,
    isExpanded: Boolean = true,
) {
    if (isExpanded || value == null) {
        ListItem(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { },
            trailingContent = {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = StringConstants
                        .Composable
                        .Placeholders
                        .HelpAndSupportSectionListItemIconDescription
                )
            },
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ),
        )
    } else {
        ListItem(
            modifier = Modifier
                .padding(top = 16.dp)
                .clickable { },
            headlineContent = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            },
            supportingContent = {
                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.alpha(0.75f).padding(vertical = 4.dp)
                )
            },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ),
        )
    }
}

@PhonePreview
@FoldablePreview
@Composable
fun HelpSectionCompactPreview() {

    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            HelpAndSupportSection(isExpanded = false)
        }
    }
}

@TvPreview
@Composable
fun HelpSectionExpandedPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            HelpAndSupportSection()
        }
    }
}
