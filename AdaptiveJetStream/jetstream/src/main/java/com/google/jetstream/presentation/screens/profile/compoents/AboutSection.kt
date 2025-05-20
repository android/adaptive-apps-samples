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

package com.google.jetstream.presentation.screens.profile.compoents

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import com.google.jetstream.data.util.StringConstants
import com.google.jetstream.presentation.components.FoldablePreview
import com.google.jetstream.presentation.components.PhonePreview
import com.google.jetstream.presentation.components.TvPreview
import com.google.jetstream.presentation.theme.JetStreamTheme

@Composable
fun AboutSection(isExpanded: Boolean = true) {
    val context = LocalContext.current
    val versionNumber = if (LocalInspectionMode.current) {
        "Preview version"
    } else {
        remember(context) {
            context.getVersionNumber()
        }
    }

    val padding = if (isExpanded) 72.dp else 16.dp
    with(StringConstants.Composable.Placeholders) {
        Column(modifier = Modifier.padding(horizontal = padding)) {
            if (isExpanded) {
                Text(
                    text = AboutSectionTitle,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                modifier = Modifier
                    .graphicsLayer { alpha = 0.8f }
                    .padding(top = 16.dp),
                text = AboutSectionDescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.6f))
            )
            Text(
                modifier = Modifier
                    .graphicsLayer { alpha = 0.6f }
                    .padding(top = 16.dp),
                text = AboutSectionAppVersionTitle,
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = versionNumber,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

private fun Context.getVersionNumber(): String {
    val packageName = packageName
    val metaData = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
    return metaData.versionName!!
}

@PhonePreview
@FoldablePreview
@Composable
fun AboutSectionCompactPreview() {

    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            AboutSection(isExpanded = false)
        }
    }
}

@TvPreview
@Composable
fun AboutSectionExpandedPreview() {
    JetStreamTheme {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
            AboutSection(isExpanded = true)
        }
    }
}
