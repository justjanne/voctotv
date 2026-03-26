/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.theme.textShadow
import de.justjanne.voctotv.mobile.ui.useSystemUi
import de.justjanne.voctotv.voctoweb.model.LectureModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlay(
    viewModel: PlayerViewModel,
    lecture: LectureModel?,
    showTitle: Boolean,
    showPreview: Boolean,
    isFullscreen: Boolean,
    onFullscreen: () -> Unit,
    onDescription: (Boolean) -> Unit,
    back: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val systemUi = useSystemUi()
    val uiVisible = remember { mutableStateOf(false) }
    val uiForced =
        remember {
            derivedStateOf {
                viewModel.playerState.casting || viewModel.playerState.loading
            }
        }

    PlayerOverlayLayout(
        contentPadding = contentPadding,
        visible = uiVisible.value || uiForced.value,
        enabled = !uiForced.value,
        centerVisible = !viewModel.playerState.seeking,
        onClick = {
            if (uiVisible.value && isFullscreen) {
                systemUi?.hideSystemUi()
            }
            uiVisible.value = !uiVisible.value
        },
        top = {
            PlayerNavigationIcon(
                back = { back() }
            )
            Row(Modifier.weight(1f)) {
                if (showTitle) {
                    VideoTitle(
                        lecture = lecture,
                        onClick = { onDescription(true) },
                    )
                }
            }
            PlayerActions(viewModel, lecture)
        },
        bottom = {
            Box(contentAlignment = Alignment.BottomCenter) {
                if (showPreview) {
                    Previewbar(viewModel)
                }
                Row(
                    modifier =
                        Modifier
                            .padding(horizontal = 22.dp)
                            .padding(top = 16.dp)
                            .graphicsLayer { alpha = if (showPreview && viewModel.playerState.seeking) 0f else 1f },
                ) {
                    Text(
                        text = formatTime(
                            if (!showPreview && viewModel.playerState.seeking) viewModel.playerState.seekingMs
                            else viewModel.playerState.progressMs
                        ),
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
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Seekbar(viewModel.playerState)
                }
                IconButton(onClick = onFullscreen) {
                    Icon(
                        painterResource(
                            if (isFullscreen) R.drawable.ic_fullscreen_exit else R.drawable.ic_fullscreen,
                        ),
                        contentDescription =
                            stringResource(
                                if (isFullscreen) R.string.action_fullscreen_exit else R.string.action_fullscreen_enter,
                            ),
                    )
                }
            }
        },
        center = {
            PlayPauseButton(
                viewModel.playerState.status,
                onPlay = {
                    viewModel.mediaSession.player.play()
                },
                onPause = viewModel.mediaSession.player::pause,
                onReplay = {
                    viewModel.mediaSession.player.seekToDefaultPosition()
                    viewModel.mediaSession.player.play()
                },
            )
        }
    )
}
