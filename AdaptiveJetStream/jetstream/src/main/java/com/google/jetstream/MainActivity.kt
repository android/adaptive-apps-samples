/*
 * Copyright 2024 Google LLC
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

package com.google.jetstream

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyboardShortcutGroup
import android.view.KeyboardShortcutInfo
import android.view.Menu
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.xr.compose.material3.ExperimentalMaterial3XrApi
import com.google.jetstream.presentation.App
import com.google.jetstream.presentation.theme.JetStreamTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3XrApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        tryEnableCustomHeader()
        setContent {
            JetStreamTheme {
                CompositionLocalProvider(
                    // TODO: Shouldn't this be set by JetStreamTheme?
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface
                ) {
                    App(
                        // TODO: Figure out why this is being used instead of a BackHandler
                        onActivityBackPressed = onBackPressedDispatcher::onBackPressed,
                        // TODO: Is it necessary to tell every child that they need to fill max size and use safe drawing padding?
                        //  This feels like it would be better declared by the main app layouts rather than being mandated here
                        modifier = Modifier
                            .safeDrawingPadding()
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    override fun onProvideKeyboardShortcuts(
        data: MutableList<KeyboardShortcutGroup>?,
        menu: Menu?,
        deviceId: Int
    ) {
        data?.addAll(keyboardShortcuts)
    }

    private fun tryEnableCustomHeader() {
        if (toEnableCustomHeader()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                window.insetsController?.apply {
                    setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
                        WindowInsetsController.APPEARANCE_TRANSPARENT_CAPTION_BAR_BACKGROUND,
                    )
                }
            }
        }
    }

    private fun toEnableCustomHeader(): Boolean {
        val reliedFeatures = listOf(
            PackageManager.FEATURE_FREEFORM_WINDOW_MANAGEMENT,
            PackageManager.FEATURE_PICTURE_IN_PICTURE,
        )
        return reliedFeatures.all { feature ->
            packageManager.hasSystemFeature(feature)
        }
    }

    private val keyboardShortcuts by lazy {
        listOf(
            KeyboardShortcutGroup(
                getString(R.string.general),
                listOf(
                    KeyboardShortcutInfo(
                        getString(R.string.close_dialog),
                        KeyEvent.KEYCODE_ESCAPE,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.scroll_down),
                        KeyEvent.KEYCODE_PAGE_DOWN,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.scroll_up),
                        KeyEvent.KEYCODE_PAGE_UP,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_profile_screen),
                        KeyEvent.KEYCODE_COMMA,
                        KeyEvent.META_CTRL_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_profile_screen),
                        KeyEvent.KEYCODE_P,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_home_screen),
                        KeyEvent.KEYCODE_H,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_movies_screen),
                        KeyEvent.KEYCODE_M,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_shows_screen),
                        KeyEvent.KEYCODE_T,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_favorites_screen),
                        KeyEvent.KEYCODE_F,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_search_screen),
                        KeyEvent.KEYCODE_SLASH,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.show_search_screen),
                        KeyEvent.KEYCODE_S,
                        KeyEvent.META_CTRL_ON + KeyEvent.META_ALT_ON
                    ),
                )
            ),
            KeyboardShortcutGroup(
                getString(R.string.video_playback),
                listOf(
                    KeyboardShortcutInfo(
                        getString(R.string.back_from_video_player),
                        KeyEvent.KEYCODE_BACK,
                        0
                    ),

                    KeyboardShortcutInfo(
                        getString(R.string.toggle_full_screen),
                        KeyEvent.KEYCODE_F,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.back_from_video_player),
                        KeyEvent.KEYCODE_ESCAPE,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.toggle_play_pause),
                        KeyEvent.KEYCODE_K,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.rewind_10_sec),
                        KeyEvent.KEYCODE_J,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.skip_10_sec),
                        KeyEvent.KEYCODE_L,
                        0
                    ),
                )
            ),
            KeyboardShortcutGroup(
                getString(R.string.spatial_video),
                listOf(
                    KeyboardShortcutInfo(
                        getString(R.string.pan_up),
                        KeyEvent.KEYCODE_W,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.pan_down),
                        KeyEvent.KEYCODE_S,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.pan_left),
                        KeyEvent.KEYCODE_A,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.pan_right),
                        KeyEvent.KEYCODE_D,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.zoom_in),
                        KeyEvent.KEYCODE_PLUS,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.zoom_in),
                        KeyEvent.KEYCODE_RIGHT_BRACKET,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.zoom_out),
                        KeyEvent.KEYCODE_MINUS,
                        0
                    ),
                    KeyboardShortcutInfo(
                        getString(R.string.zoom_out),
                        KeyEvent.KEYCODE_LEFT_BRACKET,
                        0
                    ),
                )
            )
        )
    }
}
