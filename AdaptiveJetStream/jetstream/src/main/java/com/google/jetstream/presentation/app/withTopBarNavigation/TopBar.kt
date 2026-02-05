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

package com.google.jetstream.presentation.app.withTopBarNavigation

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.app.JetStreamLogo
import com.google.jetstream.presentation.app.UserAvatar
import com.google.jetstream.presentation.components.feature.isDpadAvailable
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.Screens

@Composable
internal fun TopBar(
    items: List<Screens>,
    selectedScreen: Screens,
    showScreen: (Screens) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val (tabRow, avatar) = remember { FocusRequester.createRefs() }

    val isDpadAvailable = isDpadAvailable()

    // TODO: Is this a bug?
    // If I run the app on the TV emulator, nothing happens when I click on the top navigation items
    val onClickHandler: (Screens) -> Unit = remember(isDpadAvailable) {
        if (isDpadAvailable) {
            { focusManager.moveFocus(FocusDirection.Down) }
        } else {
            { showScreen(it) }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .focusProperties {
                onEnter = {
                    when (selectedScreen) {
                        Screens.Profile -> avatar.tryRequestFocus()
                        else -> tabRow.tryRequestFocus()
                    }
                }
            }
            .focusGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.padding(8.dp)) {
            UserAvatar(
                modifier = Modifier
                    .size(32.dp)
                    .semantics {
                        contentDescription =
                            StringConstants.Composable.ContentDescription.UserAvatar
                    }
                    .focusRequester(avatar),
                selected = selectedScreen == Screens.Profile,
                onClick = { showScreen(Screens.Profile) }
            )
        }
        TopBarTabRow(
            tabs = items,
            selectedScreen = selectedScreen,
            onClick = onClickHandler,
            onTabSelected = showScreen,
            modifier = Modifier
                .weight(1f)
                .focusRequester(tabRow)
        )
        Spacer(modifier.weight(0.1f))
        JetStreamLogo(
            modifier = Modifier
                .alpha(0.75f)
                .padding(end = 8.dp),
        )
    }
}

@Composable
private fun TopBarTabRow(
    tabs: List<Screens>,
    selectedScreen: Screens,
    onTabSelected: (Screens) -> Unit,
    onClick: (Screens) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = remember(tabs) {
        tabs.map { it to FocusRequester() }
    }

    var selectedScreenIndex by rememberSaveable(tabs) {
        mutableIntStateOf(selectedTabIndex(tabs, selectedScreen, 0))
    }

    selectedScreenIndex = selectedTabIndex(tabs, selectedScreen, selectedScreenIndex)
    PrimaryTabRow(
        selectedTabIndex = selectedScreenIndex,
        divider = {},
        modifier = modifier
            .focusProperties {
                onEnter = {
                    if (selectedScreenIndex < items.size) {
                        items[selectedScreenIndex].second.tryRequestFocus()
                    } else {
                        items[0].second.tryRequestFocus()
                    }
                }
            }
            .focusGroup()
    ) {
        items.forEach { (screen, focusRequester) ->
            key(screen) {
                TopBarTab(
                    screen = screen,
                    selected = selectedScreen == screen,
                    onClick = {
                        onClick(screen)
                              },
                    onSelect = { onTabSelected(screen) },
                    modifier = Modifier.focusRequester(focusRequester)
                )
            }
        }
    }
}

private fun selectedTabIndex(
    tabs: List<Screens>,
    selectedScreen: Screens,
    previouslySelectedTabIndex: Int
): Int {
    val index = tabs.indexOf(selectedScreen) % tabs.size
    return when {
        index < 0 -> previouslySelectedTabIndex
        else -> index
    }
}

@Composable
private fun TopBarTab(
    screen: Screens,
    onClick: () -> Unit,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    Tab(
        selected = selected,
        modifier = Modifier
            .onFocusChanged {
                if (it.isFocused) {
                    onSelect()
                }
            }
            .then(modifier),
        onClick = onClick,
    ) {
        TopBarTabContent(screen)
    }
}

@Composable
private fun TopBarTabContent(
    screen: Screens,
    modifier: Modifier = Modifier
) {
    if (screen.tabIcon != null) {
        Icon(
            screen.tabIcon,
            modifier = Modifier
                .padding(4.dp)
                .then(modifier),
            contentDescription = StringConstants.Composable
                .ContentDescription.DashboardSearchButton,
            tint = LocalContentColor.current,
        )
    } else {
        Text(
            modifier = modifier,
            text = screen.name,
            style = MaterialTheme.typography.titleSmall.copy(
                color = LocalContentColor.current
            )
        )
    }
}
