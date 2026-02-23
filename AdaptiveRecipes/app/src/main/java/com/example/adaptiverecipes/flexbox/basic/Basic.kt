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

package com.example.adaptiverecipes.flexbox.basic

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexAlignItems
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.layout.FlexWrap
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.OrangeRoundedBox
import com.example.adaptiverecipes.content.PinkRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox




@Preview(showBackground = true)
@Composable
fun HelloWorld() {
    FlexBox(
        config = {
            direction = FlexDirection.Column
            alignItems = FlexAlignItems.Center
        }
    ) {
        Text(text = "Hello", fontSize = 48.sp)
        Text(text = "World!", fontSize = 48.sp)
    }
}


@Preview(widthDp = 1200, showBackground = true, backgroundColor = 0xFF777777)
@Preview(widthDp = 840, showBackground = true, backgroundColor = 0xFF777777)
@Preview(widthDp = 600, showBackground = true, backgroundColor = 0xFF777777)
annotation class WrapPreview

/**
 * A FlexBox that wraps with variable width items that grow equally to fill the available space
 */
@WrapPreview
@Composable
fun WrapWithGrow() {
    FlexBox(
        config = {
            wrap = FlexWrap.Wrap
        }
    ) {
        RedRoundedBox(modifier = Modifier.width(150.dp).flex { grow = 1.0f })
        BlueRoundedBox(modifier = Modifier.width(250.dp).flex { grow = 1.0f })
        GreenRoundedBox(modifier = Modifier.width(350.dp).flex { grow = 1.0f })
        OrangeRoundedBox(modifier = Modifier.width(200.dp).flex { grow = 1.0f })
        PinkRoundedBox(modifier = Modifier.width(200.dp).flex { grow = 1.0f })
    }
}
