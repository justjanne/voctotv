/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui.player

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.tv.material3.IconButton
import androidx.tv.material3.LocalContentColor
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.tv.R

@Composable
fun PlayPauseButton(
    status: PlayerState.Status,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReplay: () -> Unit,
) {
    IconButton(
        onClick = {
            when (status) {
                PlayerState.Status.BUFFERING -> Unit
                PlayerState.Status.PLAYING -> onPause()
                PlayerState.Status.PAUSED -> onPlay()
                PlayerState.Status.ENDED -> onReplay()
            }
        },
        modifier = Modifier.requiredSize(androidx.tv.material3.IconButtonDefaults.LargeButtonSize),
    ) {
        when (status) {
            PlayerState.Status.BUFFERING ->
                CircularProgressIndicator()

            PlayerState.Status.PLAYING ->
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = "Pause",
                    tint = LocalContentColor.current,
                )

            PlayerState.Status.PAUSED ->
                Icon(
                    painter = painterResource(R.drawable.ic_play_arrow),
                    contentDescription = "Play",
                    tint = LocalContentColor.current,
                )

            PlayerState.Status.ENDED ->
                Icon(
                    painter = painterResource(R.drawable.ic_replay),
                    contentDescription = "Play Again",
                    tint = LocalContentColor.current,
                )
        }
    }
}
