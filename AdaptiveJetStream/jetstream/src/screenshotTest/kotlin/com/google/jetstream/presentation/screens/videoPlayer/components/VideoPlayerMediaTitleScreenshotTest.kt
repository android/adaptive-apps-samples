/*
 * Copyright 2024 Google LLC
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

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.android.tools.screenshot.PreviewTest
import com.google.jetstream.presentation.theme.JetStreamTheme

@PreviewTest
@Preview(name = "TV Series", device = "id:tv_4k")
@Composable
fun VideoPlayerMediaTitlePreviewSeriesScreenshot() {
    JetStreamTheme {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "True Detective",
                secondaryText = "S1E5",
                tertiaryText = "The Secret Fate Of All Life",
                type = VideoPlayerMediaTitleType.DEFAULT
            )
        }
    }
}

@PreviewTest
@Preview(name = "Live", device = "id:tv_4k")
@Composable
fun VideoPlayerMediaTitlePreviewLiveScreenshot() {
    JetStreamTheme {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "MacLaren Reveal Their 2022 Car: The MCL36",
                secondaryText = "Formula 1",
                tertiaryText = "54K watching now",
                type = VideoPlayerMediaTitleType.LIVE
            )
        }
    }
}

@PreviewTest
@Preview(name = "Ads", device = "id:tv_4k")
@Composable
fun VideoPlayerMediaTitlePreviewAdScreenshot() {
    JetStreamTheme {
        Surface(shape = RectangleShape) {
            VideoPlayerMediaTitle(
                title = "Samsung Galaxy Note20 | Ultra 5G",
                secondaryText = "Get the most powerful Note yet",
                tertiaryText = "",
                type = VideoPlayerMediaTitleType.AD
            )
        }
    }
}
