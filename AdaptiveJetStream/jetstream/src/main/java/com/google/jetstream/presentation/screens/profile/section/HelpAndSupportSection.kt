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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.data.util.StringConstants.Composable.Placeholders.HelpAndSupportSectionContactItem
import com.google.jetstream.data.util.StringConstants.Composable.Placeholders.HelpAndSupportSectionFAQItem
import com.google.jetstream.data.util.StringConstants.Composable.Placeholders.HelpAndSupportSectionPrivacyItem
import com.google.jetstream.data.util.StringConstants.Composable.Placeholders.HelpAndSupportSectionTitle
import com.google.jetstream.presentation.screens.profile.compoents.ProfileSectionTitle
import com.google.jetstream.presentation.screens.profile.compoents.SettingItem
import com.google.jetstream.presentation.theme.LocalContentPadding

@Composable
fun HelpAndSupportSection() {
    Column(
        modifier = Modifier.padding(LocalContentPadding.current.intoPaddingValues()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        ProfileSectionTitle(title = HelpAndSupportSectionTitle)
        HelpAndSupportSectionItem(name = HelpAndSupportSectionFAQItem)
        HelpAndSupportSectionItem(name = HelpAndSupportSectionPrivacyItem)
        HelpAndSupportSectionItem(name = HelpAndSupportSectionContactItem)
    }
}

@Composable
private fun HelpAndSupportSectionItem(
    name: String,
    modifier: Modifier = Modifier,
) {
    SettingItem(
        name = name,
        value = {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription =
                    StringConstants
                        .Composable
                        .Placeholders
                        .HelpAndSupportSectionListItemIconDescription,
            )
        },
        modifier = modifier,
    )
}
