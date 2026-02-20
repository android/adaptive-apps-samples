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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.adaptiverecipes.ui.theme.PastelBlue
import com.example.adaptiverecipes.ui.theme.PastelGreen
import com.example.adaptiverecipes.ui.theme.PastelRed

@Preview(showBackground = true, widthDp = 500)
@Preview(showBackground = true, widthDp = 400)
@Preview(showBackground = true, widthDp = 300)
@Composable
fun FlexShrink() {
    FlexBox {
        Text(
            "The quick brown fox",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(4.dp)
                .background(PastelRed)
                .padding(8.dp)
                .flex { shrink = 0f }
        )
        Text(
            "The quick brown fox",
            fontSize = 20.sp,
            modifier = Modifier
                .padding(4.dp)
                .background(PastelBlue)
                .padding(8.dp)
                .flex { shrink = 1f }
        )
    }
}
