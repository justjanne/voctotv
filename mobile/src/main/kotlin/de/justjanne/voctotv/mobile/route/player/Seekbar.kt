/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState

object SeekbarDefaults {
    val ThumbSize = 16.dp
    val FocusedHeight = 6.dp
    val UnfocusedHeight = 3.dp
}

@OptIn(UnstableApi::class)
@Composable
fun Seekbar(playerState: PlayerState) {
    val layoutWidth = remember { mutableFloatStateOf(0f) }
    val dragState =
        rememberDraggableState {
            val start = if (playerState.seeking) playerState.seekingMs else playerState.progressMs
            val scaleFactor = playerState.durationMs.toFloat() / layoutWidth.floatValue
            playerState.seek((start + it * scaleFactor).toLong())
        }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val progressColor = MaterialTheme.colorScheme.primaryContainer
    val bufferedColor = MaterialTheme.colorScheme.outline
    val backgroundColor = MaterialTheme.colorScheme.outlineVariant
    val thumbColor = MaterialTheme.colorScheme.inverseSurface

    Box(
        modifier =
            Modifier
                .focusRequester(focusRequester)
                .padding(start = 32.dp, end = 32.dp, bottom = 20.dp)
                .minimumInteractiveComponentSize()
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {},
                        onTap = {
                            val scaleFactor = playerState.durationMs.toFloat() / layoutWidth.floatValue
                            playerState.seek((it.x * scaleFactor).toLong())
                            playerState.commitSeek()
                        },
                    )
                }.draggable(
                    state = dragState,
                    orientation = Orientation.Horizontal,
                    startDragImmediately = playerState.seeking,
                    onDragStarted = {
                        val scaleFactor = playerState.durationMs.toFloat() / layoutWidth.floatValue
                        playerState.seek((it.x * scaleFactor).toLong())
                    },
                    onDragStopped = {
                        playerState.commitSeek()
                    },
                ).onLayoutRectChanged {
                    layoutWidth.floatValue = it.width.toFloat()
                }.drawBehind {
                    val thumb = SeekbarDefaults.ThumbSize.toPx()
                    val focusedHeight = SeekbarDefaults.FocusedHeight.toPx()
                    val unfocusedHeight = SeekbarDefaults.UnfocusedHeight.toPx()

                    val buffered = playerState.bufferedMs.toFloat() / playerState.durationMs.toFloat()
                    val progress = playerState.progressMs.toFloat() / playerState.durationMs.toFloat()

                    val currentTimestamp = if (playerState.seeking) playerState.seekingMs else playerState.progressMs
                    val seekProgress = currentTimestamp.toFloat() / playerState.durationMs.toFloat()

                    val currentHeight = if (playerState.seeking) focusedHeight else unfocusedHeight

                    val currentWidth = size.width - thumb

                    val barOffset = Offset((size.width - currentWidth) / 2, (size.height - currentHeight) / 2)
                    val thumbOffset = Offset(seekProgress * currentWidth + thumb / 2, size.height / 2)

                    drawRect(backgroundColor, size = Size(currentWidth, currentHeight), topLeft = barOffset)
                    drawRect(
                        bufferedColor,
                        size = Size(buffered * currentWidth, currentHeight),
                        topLeft = barOffset,
                    )
                    drawRect(progressColor, size = Size(progress * currentWidth, currentHeight), topLeft = barOffset)
                    drawCircle(thumbColor, radius = thumb / 2, center = thumbOffset)
                },
    ) {
    }
}
