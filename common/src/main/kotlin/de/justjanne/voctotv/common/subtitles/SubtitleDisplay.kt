/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.subtitles

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup
import de.justjanne.voctotv.common.player.PlayerState

@Composable
fun BoxScope.SubtitleDisplay(
    player: Player,
    playerState: PlayerState,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    containerColor: Color = Color.Black.copy(alpha = 0.8f),
    contentColor: Color = Color.White,
) {
    val currentCue = remember { mutableStateOf<CueGroup?>(null) }
    DisposableEffect(player) {
        val listener =
            object : Player.Listener {
                override fun onCues(cueGroup: CueGroup) {
                    currentCue.value = cueGroup
                }
            }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    val aspectRatioModifier =
        if (playerState.aspectRatio > 0f) {
            Modifier.aspectRatio(playerState.aspectRatio)
        } else {
            Modifier
        }

    BoxWithConstraints(
        modifier =
            Modifier
                .align(Alignment.Center)
                .then(aspectRatioModifier)
                .fillMaxSize()
                .padding(contentPadding),
    ) {
        val lineSize = constraints.maxHeight / 20f
        val padding = with(LocalDensity.current) { lineSize.toDp() }
        val lineHeight = with(LocalDensity.current) { lineSize.toSp() }

        Column(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = padding, start = padding, end = padding),
        ) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_START && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight, containerColor, contentColor) }
        }
        Column(
            Modifier
                .align(Alignment.Center)
                .padding(start = padding, end = padding),
        ) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_MIDDLE && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight, containerColor, contentColor) }
        }
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = padding, start = padding, end = padding),
        ) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_END || it.line < 0 }
                ?.forEach { LineDisplay(it, lineHeight, containerColor, contentColor) }
        }
    }
}
