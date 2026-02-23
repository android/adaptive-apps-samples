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
import androidx.compose.foundation.layout.FlexAlignContent
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.PinkRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox
import com.example.adaptiverecipes.content.YellowRoundedBox

@Preview(showBackground = true, widthDp = 300, heightDp = 400, backgroundColor = 0xFF777777)
annotation class AlignContentPreview

@AlignContentPreview
@Composable
fun AlignContentStart() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.Start
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}

@AlignContentPreview
@Composable
fun AlignContentEnd() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.End
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}

@AlignContentPreview
@Composable
fun AlignContentCenter() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.Center
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}

@AlignContentPreview
@Composable
fun AlignContentStretch() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.Stretch
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}

@AlignContentPreview
@Composable
fun AlignContentSpaceBetween() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.SpaceBetween
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}

@AlignContentPreview
@Composable
fun AlignContentSpaceAround() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            alignContent = FlexAlignContent.SpaceAround
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
        OrangeRoundedBox()
        PinkRoundedBox()
        YellowRoundedBox()
    }
}
