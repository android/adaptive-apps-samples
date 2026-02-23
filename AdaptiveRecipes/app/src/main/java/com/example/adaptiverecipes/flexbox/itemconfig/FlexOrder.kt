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
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, backgroundColor = 0xFF777777)
annotation class FlexOrderPreview

@FlexOrderPreview
@Composable
fun OrderDefault() {
    FlexBox {
        RedRoundedBox(title = "1")
        BlueRoundedBox(title = "2")
        GreenRoundedBox(title = "3")
        OrangeRoundedBox(title = "4")
    }
}

@FlexOrderPreview
@Composable
fun OrderReversed() {
    FlexBox {
        RedRoundedBox(title = "1", modifier = Modifier.flex { order = 4 })
        BlueRoundedBox(title = "2", modifier = Modifier.flex { order = 3 })
        GreenRoundedBox(title = "3", modifier = Modifier.flex { order = 2 })
        OrangeRoundedBox(title = "4", modifier = Modifier.flex { order = 1 })
    }
}

@FlexOrderPreview
@Composable
fun OrderMixed() {
    FlexBox {
        // Default order is 0.
        // Use negative to move to front, positive to move to back.
        RedRoundedBox(title = "1 (0)")
        BlueRoundedBox(title = "2 (10)", modifier = Modifier.flex { order = 10 })
        GreenRoundedBox(title = "3 (-1)", modifier = Modifier.flex { order = -1 })
        OrangeRoundedBox(title = "4 (0)")
    }
}
