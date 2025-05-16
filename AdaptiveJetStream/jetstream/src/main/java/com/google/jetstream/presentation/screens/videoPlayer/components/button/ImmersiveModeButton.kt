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

package com.google.jetstream.presentation.screens.videoPlayer.components.button

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.screens.videoPlayer.components.toggleImmersiveMode
import kotlinx.coroutines.delay

@Composable
fun ImmersiveModeButton(
    modifier: Modifier = Modifier,
    activity: Activity? = LocalActivity.current,
) {
    if (activity != null) {
        ActualImmersiveButton(
            activity = activity,
            modifier = modifier
        )
    }
}

@Composable
private fun ActualImmersiveButton(
    activity: Activity,
    modifier: Modifier = Modifier,
) {
    var isImmersive by remember(activity) { mutableStateOf(activity.isImmersive) }

    LaunchedEffect(activity) {
        while (true) {
            delay(300)
            isImmersive = activity.isImmersive
        }
    }

    VideoPlayerControlsIcon(
        icon = if (activity.isImmersive) {
            Icons.Default.FullscreenExit
        } else {
            Icons.Default.Fullscreen
        },
        contentDescription = if (isImmersive) {
            StringConstants.Composable.VideoPlayerExitImmersiveMode
        } else {
            StringConstants.Composable.VideoPlayerEnterImmersiveMode
        },
        onClick = activity::toggleImmersiveMode,
        modifier = modifier
    )
}
