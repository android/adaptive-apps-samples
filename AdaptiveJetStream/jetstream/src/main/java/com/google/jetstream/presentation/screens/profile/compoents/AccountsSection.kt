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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.theme.JetStreamTheme
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding

@Immutable
data class AccountsSectionData(
    val title: String,
    val value: String? = null,
    val onClick: () -> Unit = {}
)

@TvPreview
@Composable
fun AccountsSection(
    numberOfColumns: Int = 2,
    contentPadding: Padding = LocalContentPadding.current
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val accountsSectionListItems = remember {
        listOf(
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionSwitchAccountsTitle,
                value = StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsEmail
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionLogOut,
                value = StringConstants.Composable.Placeholders.AccountsSelectionSwitchAccountsEmail
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionChangePasswordTitle,
                value = StringConstants.Composable.Placeholders.AccountsSelectionChangePasswordValue
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionAddNewAccountTitle,
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionViewSubscriptionsTitle
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionDeleteAccountTitle,
                onClick = { showDeleteDialog = true }
            )
        )
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = contentPadding.start),
        columns = GridCells.Fixed(numberOfColumns),
        content = {
            val itemModifier = if (numberOfColumns == 2) {
                Modifier
                    .focusRequester(focusRequester)
                    .padding(8.dp)
                    .fillMaxWidth()
                    .aspectRatio(2f)
            } else {
                Modifier
            }
            items(accountsSectionListItems.size) { index ->
                AccountsSelectionItem(
                    modifier = itemModifier,
                    key = index,
                    accountsSectionData = accountsSectionListItems[index],
                    isExpanded = numberOfColumns == 2
                )
            }
        }
    )

    AccountsSectionDeleteDialog(
        showDialog = showDeleteDialog,
        onDismissRequest = { showDeleteDialog = false },
        modifier = Modifier.width(428.dp)
    )
}

@PhonePreview
@FoldablePreview
@Composable
private fun SingleColumnAccountPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            AccountsSection(numberOfColumns = 1)
        }
    }
}
