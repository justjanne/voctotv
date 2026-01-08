/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.voctoweb.model.LectureModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerButtons(
    player: Player,
    playerState: PlayerState,
    lecture: LectureModel?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start,
        ) {
        }

        PlayPauseButton(
            status = playerState.status,
            onPlay = player::play,
            onPause = player::pause,
            onReplay = {
                player.seekToDefaultPosition()
                player.play()
            },
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
        ) {
            CaptionSelection(player, playerState)
            AudioSelection(player, playerState, lecture)
        }
    }
}
