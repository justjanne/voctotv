/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastCoerceIn
import androidx.media3.common.DeviceInfo
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.observeState
import kotlinx.coroutines.delay

class PlayerState(
    private val player: Player,
) {
    enum class Status {
        BUFFERING,
        PLAYING,
        PAUSED,
        ENDED,
    }

    private val playbackState = mutableIntStateOf(Player.STATE_IDLE)
    private val playingState = mutableStateOf(false)

    private val bufferedState = mutableLongStateOf(0L)
    private val progressState = mutableLongStateOf(0L)
    private val durationState = mutableLongStateOf(0L)
    private val seekingState = mutableLongStateOf(-1L)

    private val castingState = mutableStateOf(false)

    val status get() =
        if (castingState.value) {
            when (playbackState.intValue) {
                Player.STATE_BUFFERING -> Status.BUFFERING
                Player.STATE_ENDED -> Status.ENDED
                else ->
                    when {
                        playingState.value -> Status.PLAYING
                        else -> Status.PAUSED
                    }
            }
        } else {
            when (playbackState.intValue) {
                Player.STATE_BUFFERING -> Status.BUFFERING
                Player.STATE_ENDED -> Status.ENDED
                else ->
                    when {
                        playingState.value -> Status.PLAYING
                        else -> Status.PAUSED
                    }
            }
        }

    val loading: Boolean get() = status == Status.BUFFERING

    val bufferedMs: Long get() = bufferedState.longValue
    val progressMs: Long get() = progressState.longValue
    val durationMs: Long get() = durationState.longValue
    val seekingMs: Long get() = seekingState.longValue

    val casting: Boolean get() = castingState.value
    val seeking: Boolean get() = seekingState.longValue >= 0

    fun seek(ms: Long) {
        if (durationMs > 0) {
            seekingState.longValue = ms.fastCoerceIn(0L, durationMs)
        }
    }

    fun commitSeek() {
        val timestamp = seekingState.longValue
        if (timestamp > 0) {
            player.seekTo(timestamp)
            if (casting && status == Status.PAUSED) {
                player.play()
            }
            seekingState.longValue = -1L
        }
    }

    @UnstableApi
    private val playerStateObserver =
        player.observeState(
            Player.EVENT_IS_PLAYING_CHANGED,
            Player.EVENT_PLAYBACK_STATE_CHANGED,
            Player.EVENT_PLAY_WHEN_READY_CHANGED,
            Player.EVENT_AVAILABLE_COMMANDS_CHANGED,
            Player.EVENT_DEVICE_INFO_CHANGED,
            Player.EVENT_IS_LOADING_CHANGED,
        ) {
            updateProgress()
        }

    fun updateProgress() {
        playbackState.intValue = player.playbackState
        playingState.value = player.isPlaying

        bufferedState.longValue = player.bufferedPosition
        progressState.longValue = player.currentPosition
        durationState.longValue = player.duration

        castingState.value = player.deviceInfo.playbackType == DeviceInfo.PLAYBACK_TYPE_REMOTE
    }

    @UnstableApi
    suspend fun observe(): Nothing = playerStateObserver.observe()
}

@UnstableApi
@Composable
fun rememberPlayerState(player: Player): PlayerState {
    val state = remember(player) { PlayerState(player) }

    LaunchedEffect(state, state.status) {
        state.updateProgress()
        while (state.status == PlayerState.Status.PLAYING) {
            delay(50)
            state.updateProgress()
        }
    }

    LaunchedEffect(state) {
        state.observe()
    }

    return state
}
