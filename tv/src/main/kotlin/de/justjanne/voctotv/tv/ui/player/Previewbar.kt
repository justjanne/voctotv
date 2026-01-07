/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.Border
import androidx.tv.material3.Surface
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.previews.rememberPreviewThumbnail
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel

private val thumb = 16.dp

@OptIn(UnstableApi::class)
@Composable
fun Previewbar(
    viewModel: PlayerViewModel,
    player: Player,
    interactionSource: InteractionSource,
    visible: State<Boolean>,
    modifier: Modifier = Modifier,
) {
    val playbackState = rememberPlayPauseButtonState(player)
    val progressState = rememberProgressStateWithTickInterval(player)
    val isFocused = interactionSource.collectIsFocusedAsState()
    val allCues = viewModel.previews.collectAsState()

    val currentCue =
        remember {
            derivedStateOf {
                val currentTimestamp = progressState.currentPositionMs * 1000
                allCues.value.firstOrNull { it.startUs <= currentTimestamp && it.endUs >= currentTimestamp }?.data
            }
        }

    val currentThumbnail = rememberPreviewThumbnail(currentCue)

    BoxWithConstraints(
        modifier =
            modifier
                .padding(horizontal = 32.dp)
                .height(96.dp)
                .fillMaxWidth(),
    ) {
        if (visible.value && playbackState.showPlay) {
            Surface(
                modifier =
                    Modifier
                        .graphicsLayer {
                            val thumb = thumb.toPx()
                            val progress =
                                progressState.currentPositionMs.toFloat() / progressState.durationMs.toFloat()
                            val currentWidth = constraints.maxWidth - thumb
                            val translation = (progress * currentWidth + thumb / 2) - size.width / 2
                            translationX = translation.coerceIn(0f, currentWidth - size.width)
                            translationY = -size.height + 24.dp.toPx()
                            alpha = if (isFocused.value) 1f else 0f
                        },
                shape = RoundedCornerShape(8.dp),
                border = Border(
                    BorderStroke(
                        width = 3.dp,
                        color = Color(red = 28, green = 27, blue = 31, alpha = 204)
                    )
                ),
            ) {
                Box(Modifier
                    .height(90.dp)
                    .aspectRatio(16f / 9f)) {
                    AsyncImage(
                        model = currentThumbnail.value,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
