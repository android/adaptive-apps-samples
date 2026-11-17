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

package com.google.jetstream.presentation.screens.videoPlayer

import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import java.lang.reflect.Proxy
import java.util.concurrent.CopyOnWriteArraySet

class FakePlayer(
    initialIsPlaying: Boolean = false,
    var durationMs: Long = 120_000L,
) {
    private val listeners = CopyOnWriteArraySet<Player.Listener>()
    val mediaItems = mutableListOf<MediaItem>()

    var isPlayingState: Boolean = initialIsPlaying
        private set
    var currentPositionMs: Long = 0L
        private set
    var currentMediaItemIndexState: Int = 0
        private set
    var repeatModeState: Int = Player.REPEAT_MODE_OFF
        private set
    var playCallCount: Int = 0
        private set
    var pauseCallCount: Int = 0
        private set
    var seekBackCallCount: Int = 0
        private set
    var seekForwardCallCount: Int = 0
        private set
    var isPrepared: Boolean = false
        private set
    var isReleased: Boolean = false
        private set

    private val availableCommands =
        Player.Commands.Builder()
            .addAllCommands()
            .build()

    val instance: Player =
        Proxy.newProxyInstance(
            Player::class.java.classLoader,
            arrayOf(Player::class.java),
        ) { proxy, method, args ->
            when (method.name) {
                "addListener" -> {
                    val listener = args?.getOrNull(0) as? Player.Listener
                    if (listener != null) listeners.add(listener)
                    Unit
                }

                "removeListener" -> {
                    val listener = args?.getOrNull(0) as? Player.Listener
                    if (listener != null) listeners.remove(listener)
                    Unit
                }

                "getApplicationLooper" -> {
                    Looper.getMainLooper()
                }

                "getAvailableCommands" -> {
                    availableCommands
                }

                "isCommandAvailable" -> {
                    true
                }

                "isPlaying" -> {
                    isPlayingState
                }

                "getPlayWhenReady" -> {
                    isPlayingState
                }

                "setPlayWhenReady" -> {
                    val playWhenReady = args?.getOrNull(0) as? Boolean ?: false
                    setPlaying(playWhenReady)
                    Unit
                }

                "getPlaybackState" -> {
                    Player.STATE_READY
                }

                "getPlaybackSuppressionReason" -> {
                    Player.PLAYBACK_SUPPRESSION_REASON_NONE
                }

                "play" -> {
                    playCallCount++
                    setPlaying(true)
                    Unit
                }

                "pause" -> {
                    pauseCallCount++
                    setPlaying(false)
                    Unit
                }

                "stop" -> {
                    setPlaying(false)
                    Unit
                }

                "prepare" -> {
                    isPrepared = true
                    Unit
                }

                "release" -> {
                    isReleased = true
                    Unit
                }

                "seekBack" -> {
                    seekBackCallCount++
                    currentPositionMs = (currentPositionMs - 10_000L).coerceAtLeast(0L)
                    Unit
                }

                "seekForward" -> {
                    seekForwardCallCount++
                    currentPositionMs = (currentPositionMs + 10_000L).coerceAtMost(durationMs)
                    Unit
                }

                "seekTo" -> {
                    val pos = args?.lastOrNull() as? Long ?: 0L
                    currentPositionMs = pos.coerceIn(0L, durationMs)
                    Unit
                }

                "getDuration" -> {
                    durationMs
                }

                "getCurrentPosition" -> {
                    currentPositionMs
                }

                "getBufferedPosition" -> {
                    currentPositionMs
                }

                "getRepeatMode" -> {
                    repeatModeState
                }

                "setRepeatMode" -> {
                    repeatModeState = args?.getOrNull(0) as? Int ?: Player.REPEAT_MODE_OFF
                    listeners.forEach { it.onRepeatModeChanged(repeatModeState) }
                    Unit
                }

                "getVideoSize" -> {
                    VideoSize.UNKNOWN
                }

                "getMediaItemCount" -> {
                    mediaItems.size
                }

                "getMediaItemAt" -> {
                    val index = args?.getOrNull(0) as? Int ?: 0
                    mediaItems[index]
                }

                "getCurrentMediaItem" -> {
                    mediaItems.getOrNull(currentMediaItemIndexState)
                }

                "getCurrentMediaItemIndex" -> {
                    currentMediaItemIndexState
                }

                "clearMediaItems" -> {
                    mediaItems.clear()
                    Unit
                }

                "addMediaItem" -> {
                    val item = args?.lastOrNull() as? MediaItem
                    if (item != null) mediaItems.add(item)
                    Unit
                }

                "addMediaItems" -> {
                    @Suppress("UNCHECKED_CAST")
                    val items = args?.lastOrNull() as? List<MediaItem>
                    if (items != null) mediaItems.addAll(items)
                    Unit
                }

                "replaceMediaItem" -> {
                    val index = args?.getOrNull(0) as? Int ?: 0
                    val item = args?.getOrNull(1) as? MediaItem
                    if (item != null && index in mediaItems.indices) {
                        mediaItems[index] = item
                    }
                    Unit
                }

                "equals" -> {
                    proxy === args?.getOrNull(0)
                }

                "hashCode" -> {
                    System.identityHashCode(proxy)
                }

                "toString" -> {
                    "FakePlayer(isPlaying=$isPlayingState, position=$currentPositionMs)"
                }

                else -> {
                    defaultReturnValue(method.returnType)
                }
            }
        } as Player

    private fun setPlaying(playing: Boolean) {
        val changed = isPlayingState != playing
        isPlayingState = playing
        if (changed) {
            listeners.forEach { listener ->
                listener.onIsPlayingChanged(playing)
                listener.onPlayWhenReadyChanged(playing, Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST)
                listener.onEvents(
                    instance,
                    Player.Events(
                        androidx.media3.common.FlagSet.Builder()
                            .add(Player.EVENT_IS_PLAYING_CHANGED)
                            .add(Player.EVENT_PLAY_WHEN_READY_CHANGED)
                            .build(),
                    ),
                )
            }
        }
    }

    private fun defaultReturnValue(returnType: Class<*>): Any? =
        when (returnType) {
            java.lang.Boolean.TYPE -> false
            java.lang.Integer.TYPE -> 0
            java.lang.Long.TYPE -> 0L
            java.lang.Float.TYPE -> 0f
            java.lang.Double.TYPE -> 0.0
            else -> null
        }
}
