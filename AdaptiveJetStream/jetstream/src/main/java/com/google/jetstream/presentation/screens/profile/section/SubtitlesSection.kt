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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.screens.profile.compoents.ProfileSectionTitle
import com.google.jetstream.presentation.screens.profile.compoents.SettingItem
import com.google.jetstream.presentation.theme.LocalContentPadding

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun SubtitlesSection(
    isSubtitlesChecked: Boolean,
    onSubtitleCheckChange: (isChecked: Boolean) -> Unit,
) {
    val contentPadding = LocalContentPadding.current

    Column(
        modifier =
            Modifier.styleable {
                contentPaddingStart(contentPadding.start)
                contentPaddingEnd(contentPadding.end)
                contentPaddingTop(contentPadding.top)
                contentPaddingBottom(contentPadding.bottom)
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileSectionTitle(
            title = StringConstants.Composable.Placeholders.SubtitlesSectionTitle,
        )
        SubtitleSwitch(isEnabled = isSubtitlesChecked, onValueChange = onSubtitleCheckChange)
        SettingItem(
            name = StringConstants.Composable.Placeholders.SubtitlesSectionLanguageItem,
            value = StringConstants.Composable.Placeholders.SubtitlesSectionLanguageValue,
        )
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun SubtitleSwitch(
    isEnabled: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingItem(
        name = StringConstants.Composable.Placeholders.SubtitlesSectionSubtitlesItem,
        value = {
            Switch(
                checked = isEnabled,
                onCheckedChange = null,
                colors =
                    SwitchDefaults.colors(
                        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
                        checkedThumbColor = MaterialTheme.colorScheme.surface,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onSurface,
                        checkedTrackColor = MaterialTheme.colorScheme.onSurface,
                        uncheckedBorderColor = MaterialTheme.colorScheme.surface,
                        checkedBorderColor = MaterialTheme.colorScheme.surface,
                    ),
            )
        },
        modifier =
            modifier
                .clickable {
                    onValueChange(!isEnabled)
                }
                .semantics {
                    role = Role.Button
                },
    )
}
