package de.justjanne.voctotv.ui.player

import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import de.justjanne.voctotv.R
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

    val seekPositionMs = remember { mutableStateOf<Long?>(null) }

    val context = LocalActivity.current
    DisposableEffect(viewModel.mediaSession.player, hideJob, uiVisible) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    context?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    hideJob.value?.cancel()
                    hideJob.value = hideScope.launch {
                        delay(5.seconds)
                        uiVisible.value = false
                    }
                } else {
                    context?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
        modifier = Modifier
            .fillMaxSize()
            .clickable(mainInteractionSource, indication = null, enabled = !uiVisible.value) {
                uiVisible.value = true
            }
    ) {
        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            lecture
                ?.let {
                    Row(
                        modifier = Modifier
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color(red = 28, green = 27, blue = 31, alpha = 204),
                                        Color(red = 28, green = 27, blue = 31, alpha = 0),
                                    )
                                )
                            )
                            .padding(32.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = lecture.conferenceTitle,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White.copy(alpha = 0.65f),
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 1,
                            )
                            Text(
                                text = lecture.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.White,
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        offset = Offset(x = 2f, y = 4f),
                                        blurRadius = 2f
                                    )
                                ),
                                maxLines = 2,
                            )
                        }

                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        ) {
                            CaptionSelection(viewModel.mediaSession.player, playPauseState)
                            AudioSelection(viewModel.mediaSession.player, playPauseState, lecture)
                        }
                    }
                }
        }

        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            FilledIconButton(
                onClick = {
                    if (viewModel.mediaSession.player.isPlaying) {
                        viewModel.mediaSession.player.pause()
                    } else viewModel.mediaSession.player.play()
                },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White,
                    disabledContainerColor = Color.White,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Black,
                ),
                modifier = Modifier.requiredSize(96.dp)
            ) {
                Icon(
                    painter = painterResource(if (playPauseState.showPlay) R.drawable.ic_play_arrow else R.drawable.ic_pause),
                    contentDescription = if (playPauseState.showPlay) "Play" else "Pause",
                    tint = LocalContentColor.current,
                )
            }
        }

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
                    seekPositionMs,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
                Column(
                    modifier = Modifier
                        .heightIn(min=96.dp)
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
                    Seekbar(viewModel.mediaSession.player, seekPositionMs)
                }
            }
        }
    }
}
