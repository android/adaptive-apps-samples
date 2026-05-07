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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridConfigurationScope
import androidx.compose.foundation.layout.GridTrackSize
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import androidx.xr.compose.spatial.Subspace
import androidx.xr.compose.subspace.ResizePolicy
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.unit.DpVolumeSize
import com.google.jetstream.presentation.components.feature.EngagementMode
import com.google.jetstream.presentation.components.feature.LocalEngagementMode
import com.google.jetstream.presentation.theme.JetStreamTokens

enum class PresentationType {
    SinglePane,
    ListDetailParent,
    ListDetailChild,
    Overlay,
    ;

    companion object {
        val PresentationTypeKey = object : NavMetadataKey<PresentationType> {}
    }
}

/**
 * Strategy for decorating a scene with navigation component and subNavigation component
 * based on its presentation type and current EngagementMode.
 *
 * @param navigation A component implementing global navigation.
 * @param subNavigation A component implement supplement navigation.
 * @param engagementMode The current EngagementMode.
 */
class AppLayoutSceneDecoratorStrategy(
    val engagementMode: EngagementMode,
    val subNavigation: @Composable () -> Unit = {},
    val navigation: @Composable () -> Unit = {},
) : SceneDecoratorStrategy<NavKey> {
    override fun SceneDecoratorStrategyScope<NavKey>.decorateScene(
        scene: Scene<NavKey>,
    ): Scene<NavKey> {
        val presentationType = scene.metadata[PresentationType.PresentationTypeKey]

        // If the presentation type is Overlay, do not decorate the scene
        return when {
            engagementMode == EngagementMode.Spatial -> {
                SpatialAppLayoutSceneDecorator(
                    scene = scene,
                    navigation = navigation,
                    subNavigation = subNavigation,
                )
            }

            presentationType == PresentationType.Overlay -> {
                scene
            }

            else -> {
                // Otherwise, decorate the scene with a layout that includes navigation
                AppLayoutSceneDecorator(
                    scene = scene,
                    navigation = navigation,
                    subNavigation = subNavigation,
                )
            }
        }
    }
}

private class SpatialAppLayoutSceneDecorator(
    val scene: Scene<NavKey>,
    val subNavigation: @Composable () -> Unit = {},
    val navigation: @Composable () -> Unit = {},
) : Scene<NavKey> {
    override val key: Any
        get() = scene.key
    override val entries: List<NavEntry<NavKey>>
        get() = scene.entries
    override val previousEntries: List<NavEntry<NavKey>>
        get() = scene.previousEntries

    @OptIn(ExperimentalMaterial3XrApi::class)
    override val content: @Composable (() -> Unit) = {

        val resizePolicy =
            remember {
                ResizePolicy(
                    minimumSize = DpVolumeSize.from(JetStreamTokens.LeanbackWindowSize),
                )
            }
        val presentationType = scene.metadata[PresentationType.PresentationTypeKey]

        val isNavigationVisible = presentationType != PresentationType.Overlay

        Subspace {
            SpatialPanel(
                resizePolicy = resizePolicy,
                modifier = SubspaceModifier.movable(),
            ) {
                Surface {
                    MainPanel(
                        isNavigationVisible = isNavigationVisible,
                        navigation = subNavigation,
                        content = scene.content,
                    )
                }
            }
            if (isNavigationVisible) {
                navigation()
            }
        }
    }

    @Composable
    private fun MainPanel(
        isNavigationVisible: Boolean,
        navigation: @Composable () -> Unit,
        content: @Composable () -> Unit,
    ) {
        Column {
            if (isNavigationVisible) {
                navigation()
            }
            content()
        }
    }
}

/**
 * A scene decorator that wraps the content of a scene with navigation components.
 * It uses a Grid layout to position navigation, sub-navigation, and the main content.
 */
private class AppLayoutSceneDecorator(
    val scene: Scene<NavKey>,
    val subNavigation: @Composable () -> Unit = {},
    val navigation: @Composable () -> Unit = {},
) : Scene<NavKey> {
    override val key: Any
        get() = scene.key
    override val entries: List<NavEntry<NavKey>>
        get() = scene.entries
    override val previousEntries: List<NavEntry<NavKey>>
        get() = scene.previousEntries

    @OptIn(ExperimentalGridApi::class, ExperimentalFoundationStyleApi::class)
    override val content: @Composable (() -> Unit) = {
        val layout = selectLayout()
        val subNavigationArea = layout.subNavigationArea
        val backgroundColor = MaterialTheme.colorScheme.surface

        Grid(
            config = layout.gridConfig,
            modifier =
                Modifier.styleable {
                    background(backgroundColor)
                },
        ) {
            Box(
                modifier =
                    Modifier.gridItem(
                        column = layout.navigationArea.column,
                        row = layout.navigationArea.row,
                        rowSpan = layout.navigationArea.rowSpan,
                        columnSpan = layout.navigationArea.columnSpan,
                    ),
            ) {
                navigation()
            }
            if (subNavigationArea != null) {
                Box(
                    modifier =
                        Modifier.gridItem(
                            column = subNavigationArea.column,
                            row = subNavigationArea.row,
                            rowSpan = subNavigationArea.rowSpan,
                            columnSpan = subNavigationArea.columnSpan,
                        ),
                ) {
                    subNavigation()
                }
            }
            scene.content()
        }
    }

    /**
     * Selects the appropriate layout based on the current EngagementMode.
     */
    @OptIn(ExperimentalGridApi::class)
    @Composable
    private fun selectLayout(): AppLayout {
        return when (LocalEngagementMode.current) {
            is EngagementMode.Compact -> AppLayout.NavigationBar
            EngagementMode.Leanback, EngagementMode.Cabin, is EngagementMode.Workstation -> AppLayout.TopBar
            else -> AppLayout.NavigationRail
        }
    }
}

// ToDo: Update with named-area.

/**
 * Interface defining the grid configuration and areas for different app layouts.
 */
@OptIn(ExperimentalGridApi::class)
private sealed interface AppLayout {
    val gridConfig: GridConfigurationScope.() -> Unit
    val navigationArea: GridArea
    val subNavigationArea: GridArea?
    val contentArea: GridArea

    /**
     * Layout using a bottom navigation bar, typically for Compact.
     * The navigation, subNavigation, and content are rendered in the following layout:
     *   - navigation: area A
     *   - subNavigation: area B
     *   - content: area C
     *
     *   B B B B
     *   C C C C
     *   C C C C
     *   C C C C
     *   C C C C
     *   A A A A
     */
    object NavigationBar : AppLayout {
        override val gridConfig: GridConfigurationScope.() -> Unit = {
            column(GridTrackSize.MinMax(350.dp, 1.fr))
            row(GridTrackSize.Auto)
            row(GridTrackSize.MinMax(200.dp, 1.fr))
            row(GridTrackSize.Auto)
        }
        override val navigationArea = GridArea(row = -1, column = 1)
        override val subNavigationArea = GridArea(row = 1, column = 1)
        override val contentArea = GridArea(row = 2, column = 1)
    }

    /**
     * Layout using a side navigation rail, typically for Medium.
     * It layouts navigation, subNavigation and content as follows.
     *   - navigation: area A
     *   - subNavigation: area B
     *   - content: area C
     *
     *   A B B B B
     *   A C C C C
     *   A C C C C
     *   A C C C C
     */
    object NavigationRail : AppLayout {
        override val gridConfig: GridConfigurationScope.() -> Unit
            get() = {
                column(GridTrackSize.Auto)
                column(GridTrackSize.MinMax(200.dp, 1.fr))
                row(GridTrackSize.Auto)
                row(GridTrackSize.MinMax(200.dp, 1.fr))
                rowGap(8.dp)
            }
        override val navigationArea = GridArea(column = 1, row = 1, rowSpan = 2, columnSpan = 1)
        override val subNavigationArea = GridArea(column = 2, row = 1)
        override val contentArea = GridArea(column = 2, row = 2)
    }

    /**
     * Layout using a top navigation bar, typically for Leanback.
     * It layouts navigation, subNavigation and content as follows.
     *   - navigation: area A
     *   - subNavigation: N/A
     *   - content: area C
     *
     *   A A A A A
     *   C C C C C
     *   C C C C C
     *   C C C C C
     */
    object TopBar : AppLayout {
        override val gridConfig: GridConfigurationScope.() -> Unit = {
            column(1f)
            row(GridTrackSize.Auto)
            row(GridTrackSize.MinMax(300.dp, 1.fr))
        }
        override val navigationArea: GridArea = GridArea(row = 1, column = 1)
        override val subNavigationArea: GridArea? = null
        override val contentArea: GridArea = GridArea(row = 2, column = 1)
    }
}

/**
 * Data structure describing a grid area.
 */
private data class GridArea(
    val row: Int = 0,
    val column: Int = 0,
    val rowSpan: Int = 1,
    val columnSpan: Int = 1,
)

private fun DpVolumeSize.Companion.from(dpSize: DpSize): DpVolumeSize {
    return DpVolumeSize(width = dpSize.width, height = dpSize.height, depth = 0.dp)
}

/**
 * Remembers an [AppLayoutSceneDecoratorStrategy] with the given navigation components.
 */
@Composable
fun rememberAppLayoutSceneDecorator(
    subNavigation: @Composable () -> Unit = {},
    navigation: @Composable () -> Unit = {},
): AppLayoutSceneDecoratorStrategy {
    val engagementMode = LocalEngagementMode.current
    return remember(navigation, subNavigation, engagementMode) {
        AppLayoutSceneDecoratorStrategy(
            navigation = navigation,
            subNavigation = subNavigation,
            engagementMode = engagementMode,
        )
    }
}
