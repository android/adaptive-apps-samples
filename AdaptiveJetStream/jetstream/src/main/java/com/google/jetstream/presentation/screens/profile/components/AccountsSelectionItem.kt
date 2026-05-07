/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.jetstream.presentation.screens.profile.components

import androidx.compose.foundation.layout.ExperimentalFlexBoxApi
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.FlexBox
import androidx.compose.foundation.layout.FlexDirection
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillSize
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.jetstream.presentation.components.shim.stylable.StylableCard
import com.google.jetstream.presentation.screens.profile.section.AccountsSectionData

@OptIn(
    ExperimentalFoundationStyleApi::class,
    ExperimentalGridApi::class,
    ExperimentalFlexBoxApi::class,
)
@Composable
fun AccountsSelectionItem(
    accountsSectionData: AccountsSectionData,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
    StylableCard(
        modifier = modifier,
        enabled = accountsSectionData.onClick != null,
        onClick = accountsSectionData.onClick ?: {},
        style = {
            background(backgroundColor)
        },
    ) {
        val value = accountsSectionData.value

        FlexBox(
            config = {
                direction(FlexDirection.Column)
            },
            modifier =
                Modifier.styleable {
                    fillSize()
                    contentPadding(16.dp)
                },
        ) {
            Text(
                text = accountsSectionData.title,
                style =
                    MaterialTheme.typography.titleSmall.copy(
                        fontSize = 15.sp,
                    ),
            )
            if (value != null) {
                Text(
                    text = value,
                    style =
                        MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                        ),
                    modifier =
                        Modifier.styleable {
                            alpha(0.75f)
                        },
                )
            }
        }
    }
}
