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

import android.content.Context
import android.hardware.input.InputManager
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.InputDevice
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow

enum class InputDeviceType {
    TouchScreen,
    Keyboard,
    Mouse,
    TouchPad,
    Dpad,
    RotaryController;

    internal fun inputSources(): List<Int> {
        return when (this) {
            TouchScreen -> listOf(InputDevice.SOURCE_TOUCHSCREEN)
            Keyboard -> listOf(InputDevice.SOURCE_KEYBOARD)
            Mouse -> listOf(InputDevice.SOURCE_MOUSE, InputDevice.SOURCE_TRACKBALL)
            TouchPad -> listOf(InputDevice.SOURCE_TOUCHPAD)
            Dpad -> listOf(InputDevice.SOURCE_DPAD, InputDevice.SOURCE_HDMI)
            RotaryController -> listOf(InputDevice.SOURCE_ROTARY_ENCODER)
        }
    }
}

class InputDeviceMonitor(
    private var inputManager: InputManager,
    onInputDeviceUpdated: (List<InputDeviceType>) -> Unit
) : HandlerThread("InputDeviceMonitor") {

    private lateinit var handler: Handler
    private val listener = object : InputManager.InputDeviceListener {
        override fun onInputDeviceAdded(deviceId: Int) {
            updateDeviceList()
        }

        override fun onInputDeviceRemoved(deviceId: Int) {
            updateDeviceList()
        }

        override fun onInputDeviceChanged(deviceId: Int) {
            updateDeviceList()
        }

        private fun updateDeviceList() {
            val updatedList = inputManager.inputDeviceIds
                .map { deviceId ->
                    inputManager.getInputDevice(deviceId)
                }
                .filterNotNull()
                .filter { device ->
                    device.isEnabled && !device.isVirtual
                }
                .map { device ->
                    device.deviceType()
                }
                .flatten()
                .toSet()
                .toList()

            Log.d("InputDeviceMonitor", "Available device types: $updatedList")
            onInputDeviceUpdated(updatedList)
        }
    }

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        handler = Handler(looper)
        inputManager.registerInputDeviceListener(listener, handler)
    }

}

@Composable
internal fun rememberInputDeviceMonitor(
    context: Context = LocalContext.current,
    onInputDeviceUpdated: (List<InputDeviceType>) -> Unit,
): InputDeviceMonitor {
    val inputManager = remember {
        context.getSystemService(Context.INPUT_SERVICE) as InputManager
    }

    return remember {
        InputDeviceMonitor(
            inputManager = inputManager,
            onInputDeviceUpdated = onInputDeviceUpdated
        ).also { monitor ->
            monitor.start()
        }
    }
}

private fun InputDevice.deviceType(): List<InputDeviceType> {

    val list = InputDeviceType.entries.filter { type ->
        type.inputSources().any { expected ->
            sources and expected == expected
        }
    }
    return list
}

@Composable
internal fun rememberAvailableInputDevices(
    context: Context = LocalContext.current,
): State<List<InputDeviceType>> {
    val availableDeviceTypes = remember {
        MutableStateFlow<List<InputDeviceType>>(emptyList())
    }
    rememberInputDeviceMonitor(
        context = context,
        onInputDeviceUpdated = {
            availableDeviceTypes.value = it
        }
    )
    return availableDeviceTypes.collectAsStateWithLifecycle()
}

@Composable
internal fun isDpadAvailable(
    context: Context = LocalContext.current,
): Boolean {
    val list by rememberAvailableInputDevices(context)
    return list.contains(InputDeviceType.Dpad)
}

@Composable
internal fun isMouseAvailable(
    context: Context = LocalContext.current,
): Boolean {
    val list by rememberAvailableInputDevices(context)
    return list.contains(InputDeviceType.Mouse)
}

@Composable
internal fun isTouchPadAvailable(
    context: Context = LocalContext.current,
): Boolean {
    val list by rememberAvailableInputDevices(context)
    return list.contains(InputDeviceType.TouchPad)
}
