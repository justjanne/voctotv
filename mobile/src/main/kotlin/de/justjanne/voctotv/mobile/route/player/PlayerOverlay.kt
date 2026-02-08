/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.cast.MediaRouteButton
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.rememberWindowState
import de.justjanne.voctotv.mobile.ui.theme.PlayerScrimBottom
import de.justjanne.voctotv.mobile.ui.theme.PlayerScrimTop
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.mobile.ui.theme.textShadow
import de.justjanne.voctotv.voctoweb.model.LectureModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
    sidebarVisible: MutableState<Boolean>,
    contentPadding: PaddingValues,
    back: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    val uiVisible = remember { mutableStateOf(false) }
    val uiForced =
        remember {
            derivedStateOf {
                viewModel.playerState.casting || viewModel.playerState.loading
            }
        }

    val mainInteractionSource = remember { MutableInteractionSource() }

    val windowState = rememberWindowState()

    BackHandler(uiVisible.value && !uiForced.value) {
        uiVisible.value = false
    }

    val context = LocalActivity.current

    val isLandscape =
        remember {
            derivedStateOf {
                viewModel.playerState.aspectRatio > 1f
            }
        }
    // Set Orientation
    DisposableEffect(context, viewModel.playerState.casting, isLandscape.value) {
        if (!viewModel.playerState.casting && context != null && isLandscape.value) {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

            onDispose {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        } else {
            sidebarVisible.value = false
            onDispose {}
        }
    }

    // Hide System Bars
    DisposableEffect(windowState, viewModel.playerState.casting) {
        if (!viewModel.playerState.casting) {
            windowState?.hideSystemUi()
            onDispose {
                windowState?.showSystemUi()
            }
        } else {
            onDispose {}
        }
    }

    val activelyPlaying =
        remember {
            derivedStateOf {
                when (viewModel.playerState.status) {
                    PlayerState.Status.BUFFERING, PlayerState.Status.PLAYING -> !viewModel.playerState.casting
                    PlayerState.Status.PAUSED, PlayerState.Status.ENDED -> false
                }
            }
        }
    // Keep Screen On
    DisposableEffect(windowState, activelyPlaying.value) {
        if (activelyPlaying.value) {
            windowState?.disableSleep()
            onDispose {
                windowState?.enableSleep()
            }
        } else {
            onDispose {}
        }
    }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable(mainInteractionSource, indication = null, enabled = !uiForced.value) {
                    if (uiVisible.value && !viewModel.playerState.casting) {
                        windowState?.hideSystemUi()
                    }
                    uiVisible.value = !uiVisible.value
                },
    ) {
        AnimatedVisibility(
            uiVisible.value || uiForced.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Row(
                modifier =
                    Modifier
                        .background(PlayerScrimTop)
                        .padding(32.dp)
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            top = contentPadding.calculateTopPadding(),
                        ).fillMaxWidth(),
            ) {
                IconButton(
                    onClick = back,
                ) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.action_back),
                    )
                }
                lecture
                    ?.let {
                        Column(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable(
                                        enabled = !viewModel.playerState.casting && isLandscape.value,
                                    ) { sidebarVisible.value = true }
                                    .padding(start = 6.dp),
                        ) {
                            Text(
                                text = lecture.conferenceTitle,
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = SubtitleAlpha),
                                        shadow = MaterialTheme.colorScheme.textShadow,
                                    ),
                                maxLines = 1,
                            )
                            Text(
                                text = lecture.title,
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shadow = MaterialTheme.colorScheme.textShadow,
                                    ),
                                maxLines = 2,
                            )
                        }

                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        ) {
                            CaptionSelection(viewModel.mediaSession.player, viewModel.playerState)
                            AudioSelection(viewModel.mediaSession.player, viewModel.playerState, lecture)
                            MediaRouteButton()
                        }
                    }
            }
        }

        AnimatedVisibility(
            uiVisible.value || uiForced.value,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier =
                    Modifier
                        .background(PlayerScrimBottom)
                        .padding(
                            top = 32.dp,
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Previewbar(viewModel, viewModel.playerState)
                    Row(
                        modifier =
                            Modifier
                                .padding(start = 32.dp, end = 32.dp, top = 16.dp)
                                .graphicsLayer { alpha = if (viewModel.playerState.seeking) 0f else 1f },
                    ) {
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
                }
                Seekbar(viewModel.playerState)
            }
        }

        AnimatedVisibility(
            (uiVisible.value || uiForced.value) && !viewModel.playerState.seeking,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            PlayPauseButton(
                viewModel.playerState.status,
                onPlay = {
                    viewModel.mediaSession.player.play()
                    if (!viewModel.playerState.casting) {
                        windowState?.hideSystemUi()
                    }
                },
                onPause = viewModel.mediaSession.player::pause,
                onReplay = {
                    viewModel.mediaSession.player.seekToDefaultPosition()
                    viewModel.mediaSession.player.play()
                },
            )
        }
    }
}
