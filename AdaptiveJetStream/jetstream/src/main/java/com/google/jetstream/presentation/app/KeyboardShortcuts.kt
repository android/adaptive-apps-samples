/*
 * Copyright 2026 Google LLC
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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.navigation.NavHostController
import com.google.jetstream.presentation.components.KeyboardShortcut
import com.google.jetstream.presentation.components.ModifierKey
import com.google.jetstream.presentation.screens.Screens

// TODO: Consider associating the keys and modifier keys with each Screen directly, rather than
//  having this separate list
@Composable
fun rememberKeyboardShortcuts(
    navController: NavHostController,
    selectedScreen: Screens
): List<KeyboardShortcut> = remember {
    listOf(
        KeyboardShortcut(
            key = Key.Comma,
            modifierKeys = setOf(ModifierKey.Ctrl),
            action = {
                if (selectedScreen != Screens.Profile) {
                    navController.navigate(Screens.Profile())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.P,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Profile) {
                    navController.navigate(Screens.Profile())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.H,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Home) {
                    navController.navigate(Screens.Home())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.C,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Categories) {
                    navController.navigate(Screens.Categories())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.M,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Movies) {
                    navController.navigate(Screens.Movies())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.T,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Shows) {
                    navController.navigate(Screens.Shows())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.F,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Favourites) {
                    navController.navigate(Screens.Favourites())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.Slash,
            action = {
                if (selectedScreen != Screens.Search) {
                    navController.navigate(Screens.Search())
                }
            }
        ),
        KeyboardShortcut(
            key = Key.S,
            modifierKeys = setOf(ModifierKey.Ctrl, ModifierKey.Alt),
            action = {
                if (selectedScreen != Screens.Search) {
                    navController.navigate(Screens.Search())
                }
            }
        ),
    )
}

