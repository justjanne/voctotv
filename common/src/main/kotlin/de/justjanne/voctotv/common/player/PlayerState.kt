package de.justjanne.voctotv.common.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.util.fastCoerceIn
import androidx.media3.common.DeviceInfo
import androidx.media3.common.Player
import kotlinx.coroutines.delay

class PlayerState {
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

    val status: Status get() =
        when (playbackState.intValue) {
            Player.STATE_IDLE, Player.STATE_BUFFERING -> Status.BUFFERING
            Player.STATE_ENDED -> Status.ENDED
            else ->
                when {
                    playingState.value -> Status.PLAYING
                    else -> Status.PAUSED
                }
        }
    val loading: Boolean get() = status == Status.BUFFERING

    val bufferedMs: Long get() = bufferedState.longValue
    val progressMs: Long get() = progressState.longValue
    val durationMs: Long get() = durationState.longValue
    val seekingMs: Long get() = seekingState.longValue

    val casting: Boolean get() = castingState.value
    val seeking: Boolean get() = seekingState.longValue >= 0

    fun updateProgress(player: Player) {
        bufferedState.longValue = player.bufferedPosition
        progressState.longValue = player.currentPosition
        durationState.longValue = player.duration
    }

    fun seek(ms: Long) {
        if (durationMs > 0) {
            seekingState.longValue = ms.fastCoerceIn(0L, durationMs)
        }
    }

    fun commitSeek(player: Player) {
        val timestamp = seekingState.longValue
        if (timestamp > 0) {
            player.seekTo(timestamp)
            if (casting && status == Status.PAUSED) {
                player.play()
            }
            seekingState.longValue = -1L
        }
    }

    val listener: Player.Listener = Listener(this)

    class Listener(
        private val state: PlayerState,
    ) : Player.Listener {
        override fun onDeviceInfoChanged(deviceInfo: DeviceInfo) {
            state.castingState.value = deviceInfo.playbackType == DeviceInfo.PLAYBACK_TYPE_REMOTE
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            state.playingState.value = isPlaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            state.playbackState.intValue = playbackState
        }
    }
}

@Composable
fun rememberPlayerState(player: Player): PlayerState {
    val state = remember { PlayerState() }

    LaunchedEffect(player, state.status) {
        state.updateProgress(player)
        while (state.status == PlayerState.Status.PLAYING) {
            delay(50)
            state.updateProgress(player)
        }
    }

    DisposableEffect(player) {
        player.addListener(state.listener)
        onDispose {
            player.removeListener(state.listener)
        }
    }

    return state
}
