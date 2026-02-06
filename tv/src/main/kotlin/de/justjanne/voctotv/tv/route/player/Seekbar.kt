/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.MaterialTheme

object SeekbarDefaults {
    val ThumbSize = 16.dp
    val FocusedHeight = 6.dp
    val UnfocusedHeight = 3.dp
}

@OptIn(UnstableApi::class)
@Composable
fun Seekbar(
    player: Player,
    interactionSource: MutableInteractionSource,
    seekBack: () -> Unit,
    seekForward: () -> Unit,
) {
    val progressState = rememberProgressStateWithTickInterval(player)
    val isFocused = interactionSource.collectIsFocusedAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val progressColor = MaterialTheme.colorScheme.primaryContainer
    val bufferedColor = MaterialTheme.colorScheme.border
    val backgroundColor = MaterialTheme.colorScheme.borderVariant
    val thumbColor = MaterialTheme.colorScheme.inverseSurface

    Box(
        modifier =
            Modifier
                .focusRequester(focusRequester)
                .padding(horizontal = 32.dp)
                .height(20.dp)
                .fillMaxWidth()
                .onKeyEvent {
                    if (it.type == KeyEventType.KeyDown) {
                        when (it.key) {
                            Key.DirectionRight -> {
                                seekForward()
                                true
                            }

                            Key.DirectionLeft -> {
                                seekBack()
                                true
                            }

                            else -> false
                        }
                    } else {
                        false
                    }
                }.clickable(interactionSource = interactionSource, indication = null) {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                }.drawBehind {
                    val thumb = SeekbarDefaults.ThumbSize.toPx()
                    val focusedHeight = SeekbarDefaults.FocusedHeight.toPx()
                    val unfocusedHeight = SeekbarDefaults.UnfocusedHeight.toPx()

                    val buffered = progressState.bufferedPositionMs.toFloat() / progressState.durationMs.toFloat()
                    val progress = progressState.currentPositionMs.toFloat() / progressState.durationMs.toFloat()

                    val currentHeight = if (isFocused.value) focusedHeight else unfocusedHeight

                    val currentWidth = size.width - thumb

                    val barOffset = Offset((size.width - currentWidth) / 2, (size.height - currentHeight) / 2)
                    val thumbOffset = Offset(progress * currentWidth + thumb / 2, size.height / 2)

                    drawRect(backgroundColor, size = Size(currentWidth, currentHeight), topLeft = barOffset)
                    drawRect(
                        bufferedColor,
                        size = Size(buffered * currentWidth, currentHeight),
                        topLeft = barOffset,
                    )
                    drawRect(progressColor, size = Size(progress * currentWidth, currentHeight), topLeft = barOffset)
                    if (isFocused.value) {
                        drawCircle(thumbColor, radius = thumb / 2, center = thumbOffset)
                    }
                },
    ) {
    }
}
