/*
 * Copyright 2026 The Android Open Source Project
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

@file:OptIn(ExperimentalFlexBoxApi::class)

package com.example.adaptiverecipes.flexbox.containerconfig

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexJustifyContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
annotation class JustifyContentPreview

@JustifyContentPreview
@Composable
fun JustifyContentStart() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.Start
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@JustifyContentPreview
@Composable
fun JustifyContentEnd() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.End
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@JustifyContentPreview
@Composable
fun JustifyContentCenter() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.Center
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@JustifyContentPreview
@Composable
fun JustifyContentSpaceBetween() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.SpaceBetween
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@JustifyContentPreview
@Composable
fun JustifyContentSpaceAround() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.SpaceAround
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@JustifyContentPreview
@Composable
fun JustifyContentSpaceEvenly() {
    FlexBox(
        config = {
            justifyContent = FlexJustifyContent.SpaceEvenly
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}
