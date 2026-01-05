package de.justjanne.voctotv.ui.player

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.util.formatTime
import de.justjanne.voctotv.viewmodel.PlayerViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
) {
    val progressState = rememberProgressStateWithTickInterval(viewModel.mediaSession.player)
    val playPauseState = rememberPlayPauseButtonState(viewModel.mediaSession.player)

    val uiVisible = remember { mutableStateOf(false) }

    val mainInteractionSource = remember { MutableInteractionSource() }
    val seekbarInteractionSource = remember { MutableInteractionSource() }

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

    val seeking = remember { mutableStateOf(false) }
    LaunchedEffect(uiVisible.value) {
        if (!uiVisible.value) {
            seeking.value = false
        }
    }
    val seekBack = remember {
        {
            viewModel.mediaSession.player.pause()
            viewModel.mediaSession.player.seekBack()
            seeking.value = true
            showUi()
        }
    }
    val seekForward = remember {
        {
            viewModel.mediaSession.player.pause()
            viewModel.mediaSession.player.seekForward()
            seeking.value = true
            showUi()
        }
    }

    DisposableEffect(viewModel.mediaSession.player, hideJob, uiVisible) {
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

        viewModel.mediaSession.player.addListener(listener)
        onDispose {
            viewModel.mediaSession.player.removeListener(listener)
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
                            if (viewModel.mediaSession.player.isPlaying) viewModel.mediaSession.player.pause()
                            else viewModel.mediaSession.player.play()
                            true
                        }

                        Key.MediaPlay -> {
                            viewModel.mediaSession.player.play()
                            true
                        }

                        Key.MediaPause -> {
                            viewModel.mediaSession.player.pause()
                            true
                        }

                        Key.MediaFastForward, Key.MediaStepForward, Key.MediaSkipForward -> {
                            seekForward()
                            true
                        }

                        Key.MediaRewind, Key.MediaStepBackward, Key.MediaSkipBackward -> {
                            seekBack()
                            true
                        }

                        Key.DirectionRight -> {
                            if (!uiVisible.value) {
                                seekForward()
                                true
                            } else false
                        }

                        Key.DirectionLeft -> {
                            if (!uiVisible.value) {
                                seekBack()
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

        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Box {
                Previewbar(
                    viewModel,
                    viewModel.mediaSession.player,
                    seekbarInteractionSource,
                    seeking,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(red = 28, green = 27, blue = 31, alpha = 0),
                                    Color(red = 28, green = 27, blue = 31, alpha = 204)
                                )
                            )
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
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
                    Seekbar(viewModel.mediaSession.player, seekbarInteractionSource, seekBack, seekForward)
                    PlayerButtons(viewModel.mediaSession.player, playPauseState, lecture)
                }
            }
        }
    }
}
