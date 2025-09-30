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

package com.google.jetstream.presentation.components.feature

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.xr.compose.platform.LocalSpatialConfiguration

@Composable
private fun hasSystemFeature(feature: String): Boolean {
    val context = LocalContext.current
    return remember(feature) {
        context.packageManager.hasSystemFeature(feature)
    }
}

@Composable
internal fun isLeanbackEnabled(): Boolean {
    return hasSystemFeature(PackageManager.FEATURE_LEANBACK)
}

@Composable
internal fun isAutomotiveEnabled(): Boolean {
    return hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)
}

@Composable
internal fun isXrSessionAvailable(): Boolean {
    return LocalSpatialConfiguration.current.hasXrSpatialFeature
}
