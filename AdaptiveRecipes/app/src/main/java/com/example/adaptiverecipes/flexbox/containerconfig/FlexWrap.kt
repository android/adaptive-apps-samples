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
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.PinkRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, widthDp = 350, backgroundColor = 0xFF777777)
annotation class WrapPreview

@WrapPreview
@Composable
fun NoWrap() {
    FlexBox(
        config = {
            wrap = FlexWrap.NoWrap
        }
    ) {
        RedRoundedBox(title = "1")
        BlueRoundedBox(title = "2")
        GreenRoundedBox(title = "3")
        OrangeRoundedBox(title = "4", modifier = Modifier.flex { shrink = 0f })
    }
}

@WrapPreview
@Composable
fun Wrap() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
        }
    ) {
        RedRoundedBox(title = "1")
        BlueRoundedBox(title = "2")
        GreenRoundedBox(title = "3")
        OrangeRoundedBox(title = "4")
        PinkRoundedBox(title = "5")
    }
}

@WrapPreview
@Composable
fun WrapReverse() {
    FlexBox(
        config = {
            wrap = FlexWrap.WrapReverse
        }
    ) {
        RedRoundedBox(title = "1")
        BlueRoundedBox(title = "2")
        GreenRoundedBox(title = "3")
        OrangeRoundedBox(title = "4")
        PinkRoundedBox(title = "5")
    }
}
