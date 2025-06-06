/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.supportingpanecompose.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDragHandle
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.defaultDragHandleSemantics
import androidx.compose.material3.adaptive.layout.rememberPaneExpansionState
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.supportingpanecompose.R
import kotlinx.coroutines.launch

// Create some simple sample data
private val data = mapOf(
    "android" to listOf("kotlin", "java", "flutter"),
    "kotlin" to listOf("backend", "android", "desktop"),
    "desktop" to listOf("kotlin", "java", "flutter"),
    "backend" to listOf("kotlin", "java"),
    "java" to listOf("backend", "android", "desktop"),
    "flutter" to listOf("android", "desktop")
)

@ExperimentalMaterial3AdaptiveApi
@Composable
fun SupportingPaneSample(modifier: Modifier = Modifier) {
    var selectedTopic: String by rememberSaveable { mutableStateOf(data.keys.first()) }
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var preferredWidthInDp by rememberSaveable { mutableIntStateOf(300) }

    BackHandler(enabled = navigator.canNavigateBack()) {
        scope.launch {
            navigator.navigateBack()
        }
    }

    SupportingPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        modifier = modifier,
        supportingPane = {
            AnimatedPane(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .preferredWidth(preferredWidthInDp.dp)
            ) {
                Column {
                    Text(
                        stringResource(R.string.related_content_label),
                        modifier = Modifier.padding(vertical = 16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )

                    LazyColumn {
                        items(
                            data.getValue(selectedTopic),
                            key = { it }
                        ) { relatedTopic ->
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(all = 4.dp)
                                    .clickable {
                                        selectedTopic = relatedTopic
                                        if (navigator.canNavigateBack()) {
                                            scope.launch {
                                                navigator.navigateBack()
                                            }
                                        }
                                    }
                            ) {
                                Text(
                                    text = relatedTopic,
                                    modifier = Modifier
                                )
                            }
                        }
                    }
                }
            }
        }, mainPane = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    stringResource(R.string.main_content_label),
                    style = MaterialTheme.typography.titleLarge
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                        .clickable {
                            scope.launch {
                                navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = selectedTopic,
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

                Text(
                    text = stringResource(R.string.preferred_width_label),
                    modifier = modifier
                        .padding(top = 48.dp)
                        .padding(horizontal = 24.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Slider(
                    value = preferredWidthInDp.toFloat(),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .padding(horizontal = 24.dp),
                    valueRange = (250f..450f),
                    onValueChange = { preferredWidthInDp = it.toInt() }
                )
                Text(text = preferredWidthInDp.toString())
            }
        },
        paneExpansionState = rememberPaneExpansionState(navigator.scaffoldValue),
        paneExpansionDragHandle = { state ->
            val interactionSource =
                remember { MutableInteractionSource() }
            VerticalDragHandle(
                modifier =
                    Modifier.paneExpansionDraggable(
                        state,
                        LocalMinimumInteractiveComponentSize.current,
                        interactionSource, state.defaultDragHandleSemantics()
                    ), interactionSource = interactionSource
            )
        })
}
