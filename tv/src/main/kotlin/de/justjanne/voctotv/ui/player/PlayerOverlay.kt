package de.justjanne.voctotv.ui.player

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.mediacccde.model.LectureModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds


@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    lecture: LectureModel?,
    player: Player,
) {
    val progressState = rememberProgressStateWithTickInterval(player)
    val playPauseState = rememberPlayPauseButtonState(player)

    val uiVisible = remember { mutableStateOf(false) }

    val mainInteractionSource = remember { MutableInteractionSource() }

    BackHandler(uiVisible.value) {
        uiVisible.value = false
    }

    val hideJob = remember { mutableStateOf<Job?>(null) }
    val hideScope = rememberCoroutineScope()
    val showUi = remember {
        {
            hideJob.value?.cancel()
            uiVisible.value = true
        }
    }

    DisposableEffect(player, hideJob, uiVisible) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    hideJob.value?.cancel()
                    hideJob.value = hideScope.launch {
                        delay(5.seconds)
                        uiVisible.value = false
                    }
                } else {
                    showUi()
                }
            }
        }

        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .clickable(mainInteractionSource, indication = null, enabled = !uiVisible.value) {
                uiVisible.value = true
            }.onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.MediaPlayPause -> {
                            if (player.isPlaying) player.pause()
                            else player.play()
                            true
                        }

                        Key.MediaPlay -> {
                            player.play()
                            true
                        }

                        Key.MediaPause -> {
                            player.pause()
                            true
                        }

                        Key.MediaFastForward, Key.MediaStepForward, Key.MediaSkipForward -> {
                            player.seekForward()
                            showUi()
                            true
                        }

                        Key.MediaRewind, Key.MediaStepBackward, Key.MediaSkipBackward -> {
                            player.seekBack()
                            showUi()
                            true
                        }

                        Key.DirectionRight -> {
                            if (!uiVisible.value) {
                                player.seekForward()
                                showUi()
                                true
                            } else false
                        }

                        Key.DirectionLeft -> {
                            if (!uiVisible.value) {
                                player.seekBack()
                                showUi()
                                true
                            } else false
                        }

                        Key.DirectionUp,
                        Key.DirectionDown,
                        Key.DirectionCenter,
                        Key.DirectionUpLeft,
                        Key.DirectionDownLeft,
                        Key.DirectionUpRight,
                        Key.DirectionDownRight -> {
                            if (!uiVisible.value) {
                                showUi()
                                true
                            } else false
                        }

                        else -> false
                    }
                } else false
            }
    ) {
        TitleOverlay(
            uiVisible.value,
            lecture
        )

        BottomOverlay(uiVisible.value) {
            Row(Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
                Text(
                    text = formatTime(progressState.currentPositionMs),
                    style = MaterialTheme.typography.labelLarge.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(x = 2f, y = 4f),
                            blurRadius = 2f
                        )
                    ),
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = formatTime(progressState.durationMs),
                    style = MaterialTheme.typography.labelLarge.copy(
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.5f),
                            offset = Offset(x = 2f, y = 4f),
                            blurRadius = 2f
                        )
                    ),
                )
            }
            Seekbar(player)
            PlayerButtons(player, playPauseState, lecture)
        }
    }
}

fun formatTime(timeMs: Long): String {
    if (timeMs == C.TIME_UNSET) {
        return formatTime(0);
    }
    val prefix = if (timeMs < 0) "-" else ""
    val timeMsValue = abs(timeMs)
    val totalSeconds = (timeMsValue + 500) / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) String.format(Locale.ROOT, "%s%d:%02d:%02d", prefix, hours, minutes, seconds)
    else String.format(Locale.ROOT, "%s%02d:%02d", prefix, minutes, seconds)
}
