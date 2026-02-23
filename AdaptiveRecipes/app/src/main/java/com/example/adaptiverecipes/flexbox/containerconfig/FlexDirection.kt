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
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, backgroundColor = 0xFF777777)
annotation class DirectionPreview

@DirectionPreview
@Composable
fun DirectionRow() {
    FlexBox(
        config = {
            direction = FlexDirection.Row
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@DirectionPreview
@Composable
fun DirectionRowReverse() {
    FlexBox(
        config = {
            direction = FlexDirection.RowReverse
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@DirectionPreview
@Composable
fun DirectionColumn() {
    FlexBox(
        config = {
            direction = FlexDirection.Column
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}

@DirectionPreview
@Composable
fun DirectionColumnReverse() {
    FlexBox(
        config = {
            direction = FlexDirection.ColumnReverse
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox()
    }
}
