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

package com.example.adaptiverecipes.flexbox.itemconfig

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
annotation class FlexGrowPreview

@FlexGrowPreview
@Composable
fun FlexGrowSingleBefore() {
    FlexBox {
        RedRoundedBox(title = "100dp")
        BlueRoundedBox(title = "100dp")
        GreenRoundedBox(title = "100dp")
    }
}

@FlexGrowPreview
@Composable
fun FlexGrowSingle() {
    FlexBox {
        RedRoundedBox(title = "400dp", modifier = Modifier.flex { grow = 1f })
        BlueRoundedBox(title = "100dp")
        GreenRoundedBox(title = "100dp")
    }
}


@FlexGrowPreview
@Composable
fun FlexGrowMultiple() {
    FlexBox {
        RedRoundedBox(
            title = "150dp",
            modifier = Modifier.flex { grow = 1f }
        )
        BlueRoundedBox(
            title = "200dp",
            modifier = Modifier.flex { grow = 2f }
        )
        GreenRoundedBox(
            title = "250dp",
            modifier = Modifier.flex { grow = 3f }
        )
    }
}
