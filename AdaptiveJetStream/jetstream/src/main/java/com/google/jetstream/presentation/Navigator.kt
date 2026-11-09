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

package com.google.jetstream.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import com.google.jetstream.presentation.app.Destination

internal class Navigator(
    val backStack: NavBackStack<NavKey>,
) {
    fun navigate(destination: Destination) {
        if (destination == current) return
        if (destination == Destination.Home) {
            while (backStack.size > 1) {
                backStack.removeLastOrNull()
            }
            if (backStack.lastOrNull() != Destination.Home) {
                backStack.add(Destination.Home)
            }
        } else if (destination.isTopLevelDestination && current.isTopLevelDestination && current != Destination.Home) {
            backStack[backStack.lastIndex] = destination
        } else {
            backStack.add(destination)
        }
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    val current: Destination
        get() {
            return (backStack.lastOrNull() as? Destination) ?: Destination.Home
        }
}

@Composable
internal fun rememberNavigator(initialDestination: Destination = Destination.Home): Navigator {
    val backStack = rememberNavBackStack(initialDestination)
    return remember(backStack) {
        Navigator(backStack = backStack)
    }
}
