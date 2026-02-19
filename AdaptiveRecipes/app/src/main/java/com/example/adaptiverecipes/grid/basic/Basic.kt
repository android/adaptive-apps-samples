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

@file:OptIn(ExperimentalGridApi::class)

package com.example.adaptiverecipes.grid.basic

import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.PinkRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox
import com.example.adaptiverecipes.content.YellowRoundedBox

/**
 * Define a 2 x 3 grid with columns and rows having a fixed size of 100.dp
 */
@Preview
@Composable
fun FixedGrid() {

    // Define a 2 x 3 Grid
    Grid(
        config = {
            repeat(2){ column(100.dp) }
            repeat(3){ row(100.dp) }
        }
    ) {
        // Items are laid out left to right, then top to bottom
        RedRoundedBox()
        GreenRoundedBox()
        BlueRoundedBox()
        PinkRoundedBox()
        OrangeRoundedBox()
        YellowRoundedBox()
    }
}
