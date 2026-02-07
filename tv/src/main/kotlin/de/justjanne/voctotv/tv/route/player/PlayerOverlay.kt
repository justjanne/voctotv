/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.player

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.tv.material3.IconButton
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.rememberWindowState
import de.justjanne.voctotv.tv.ui.theme.PlayerScrimBottom
import de.justjanne.voctotv.tv.ui.theme.textShadow
import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
    showInfo: () -> Unit,
) {
    val uiVisible = remember { mutableStateOf(false) }

    val mainInteractionSource = remember { MutableInteractionSource() }
    val seekbarInteractionSource = remember { MutableInteractionSource() }

    val windowState = rememberWindowState()

    BackHandler(uiVisible.value) {
        uiVisible.value = false
    }

    val hideJob = remember { mutableStateOf<Job?>(null) }
    val hideScope = rememberCoroutineScope()
    val showUi =
        remember {
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
    val seekBack =
        remember {
            {
                viewModel.mediaSession.player.pause()
                viewModel.mediaSession.player.seekBack()
                seeking.value = true
                showUi()
            }
        }
    val seekForward =
        remember {
            {
                viewModel.mediaSession.player.pause()
                viewModel.mediaSession.player.seekForward()
                seeking.value = true
                showUi()
            }
        }

    DisposableEffect(viewModel.mediaSession.player, hideJob, uiVisible) {
        val listener =
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        windowState?.disableSleep()
                        seeking.value = false
                        hideJob.value?.cancel()
                        hideJob.value =
                            hideScope.launch {
                                delay(5.seconds)
                                uiVisible.value = false
                            }
                    } else {
                        windowState?.enableSleep()
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
        modifier =
            Modifier
                .fillMaxSize()
                .clickable(mainInteractionSource, indication = null, enabled = !uiVisible.value) {
                    uiVisible.value = true
                }.onPreviewKeyEvent {
                    if (it.type == KeyEventType.KeyDown) {
                        when (it.key) {
                            Key.MediaPlayPause -> {
                                if (viewModel.mediaSession.player.isPlaying) {
                                    viewModel.mediaSession.player.pause()
                                } else {
                                    viewModel.mediaSession.player.play()
                                }
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
                                } else {
                                    false
                                }
                            }

                            Key.DirectionLeft -> {
                                if (!uiVisible.value) {
                                    seekBack()
                                    true
                                } else {
                                    false
                                }
                            }

                            Key.DirectionUp,
                            Key.DirectionDown,
                            Key.DirectionCenter,
                            Key.DirectionUpLeft,
                            Key.DirectionDownLeft,
                            Key.DirectionUpRight,
                            Key.DirectionDownRight,
                                -> {
                                if (!uiVisible.value) {
                                    showUi()
                                    true
                                } else {
                                    false
                                }
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                },
    ) {
        TitleOverlay(
            uiVisible.value,
            lecture,
        )

        AnimatedVisibility(
            uiVisible.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Box {
                Previewbar(
                    viewModel,
                    viewModel.mediaSession.player,
                    seekbarInteractionSource,
                    seeking,
                    modifier = Modifier.align(Alignment.TopCenter),
                )
                Column(
                    modifier =
                        Modifier
                            .background(PlayerScrimBottom)
                            .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(Modifier.padding(start = 32.dp, end = 32.dp, top = 32.dp)) {
                        Text(
                            text = formatTime(viewModel.playerState.progressMs),
                            style =
                                MaterialTheme.typography.labelLarge.copy(
                                    shadow = MaterialTheme.colorScheme.textShadow,
                                ),
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = formatTime(viewModel.playerState.durationMs),
                            style =
                                MaterialTheme.typography.labelLarge.copy(
                                    shadow = MaterialTheme.colorScheme.textShadow,
                                ),
                        )
                    }
                    Seekbar(viewModel.mediaSession.player, seekbarInteractionSource, seekBack, seekForward)
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                        ) {
                            IconButton(onClick = showInfo) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_info),
                                    contentDescription = stringResource(R.string.player_info),
                                    tint = LocalContentColor.current,
                                )
                            }
                        }

                        PlayPauseButton(
                            status = viewModel.playerState.status,
                            onPlay = viewModel.mediaSession.player::play,
                            onPause = viewModel.mediaSession.player::pause,
                            onReplay = {
                                viewModel.mediaSession.player.seekToDefaultPosition()
                                viewModel.mediaSession.player.play()
                            },
                        )

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        ) {
                            CaptionSelection(viewModel.mediaSession.player, viewModel.playerState)
                            AudioSelection(viewModel.mediaSession.player, viewModel.playerState, lecture)
                        }
                    }
                }
            }
        }
    }
}
