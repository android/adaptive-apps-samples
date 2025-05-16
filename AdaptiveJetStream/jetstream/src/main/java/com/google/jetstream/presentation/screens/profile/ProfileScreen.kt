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

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.jetstream.R
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.profile.compoents.AboutSection
import com.google.jetstream.presentation.screens.profile.compoents.AccountsSection
import com.google.jetstream.presentation.screens.profile.compoents.HelpAndSupportSection
import com.google.jetstream.presentation.screens.profile.compoents.LanguageSection
import com.google.jetstream.presentation.screens.profile.compoents.ProfileScreenLayoutType
import com.google.jetstream.presentation.screens.profile.compoents.ProfileScreens
import com.google.jetstream.presentation.screens.profile.compoents.SearchHistorySection
import com.google.jetstream.presentation.screens.profile.compoents.SubtitlesSection
import com.google.jetstream.presentation.screens.profile.compoents.rememberProfileScreenLayoutType
import com.google.jetstream.presentation.theme.JetStreamTheme
import com.google.jetstream.presentation.theme.LocalContentPadding
import com.google.jetstream.presentation.theme.Padding
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    @FloatRange(from = 0.0, to = 1.0) sidebarWidthFraction: Float = 0.32f,
    profileScreenLayoutType: ProfileScreenLayoutType = rememberProfileScreenLayoutType()
) {
    when (profileScreenLayoutType) {
        ProfileScreenLayoutType.FullyExpanded -> {
            LargeProfileScreen(sidebarWidthFraction)
        }

        ProfileScreenLayoutType.ListDetail -> {
            CompactProfileScreen()
        }
    }
}

@Composable
private fun LargeProfileScreen(
    sidebarWidthFraction: Float,
    contentPadding: Padding = LocalContentPadding.current,
) {
    val profileNavController = rememberNavController()

    val backStack by profileNavController.currentBackStackEntryAsState()

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isLeftColumnFocused by remember { mutableStateOf(false) }

    var selectedLanguageIndex by rememberSaveable { mutableIntStateOf(0) }
    var isSubtitlesChecked by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) { focusRequester.tryRequestFocus() }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = contentPadding.start, vertical = contentPadding.top)
    ) {
        ListPane(
            currentDestination = backStack?.destination?.route ?: ProfileScreens.Accounts(),
            onSelected = {
                profileNavController.navigate(it) {
                    popUpTo(it) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .fillMaxWidth(fraction = sidebarWidthFraction)
                .verticalScroll(rememberScrollState())
                .fillMaxHeight()
                .onFocusChanged {
                    isLeftColumnFocused = it.hasFocus
                }
                .focusRestorer()
                .focusGroup(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            focusRequester = focusRequester
        )
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .onPreviewKeyEvent {
                    when {
                        it.key == Key.Back && it.type == KeyEventType.KeyUp -> {
                            while (!isLeftColumnFocused) {
                                focusManager.moveFocus(FocusDirection.Left)
                            }
                            true
                        }

                        else -> false
                    }
                }
                .focusGroup(),
            navController = profileNavController,
            startDestination = ProfileScreens.Accounts(),
            builder = {
                composable(ProfileScreens.Accounts()) {
                    AccountsSection()
                }
                composable(ProfileScreens.About()) {
                    AboutSection()
                }
                composable(ProfileScreens.Subtitles()) {
                    SubtitlesSection(
                        isSubtitlesChecked = isSubtitlesChecked,
                        onSubtitleCheckChange = { isSubtitlesChecked = it }
                    )
                }
                composable(ProfileScreens.Language()) {
                    LanguageSection(
                        selectedIndex = selectedLanguageIndex,
                        onSelectedIndexChange = { selectedLanguageIndex = it }
                    )
                }
                composable(ProfileScreens.SearchHistory()) {
                    SearchHistorySection()
                }
                composable(ProfileScreens.HelpAndSupport()) {
                    HelpAndSupportSection()
                }
            }
        )
    }
}

@Composable
private fun ListPane(
    currentDestination: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp),
    focusRequester: FocusRequester = remember { FocusRequester() }
) {
    Column(
        modifier = modifier, verticalArrangement = verticalArrangement
    ) {
        ProfileScreens.entries.forEachIndexed { index, profileScreen ->
            key(index) {
                ListItem(
                    trailingContent = {
                        Icon(
                            profileScreen.icon,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .padding(start = 4.dp)
                                .size(20.dp),
                            contentDescription = stringResource(
                                id = R.string.profile_screen_listItem_icon_content_description,
                                profileScreen.tabTitle
                            )
                        )
                    },
                    headlineContent = {
                        Text(
                            text = profileScreen.tabTitle,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (index == 0) Modifier.focusRequester(focusRequester)
                            else Modifier
                        )
                        .onFocusChanged {
                            if (it.isFocused && currentDestination != profileScreen.name) {
                                onSelected(profileScreen())
                            }
                        }
                        .clickable { onSelected(profileScreen()) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun CompactProfileScreen() {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var lastSelected by remember { mutableStateOf(ProfileScreens.Accounts) }

    var selectedLanguageIndex by rememberSaveable { mutableIntStateOf(0) }
    var isSubtitlesChecked by rememberSaveable { mutableStateOf(true) }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        listPane = {
            AnimatedPane {
                Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp)) {
                    ProfileScreens.entries.forEachIndexed { index, profileScreen ->
                        key(index) {
                            CompactListItem(
                                profileScreen = profileScreen,
                                isSelected = lastSelected == profileScreen
                            ) {
                                scope.launch {
                                    scaffoldNavigator.navigateTo(
                                        ListDetailPaneScaffoldRole.Detail,
                                        profileScreen
                                    )
                                }
                                lastSelected = profileScreen
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                // Show the detail pane content if selected item is available
                scaffoldNavigator.currentDestination?.contentKey?.let { profileScreen ->
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (scaffoldNavigator.canNavigateBack()) {
                            CompactListItem(
                                profileScreen = profileScreen as ProfileScreens,
                                showBackButton = true,
                                onClick = {
                                    scope.launch {
                                        scaffoldNavigator.navigateBack()
                                    }
                                }
                            )
                        }
                        when (profileScreen) {
                            ProfileScreens.Accounts -> AccountsSection(numberOfColumns = 1)
                            ProfileScreens.About -> AboutSection()
                            ProfileScreens.Subtitles ->
                                SubtitlesSection(
                                    isSubtitlesChecked = isSubtitlesChecked,
                                    onSubtitleCheckChange = { isSubtitlesChecked = it }
                                )

                            ProfileScreens.Language ->
                                LanguageSection(
                                    selectedIndex = selectedLanguageIndex,
                                    onSelectedIndexChange = { selectedLanguageIndex = it }
                                )

                            ProfileScreens.SearchHistory -> SearchHistorySection()
                            ProfileScreens.HelpAndSupport -> HelpAndSupportSection()
                        }
                    }
                }
            }
        },
    )
}

@Composable
private fun CompactListItem(
    profileScreen: ProfileScreens,
    isSelected: Boolean = false,
    showBackButton: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            Icon(
                Icons.Default.ArrowBackIosNew,
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .padding(start = 4.dp)
                    .size(36.dp),
                contentDescription = stringResource(
                    id = R.string.back_content_description,
                    profileScreen.tabTitle
                )
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
        Icon(
            profileScreen.icon,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .padding(start = 4.dp)
                .size(36.dp),
            contentDescription = stringResource(
                id = R.string.profile_screen_listItem_icon_content_description,
                profileScreen.tabTitle
            )
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = profileScreen.tabTitle,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@TvPreview
@Composable
fun ProfileScreenTvPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.FullyExpanded)
        }
    }
}

@FoldablePreview
@Composable
fun ProfileScreenFoldablePreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.ListDetail)
        }
    }
}

@PhonePreview
@Composable
fun ProfileScreenPhonePreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            ProfileScreen(profileScreenLayoutType = ProfileScreenLayoutType.ListDetail)
        }
    }
}
