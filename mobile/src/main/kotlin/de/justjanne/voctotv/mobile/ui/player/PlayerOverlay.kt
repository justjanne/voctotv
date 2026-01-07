/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui.player

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.cast.MediaRouteButton
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.mobile.R
import kotlinx.coroutines.Job

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
    playerState: PlayerState,
    contentPadding: PaddingValues,
    back: () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current

    val uiVisible = remember { mutableStateOf(false) }
    val uiForced =
        remember {
            derivedStateOf {
                playerState.casting || playerState.loading
            }
        }

    val mainInteractionSource = remember { MutableInteractionSource() }

    BackHandler(uiVisible.value && !uiForced.value) {
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

    val context = LocalActivity.current
    DisposableEffect(context, playerState.casting) {
        if (!playerState.casting) {
            context?.apply {
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            }
            context?.window?.apply {
                WindowCompat
                    .getInsetsController(this, decorView)
                    .hide(WindowInsetsCompat.Type.systemBars())
            }
            onDispose {
                context?.apply {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                }
                context?.window?.apply {
                    WindowCompat
                        .getInsetsController(this, decorView)
                        .show(WindowInsetsCompat.Type.systemBars())
                }
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
                    if (uiVisible.value) {
                        context?.window?.apply {
                            WindowCompat
                                .getInsetsController(this, decorView)
                                .hide(WindowInsetsCompat.Type.systemBars())
                        }
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
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(red = 28, green = 27, blue = 31, alpha = 204),
                                    Color(red = 28, green = 27, blue = 31, alpha = 0),
                                ),
                            ),
                        ).padding(32.dp)
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            top = contentPadding.calculateTopPadding(),
                        ).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                IconButton(
                    onClick = back,
                ) {
                    Icon(
                        painterResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                    )
                }
                lecture
                    ?.let {
                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = lecture.conferenceTitle,
                                style =
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                                        shadow =
                                            Shadow(
                                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                                offset = Offset(x = 2f, y = 4f),
                                                blurRadius = 2f,
                                            ),
                                    ),
                                maxLines = 1,
                            )
                            Text(
                                text = lecture.title,
                                style =
                                    MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        shadow =
                                            Shadow(
                                                color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                                offset = Offset(x = 2f, y = 4f),
                                                blurRadius = 2f,
                                            ),
                                    ),
                                maxLines = 2,
                            )
                        }

                        Row(
                            modifier = Modifier,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                        ) {
                            CaptionSelection(viewModel.mediaSession.player, playerState)
                            AudioSelection(viewModel.mediaSession.player, playerState, lecture)
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
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(red = 28, green = 27, blue = 31, alpha = 0),
                                    Color(red = 28, green = 27, blue = 31, alpha = 204),
                                ),
                            ),
                        ).padding(
                            top = 32.dp,
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Previewbar(viewModel, playerState)
                    Row(
                        modifier =
                            Modifier
                                .padding(start = 32.dp, end = 32.dp, top = 16.dp)
                                .graphicsLayer { alpha = if (playerState.seeking) 0f else 1f },
                    ) {
                        Text(
                            text = formatTime(playerState.progressMs),
                            style =
                                MaterialTheme.typography.labelLarge.copy(
                                    shadow =
                                        Shadow(
                                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                            offset = Offset(x = 2f, y = 4f),
                                            blurRadius = 2f,
                                        ),
                                ),
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = formatTime(playerState.durationMs),
                            style =
                                MaterialTheme.typography.labelLarge.copy(
                                    shadow =
                                        Shadow(
                                            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                            offset = Offset(x = 2f, y = 4f),
                                            blurRadius = 2f,
                                        ),
                                ),
                        )
                    }
                }
                Seekbar(playerState)
            }
        }

        AnimatedVisibility(
            (uiVisible.value || uiForced.value) && !playerState.seeking,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            PlayPauseButton(
                playerState.status,
                onPlay = viewModel.mediaSession.player::play,
                onPause = viewModel.mediaSession.player::pause,
                onReplay = {
                    viewModel.mediaSession.player.seekToDefaultPosition()
                    viewModel.mediaSession.player.play()
                },
            )
        }
    }
}
