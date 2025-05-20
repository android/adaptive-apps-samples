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

package com.google.jetstream.presentation.screens.videoPlayer.components

import android.app.Activity
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

internal fun Activity.toggleImmersiveMode() {
    if (isImmersive) {
        ImmersiveModeRequest.Exit.request(this)
    } else {
        ImmersiveModeRequest.Enter.request(this)
    }
    isImmersive = !isImmersive
}

internal sealed interface ImmersiveModeRequest {

    fun request(activity: Activity)

    data object Enter : ImmersiveModeRequest {
        override fun request(activity: Activity) {
            val insetsController =
                WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            insetsController.hide(WindowInsetsCompat.Type.systemBars())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity.requestFullscreenMode(
                    Activity.FULLSCREEN_MODE_REQUEST_ENTER,
                    null
                )
            }
        }
    }

    data object Exit : ImmersiveModeRequest {
        override fun request(activity: Activity) {
            val insetsController =
                WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            insetsController.show(WindowInsetsCompat.Type.systemBars())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                activity.requestFullscreenMode(
                    Activity.FULLSCREEN_MODE_REQUEST_EXIT,
                    null
                )
            }
        }
    }
}
