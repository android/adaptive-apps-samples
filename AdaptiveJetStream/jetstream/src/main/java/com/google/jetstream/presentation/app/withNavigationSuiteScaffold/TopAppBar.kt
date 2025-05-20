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

package com.google.jetstream.presentation.app.withNavigationSuiteScaffold

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import com.google.jetstream.presentation.app.JetStreamLogo
import com.google.jetstream.presentation.app.UserAvatar
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.Screens

@Composable
fun TopAppBar(
    selectedScreen: Screens,
    showScreen: (Screens) -> Unit,
    modifier: Modifier = Modifier,
) {
    val (avatar, search) = remember { FocusRequester.createRefs() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .focusProperties {
                onEnter = {
                    when (selectedScreen) {
                        Screens.Profile -> {
                            avatar.tryRequestFocus()
                        }

                        Screens.Search -> {
                            search.tryRequestFocus()
                        }

                        else -> {}
                    }
                }
            }
            .focusGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        JetStreamLogo(
            modifier = Modifier
                .alpha(0.75f)
                .padding(start = 8.dp),
        )
        Spacer(modifier.weight(1f))
        SearchButton(
            modifier = Modifier.focusRequester(search),
            onClick = { showScreen(Screens.Search) }
        )
        UserAvatar(
            modifier = Modifier.focusRequester(avatar),
            selected = selectedScreen == Screens.Profile,
            onClick = { showScreen(Screens.Profile) }
        )
    }
}
