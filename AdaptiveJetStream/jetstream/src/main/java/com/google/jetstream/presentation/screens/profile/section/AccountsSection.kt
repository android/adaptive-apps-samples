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

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.screens.profile.compoents.AccountsSectionDeleteDialog
import com.google.jetstream.presentation.screens.profile.compoents.AccountsSelectionItem
import com.google.jetstream.presentation.theme.LocalContentPadding

@Immutable
data class AccountsSectionData(
    val title: String,
    val value: String? = null,
    val onClick: (() -> Unit)? = null,
)

@OptIn(ExperimentalFoundationStyleApi::class)
@TvPreview
@Composable
fun AccountsSection() {
    var showDeleteDialog by remember { mutableStateOf(false) }

    // ToDo: Move the data definition outside of the composable
    val accountsSectionListItems =
        remember {
            listOf(
                AccountsSectionData(
                    title =
                        StringConstants.Composable.Placeholders
                            .AccountsSelectionSwitchAccountsTitle,
                    value = StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsEmail,
                ),
                AccountsSectionData(
                    title = StringConstants.Composable.Placeholders.AccountsSelectionLogOut,
                    value = StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsEmail,
                ),
                AccountsSectionData(
                    title =
                        StringConstants.Composable.Placeholders
                            .AccountsSelectionChangePasswordTitle,
                    value = StringConstants.Composable.Placeholders.AccountsSelectionChangePasswordValue,
                ),
                AccountsSectionData(
                    title = StringConstants.Composable.Placeholders.AccountsSelectionAddNewAccountTitle,
                ),
                AccountsSectionData(
                    title =
                        StringConstants.Composable.Placeholders
                            .AccountsSelectionViewSubscriptionsTitle,
                ),
                AccountsSectionData(
                    title = StringConstants.Composable.Placeholders.AccountsSelectionDeleteAccountTitle,
                    onClick = { showDeleteDialog = true },
                ),
            )
        }
    LazyColumn(
        contentPadding = LocalContentPadding.current.intoPaddingValues(),
    ) {
        items(accountsSectionListItems) { data ->
            AccountsSelectionItem(
                modifier =
                    Modifier.styleable {
                        fillWidth()
                        externalPadding(8.dp)
                    },
                accountsSectionData = data,
            )
        }
    }

    AccountsSectionDeleteDialog(
        showDialog = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false },
        modifier = Modifier.width(428.dp),
    )
}
