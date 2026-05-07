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

package com.google.jetstream.presentation.screens.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.MutableStyleState
import androidx.compose.foundation.style.Style
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.focused
import androidx.compose.foundation.style.hovered
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.google.jetstream.R
import com.google.jetstream.presentation.app.Destination
import com.google.jetstream.presentation.app.NavigationItem
import com.google.jetstream.presentation.components.shim.indication.borderIndication
import com.google.jetstream.presentation.components.shim.indication.scaleIndication
import com.google.jetstream.presentation.theme.LocalContentPadding

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun ProfileScreen(
    onDestinationSelected: (Destination) -> Unit,
) {
    LazyColumn(
        contentPadding = LocalContentPadding.current.intoPaddingValues(),
    ) {
        items(NavigationItem.ProfileSettings) { item ->
            SectionListItem(
                navigationItem = item,
                onClick = {
                    onDestinationSelected(item.destination)
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
private fun SectionListItem(
    navigationItem: NavigationItem,
    modifier: Modifier = Modifier,
    style: Style = Style,
    onClick: () -> Unit = {},
) {
    val icon = navigationItem.icon
    val name = navigationItem.name
    val textStyle =
        MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
        )
    val tintColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)

    val interactionSource =
        remember(navigationItem) {
            MutableInteractionSource()
        }
    val styleState =
        remember(interactionSource) {
            MutableStyleState(interactionSource = interactionSource)
        }

    val defaultStyle =
        remember {
            Style {
                fillWidth()
                borderIndication()
                scaleIndication()

                focused {
                    foreground(tintColor)
                }
                hovered {
                    foreground(tintColor)
                }
            }
        }

    ListItem(
        trailingContent = {
            Icon(
                painter = icon,
                modifier =
                    Modifier.styleable {
                        externalPaddingTop(2.dp)
                        externalPaddingBottom(2.dp)
                        externalPaddingStart(4.dp)
                        size(20.dp)
                    },
                contentDescription =
                    stringResource(
                        id = R.string.profile_screen_listItem_icon_content_description,
                        name,
                    ),
            )
        },
        headlineContent = {
            Text(
                text = name,
                style = textStyle,
                modifier =
                    Modifier.styleable {
                        fillWidth()
                        textStyle(textStyle)
                    },
            )
        },
        modifier =
            modifier
                .styleable(
                    styleState = styleState,
                    defaultStyle,
                    style,
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = dropUnlessResumed(block = onClick),
                )
                .semantics {
                    role = Role.Button
                },
    )
}
