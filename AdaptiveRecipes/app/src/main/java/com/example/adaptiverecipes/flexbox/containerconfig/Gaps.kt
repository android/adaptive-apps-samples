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
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, widthDp = 300)
@Composable
fun ColumnGap() {
    FlexBox(
        config = {
            columnGap = 16.dp
        }
    ) {
        RedRoundedBox(modifier = Modifier.size(80.dp))
        BlueRoundedBox(modifier = Modifier.size(80.dp))
        GreenRoundedBox(modifier = Modifier.size(80.dp))
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun RowGap() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            rowGap = 16.dp
        }
    ) {
        RedRoundedBox(modifier = Modifier.size(120.dp))
        BlueRoundedBox(modifier = Modifier.size(120.dp))
        GreenRoundedBox(modifier = Modifier.size(120.dp))
        OrangeRoundedBox(modifier = Modifier.size(120.dp))
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
fun CombinedGap() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
            gap(16.dp)
        }
    ) {
        RedRoundedBox(modifier = Modifier.size(120.dp))
        BlueRoundedBox(modifier = Modifier.size(120.dp))
        GreenRoundedBox(modifier = Modifier.size(120.dp))
        OrangeRoundedBox(modifier = Modifier.size(120.dp))
    }
}
