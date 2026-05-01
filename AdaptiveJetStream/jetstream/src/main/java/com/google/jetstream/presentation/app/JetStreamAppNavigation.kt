/*
 * Copyright 2026 Google LLC
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

package com.google.jetstream.presentation.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.material3.NavigationRail
import androidx.xr.compose.platform.LocalSession
import androidx.xr.scenecore.scene
import com.google.jetstream.R
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.LocalContentPadding

/**
 * Interface defining the navigation components for the JetStream app.
 * Different implementations are used based on the device's engagement mode.
 */
interface JetStreamAppNavigation {
    /**
     * Displays secondary navigation elements like logos, search, and profile buttons.
     */
    @Composable
    fun SubNavigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean = true,
    )

    /**
     * Displays the primary navigation elements to switch between main destinations.
     */
    @Composable
    fun Navigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean = true,
    )
}

/**
 * Default navigation implementation for mobile and tablet devices.
 * It adapts between a horizontal bar and a vertical rail based on engagement mode.
 */
object DefaultNavigation : JetStreamAppNavigation {
    @OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)
    @Composable
    override fun SubNavigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
        val contentPadding = LocalContentPadding.current

        Grid(
            config = {
                row(1f)
                column(GridTrackSize.Auto)
                column(1.fr)
                column(GridTrackSize.Auto)
                column(GridTrackSize.Auto)
            },
            modifier =
                Modifier
                    .padding(start = contentPadding.start, end = contentPadding.end)
                    .height(80.dp),
        ) {
            JetStreamLogo(
                modifier =
                    Modifier
                        .styleable {
                            alpha(0.75f)
                        }
                        .gridItem(alignment = Alignment.Center),
            )
            SearchButton(
                onClick = { onNavigation(Destination.Search) },
                modifier = Modifier.gridItem(column = -2),
            )
            UserAvatar(
                selected = current == Destination.Profile,
                onClick = { onNavigation(Destination.Profile) },
                modifier =
                    Modifier.gridItem(column = -1),
            )
        }
    }

    @OptIn(ExperimentalFlexBoxApi::class, ExperimentalFoundationStyleApi::class)
    @Composable
    override fun Navigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
        val direction =
            when (LocalEngagementMode.current) {
                is EngagementMode.Compact -> FlexDirection.Row
                else -> FlexDirection.Column
            }

        FlexBox(
            config = {
                direction(direction)
                justifyContent(FlexJustifyContent.Center)
                gap(4.dp)
            },
            modifier =
                Modifier.styleable {
                    fillSize()
                },
        ) {
            RootDestinations(
                current = current,
                onNavigation = onNavigation,
            )
            if (LocalEngagementMode.current == EngagementMode.Enclosed) {
                EnableSpatialUiButton()
            }
        }
    }

    @Composable
    fun RootDestinations(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
    ) {
        Destination.RootDestinations.forEach { destination ->
            NavigationRailItem(
                selected = destination == current,
                onClick = { onNavigation(destination) },
                icon = {
                    val painter = destination.icon
                    if (painter != null) {
                        Icon(
                            painter = painter,
                            contentDescription = destination.name,
                            modifier = Modifier.size(24.dp),
                        )
                    }
                },
                label = {
                    Text(text = destination.name)
                },
            )
        }
    }

    @Composable
    fun EnableSpatialUiButton(modifier: Modifier = Modifier) {
        val session = LocalSession.current
        NavigationRailItem(
            selected = false,
            onClick = {
                session?.scene?.requestFullSpaceMode()
            },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_expand_content),
                    modifier = Modifier.size(48.dp),
                    contentDescription = stringResource(R.string.home_space_mode),
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
            label = {
                Text(
                    stringResource(R.string.full_space_mode),
                    color = MaterialTheme.colorScheme.primary,
                )
            },
            modifier = modifier,
        )
    }
}

/**
 * Navigation implementation optimized for Leanback engagement mode, using a top bar.
 */
object TopBarNavigation : JetStreamAppNavigation {
    @Composable
    override fun SubNavigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
    }

    @Composable
    override fun Navigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
        // Slide in from top animation for the top bar
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically { -it },
            exit = slideOutVertically { -it },
        ) {
            Topbar(
                current = current,
                onTabClicked = onNavigation,
                onTabFocused = onNavigation,
            )
        }
    }
}

/**
 * Navigation implementation optimized for the spatial UI.
 */
object SpatialNavigation : JetStreamAppNavigation {
    @Composable
    override fun SubNavigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
        DefaultNavigation.SubNavigation(
            current = current,
            onNavigation = onNavigation,
            isVisible = isVisible,
        )
    }

    @OptIn(ExperimentalMaterial3XrApi::class)
    @Composable
    override fun Navigation(
        current: Destination?,
        onNavigation: (Destination) -> Unit,
        isVisible: Boolean,
    ) {
        NavigationRail {
            DefaultNavigation.RootDestinations(
                current = current,
                onNavigation = onNavigation,
            )
            DisableSpatialUiButton()
        }
    }

    @Composable
    private fun DisableSpatialUiButton(
        modifier: Modifier = Modifier,
    ) {
        val session = LocalSession.current
        if (session != null) {
            NavigationRailItem(
                selected = false,
                onClick = { session.scene.requestHomeSpaceMode() },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_collapse_content),
                        modifier = Modifier.size(48.dp),
                        contentDescription = stringResource(R.string.home_space_mode),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                label = {
                    Text(
                        stringResource(R.string.home_space_mode),
                        color = MaterialTheme.colorScheme.primary,
                    )
                },
                modifier = modifier,
            )
        }
    }
}

/**
 * Selects the appropriate [JetStreamAppNavigation] implementation based on the current engagement mode.
 */
@Composable
fun selectAppNavigation(): JetStreamAppNavigation {
    val engagementMode = LocalEngagementMode.current
    return remember(engagementMode) {
        when (engagementMode) {
            EngagementMode.Spatial -> {
                SpatialNavigation
            }

            EngagementMode.Leanback, EngagementMode.Cabin, is EngagementMode.Workstation -> {
                TopBarNavigation
            }

            else -> {
                DefaultNavigation
            }
        }
    }
}

/**
 * Composable representing the top navigation bar used in Leanback/TV layouts.
 * It displays the user profile, main navigation tabs, and the app logo in a grid.
 */
@OptIn(
    ExperimentalFlexBoxApi::class,
    ExperimentalFoundationStyleApi::class,
    ExperimentalGridApi::class,
)
@Composable
private fun Topbar(
    current: Destination?,
    onTabFocused: (Destination) -> Unit,
    onTabClicked: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentPadding = LocalContentPadding.current
    val focusRequester = remember { FocusRequester() }

    Grid(
        config = {
            row(1f)
            column(GridTrackSize.Auto)
            column(GridTrackSize.MinMax(100.dp, 1.fr))
            column(0.2.fr)
            column(GridTrackSize.Auto)
            gap(8.dp)
        },
        modifier =
            modifier
                .styleable {
                    contentPaddingStart(contentPadding.start)
                    contentPaddingEnd(contentPadding.end)
                }
                .focusRestorer(fallback = focusRequester)
                .focusGroup(),
    ) {
        UserAvatar(
            selected = current == Destination.Profile,
            onClick = { onTabFocused(Destination.Profile) },
            modifier =
                Modifier
                    .styleable {
                        size(32.dp)
                    }
                    .gridItem(alignment = Alignment.Center),
        )
        TabRow(
            current = current,
            onTabClicked = onTabClicked,
            onTabFocused = onTabFocused,
            modifier =
                Modifier
                    .gridItem(alignment = Alignment.CenterStart)
                    .focusRequester(focusRequester),
        )
        JetStreamLogo(modifier = Modifier.gridItem(column = -1, alignment = Alignment.Center))
    }
}

/**
 * A horizontal row of tabs for navigating between the app's root destinations.
 * Includes support for focus-based navigation and a specialized search tab.
 */
@Composable
private fun TabRow(
    current: Destination?,
    onTabFocused: (Destination) -> Unit,
    onTabClicked: (Destination) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textStyle =
        MaterialTheme.typography.titleSmall.copy(
            color = LocalContentColor.current,
        )
    val selectedTabIndex = currentTabIndex(current)

    val focusRequesterList =
        remember {
            List(Destination.RootDestinations.size + 1) {
                FocusRequester()
            }
        }

    PrimaryTabRow(
        selectedTabIndex = selectedTabIndex,
        divider = {},
        modifier =
            modifier
                .focusRestorer(fallback = focusRequesterList[selectedTabIndex])
                .focusGroup(),
    ) {
        Destination.RootDestinations.forEachIndexed { index, destination ->
            Tab(
                selected = destination == current,
                onClick = {
                    onTabClicked(destination)
                },
                modifier =
                    Modifier
                        .onFocusChanged {
                            if (it.isFocused) {
                                onTabFocused(destination)
                            }
                        }
                        .focusRequester(focusRequesterList[index]),
            ) {
                Text(text = destination.name, style = textStyle)
            }
        }
        Tab(
            selected = Destination.Search == current,
            onClick = { onTabClicked(Destination.Search) },
            modifier =
                Modifier
                    .onFocusChanged {
                        if (it.isFocused) {
                            onTabFocused(Destination.Search)
                        }
                    }
                    .focusRequester(focusRequesterList.last()),
        ) {
            Icon(
                painter = Destination.Search.icon,
                contentDescription = Destination.Search.name,
            )
        }
    }
}

/**
 * Determines the currently selected tab index based on the [current] destination.
 * Returns the index of the destination in [Destination.RootDestinations], or the
 * index of the Search destination if applicable.
 */
private fun currentTabIndex(current: Destination?): Int {
    val index = Destination.RootDestinations.indexOf(current)
    return when {
        index > -1 -> index
        current == Destination.Search -> Destination.RootDestinations.size
        else -> 0
    }
}
