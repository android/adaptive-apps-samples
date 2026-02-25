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

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBasis
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaptiverecipes.content.BlueRoundedBox
import com.example.adaptiverecipes.content.GreenRoundedBox
import com.example.adaptiverecipes.content.RedRoundedBox
import com.example.adaptiverecipes.ui.theme.PastelBlue
import com.example.adaptiverecipes.ui.theme.PastelGreen
import com.example.adaptiverecipes.ui.theme.PastelRed

@Preview(showBackground = true, widthDp = 700, backgroundColor = 0xFF777777)
@Preview(showBackground = true, widthDp = 500, backgroundColor = 0xFF777777)
@Preview(showBackground = true, widthDp = 450, backgroundColor = 0xFF777777)
annotation class FlexShrinkPreview

@FlexShrinkPreview
@Composable
fun FlexShrink() {
    FlexBox {
        Text(
            "The quick brown fox",
            fontSize = 36.sp,
            modifier = Modifier
                .background(PastelRed)
                .flex { shrink = 1f }
        )
        Text(
            "The quick brown fox",
            fontSize = 36.sp,
            modifier = Modifier
                .background(PastelBlue)
                .flex { shrink = 0f }
        )
    }
}

@Preview(showBackground = true, widthDp = 600, backgroundColor = 0xFF777777)
@Composable
fun FlexShrinkExampleNotShrunk(){
    FlexBox {
        RedRoundedBox(
            title = "200dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 1f
                }
        )
        BlueRoundedBox(
            title = "200dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 2f
                }
        )
        GreenRoundedBox(
            title = "200dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 3f
                }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, backgroundColor = 0xFF777777)
@Composable
fun FlexShrinkExampleShrunk(){
    FlexBox {
        RedRoundedBox(
            title = "160dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 1f
                }
        )
        BlueRoundedBox(
            title = "120dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 2f
                }
        )
        GreenRoundedBox(
            title = "80dp",
            modifier = Modifier
                .flex {
                    basis = FlexBasis.Dp(200.dp)
                    shrink = 3f
                }
        )
    }
}

