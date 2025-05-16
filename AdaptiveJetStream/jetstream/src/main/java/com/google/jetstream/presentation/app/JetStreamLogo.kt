/*
 * Copyright 2025 Google LLC
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

package com.google.jetstream.presentation.app

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.jetstream.R
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.theme.IconSize
import com.google.jetstream.presentation.theme.LexendExa

@Composable
fun JetStreamLogo(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.PlayCircle,
            contentDescription = StringConstants.Composable.ContentDescription.BrandLogoImage,
            modifier = Modifier
                .padding(end = 4.dp)
                .size(IconSize)
        )
        Text(
            text = stringResource(R.string.brand_logo_text),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            fontFamily = LexendExa,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
