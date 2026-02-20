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
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox

@Preview(showBackground = true, heightDp = 200)
@Composable
fun AlignItemsStart() {
    FlexBox(
        config = {
            alignItems = FlexAlignItems.Start
        }
    ) {
        RedRoundedBox(modifier = Modifier.height(50.dp))
        BlueRoundedBox()
        GreenRoundedBox(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun AlignItemsEnd() {
    FlexBox(
        config = {
            alignItems = FlexAlignItems.End
        }
    ) {
        RedRoundedBox(modifier = Modifier.height(50.dp))
        BlueRoundedBox()
        GreenRoundedBox(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun AlignItemsCenter() {
    FlexBox(
        config = {
            alignItems = FlexAlignItems.Center
        }
    ) {
        RedRoundedBox(modifier = Modifier.height(50.dp))
        BlueRoundedBox()
        GreenRoundedBox(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun AlignItemsStretch() {
    FlexBox(
        config = {
            alignItems = FlexAlignItems.Stretch
        }
    ) {
        RedRoundedBox()
        BlueRoundedBox()
        GreenRoundedBox(modifier = Modifier.height(150.dp))
    }
}

@Preview(showBackground = true, heightDp = 200)
@Composable
fun AlignItemsBaseline() {
    FlexBox(
        config = {
            alignItems = FlexAlignItems.Baseline
        }
    ) {
        RedRoundedBox(modifier = Modifier.height(50.dp))
        BlueRoundedBox()
        GreenRoundedBox(modifier = Modifier.height(150.dp))
    }
}
