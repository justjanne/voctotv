package de.justjanne.voctotv.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.state.ProgressStateWithTickInterval
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.IconButton
import androidx.tv.material3.Text
import de.ccc.media.api.LectureModel
import de.justjanne.voctotv.R
import de.justjanne.voctotv.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.time.Duration.Companion.seconds

@Composable
fun rememberPlayer(): Player {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context).build()
    }
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    return player
}

@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel,
) {
    val mediaItem by viewModel.mediaItem.collectAsState()
    val lecture by viewModel.lecture.collectAsState()
    val player = rememberPlayer()

    LaunchedEffect(player, mediaItem) {
        player.clearMediaItems()
        mediaItem?.let {
            player.setMediaItem(it)
        }
        player.prepare()
        player.playWhenReady = true
        player.play()
    }

    Box(Modifier.fillMaxSize()) {
        ContentFrame(
            player = player,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )

        PlayerUi(lecture, player)
    }
}

@OptIn(UnstableApi::class)
@Composable
fun PlayerUi(
    lecture: LectureModel?,
    player: Player,
) {
    val playPauseState = rememberPlayPauseButtonState(player)
    val focusRequester = remember { FocusRequester() }

    val uiVisible = remember { mutableStateOf(true) }

    LaunchedEffect(playPauseState.showPlay, uiVisible.value) {
        if (!playPauseState.showPlay && uiVisible.value) {
            delay(5.seconds)
            uiVisible.value = false

            for (group in player.currentTracks.groups) {
                println(group)
                for (track in 0 until group.length) {
                    val format = group.getTrackFormat(track)
                    println("  $track $format")
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val interactionSource = remember { MutableInteractionSource() }

    BackHandler(uiVisible.value) {
        uiVisible.value = false
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .focusable()
            .focusRequester(focusRequester)
            .clickable(
                indication = null,
                interactionSource = interactionSource,
            ) {
                if (player.isPlaying) {
                    player.pause()
                    uiVisible.value = true
                }
                else player.play()
            }
            .onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.MediaPlay -> {
                            player.play()
                            true
                        }

                        Key.MediaPause -> {
                            player.pause()
                            uiVisible.value = true
                            true
                        }

                        Key.MediaPlayPause -> {
                            if (player.isPlaying) {
                                player.pause()
                                uiVisible.value = true
                            }
                            else player.play()
                            true
                        }

                        Key.DirectionRight,
                        Key.SystemNavigationRight,
                        Key.MediaFastForward,
                        Key.MediaSkipForward,
                        Key.MediaStepForward -> {
                            player.seekForward()
                            uiVisible.value = true
                            true
                        }

                        Key.DirectionLeft,
                        Key.SystemNavigationLeft,
                        Key.MediaRewind,
                        Key.MediaSkipBackward,
                        Key.MediaStepBackward -> {
                            player.seekBack()
                            uiVisible.value = true
                            true
                        }

                        else -> false
                    }
                } else false
            },
    ) {
        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            lecture?.let {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Black.copy(alpha = 0.67f), Color.Transparent)
                            )
                        )
                        .padding(32.dp)
                        .fillMaxWidth()
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
            }
        }

        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.67f))
                        )
                    )
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                PlaybackBar(player)
                Row (horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.Start,
                    ) {

                    }
                    IconButton(
                        interactionSource = interactionSource,
                        onClick = {
                            if (player.isPlaying) {
                                player.pause()
                                uiVisible.value = true
                            }
                            else player.play()
                        },
                    ) {
                        Icon(
                            painter = painterResource(if (playPauseState.showPlay) R.drawable.play_arrow else R.drawable.pause),
                            contentDescription = if (playPauseState.showPlay) "Play" else "Pause",
                            tint = Color.White,
                        )
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        /*
                        val isOpen = remember { mutableStateOf(false) }

                        if (isOpen) {
                            Popup() {
                                Column {
                                    for (language in player.currentTracks) {
                                        Button(onClick = {
                                            player.trackSelectionParameters =
                                                player.trackSelectionParameters.buildUpon()
                                                    .setPreferredAudioLanguages("")
                                        })
                                    }
                                }
                            }
                        }
                        IconButton(
                            interactionSource = interactionSource,
                            onClick = {
                            },
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.audiotrack),
                                contentDescription = "Language",
                                tint = Color.White,
                            )
                        }
                         */
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnScope.PlaybackBar(
    player: Player,
) {
    val progressState = rememberProgressStateWithTickInterval(player)

    Row(
        Modifier.padding(bottom = 8.dp).fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = formatTime(progressState.currentPositionMs),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(x = 2f, y = 4f),
                    blurRadius = 2f
                )
            ),
        )
        Text(
            text = formatTime(progressState.durationMs),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                shadow = Shadow(
                    color = Color.Black.copy(alpha = 0.5f),
                    offset = Offset(x = 2f, y = 4f),
                    blurRadius = 2f
                )
            ),
        )
    }

    SeekbarBackground(progressState)
}

@Composable
fun SeekbarBackground(
    progressState: ProgressStateWithTickInterval,
) {
    Box(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .height(4.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .drawBehind {
                val scale = size.width / progressState.durationMs.toFloat()
                val position = progressState.currentPositionMs.toFloat() * scale
                val buffered = progressState.bufferedPositionMs.toFloat() * scale
                drawRect(Color.Gray.copy(alpha = 0.67f))
                drawRect(Color.White.copy(alpha = 0.67f), size = Size(buffered, size.height))
                drawRect(Color(0xffff6600), size = Size(position, size.height))
            }
    ) {

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
    return if (hours > 0) String.format("%s%d:%02d:%02d", prefix, hours, minutes, seconds)
    else String.format("%s%02d:%02d", prefix, minutes, seconds)
}
