/*
 * Copyright 2025 Google LLC
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

package com.google.jetstream.presentation.screens.videoPlayer.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import com.google.jetstream.data.entities.MovieDetails
import com.google.jetstream.presentation.components.shim.tryRequestFocus
import com.google.jetstream.presentation.screens.videoPlayer.components.button.ClosedCaptionButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.ImmersiveModeButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.NextButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.PlayListButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.PlayPauseButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.PreviousButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.RepeatButton
import com.google.jetstream.presentation.screens.videoPlayer.components.button.SettingsButton
import kotlin.time.Duration.Companion.milliseconds

private val DefaultActionSpacing = 12.dp
private val TabletopActionSpacing = 15.dp
private val TabletopButtonSize = 70.dp
private val BottomPadding = 16.dp

@Composable
fun VideoPlayerControls(
    movieDetails: MovieDetails,
    player: Player,
    modifier: Modifier = Modifier,
    isImmersiveModeAvailable: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.tryRequestFocus()
    }

    val isTabletop = currentWindowAdaptiveInfo().windowPosture.isTabletop

    VideoPlayerMainFrame(
        mediaTitle = {
            VideoPlayerMediaTitle(
                title = movieDetails.name,
                secondaryText = movieDetails.releaseDate,
                tertiaryText = movieDetails.director,
                type = VideoPlayerMediaTitleType.DEFAULT,
                modifier = Modifier.fillMaxWidth()
            )
        },
        mediaActions = {
            if (isTabletop) {
                TabletopMediaActions(player, isImmersiveModeAvailable)
            } else {
                DefaultMediaActions(player, isImmersiveModeAvailable)
            }
        },
        seeker = {
            VideoPlayerSeeker(
                player = player,
                onSeek = { player.seekTo(player.duration.times(it).toLong()) },
                contentDuration = player.duration.milliseconds,
                modifier = Modifier.focusRequester(focusRequester),
                shouldShowPlayPauseButton = !isTabletop
            )
        },
        more = null,
        modifier = modifier
    )
}

@Composable
private fun TabletopMediaActions(
    player: Player,
    isImmersiveModeAvailable: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = BottomPadding)
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                TabletopActionSpacing,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreviousButton(player = player, modifier = Modifier.size(TabletopButtonSize))
            PlayPauseButton(player = player, modifier = Modifier.size(TabletopButtonSize))
            NextButton(player = player, modifier = Modifier.size(TabletopButtonSize))
        }
        PlayerActions(
            player = player,
            isImmersiveModeAvailable = isImmersiveModeAvailable,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(
                TabletopActionSpacing,
                Alignment.CenterHorizontally
            )
        )
    }
}

@Composable
private fun DefaultMediaActions(
    player: Player,
    isImmersiveModeAvailable: Boolean,
) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .padding(bottom = BottomPadding)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DefaultActionSpacing),
    ) {
        PreviousButton(player = player)
        NextButton(player = player)
        PlayerActions(player, isImmersiveModeAvailable)
    }
}

@Composable
private fun PlayerActions(
    player: Player,
    isImmersiveModeAvailable: Boolean,
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(DefaultActionSpacing)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RepeatButton(player = player)
        PlayListButton()
        ClosedCaptionButton()
        SettingsButton()
        if (isImmersiveModeAvailable) {
            ImmersiveModeButton()
        }
    }
}