/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui.player

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.R

private val ConsentSize = 32.dp
private val ContainerSize = 96.dp

@Composable
fun PlayPauseButton(
    status: PlayerState.Status,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onReplay: () -> Unit,
) {
    FilledIconButton(
        onClick = {
            when (status) {
                PlayerState.Status.BUFFERING -> Unit
                PlayerState.Status.PLAYING -> onPause()
                PlayerState.Status.PAUSED -> onPlay()
                PlayerState.Status.ENDED -> onReplay()
            }
        },
        colors =
            IconButtonDefaults.filledIconButtonColors(
                containerColor = Color.White,
                disabledContainerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.Black,
            ),
        modifier =
            Modifier
                .requiredSize(ContainerSize)
                .shadow(elevation = 8.dp, shape = CircleShape),
    ) {
        when (status) {
            PlayerState.Status.BUFFERING ->
                CircularProgressIndicator(
                    modifier = Modifier.requiredSize(ConsentSize),
                )

            PlayerState.Status.PLAYING ->
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = "Pause",
                    modifier = Modifier.requiredSize(ConsentSize),
                )

            PlayerState.Status.PAUSED ->
                Icon(
                    painter = painterResource(R.drawable.ic_play_arrow),
                    contentDescription = "Play",
                    modifier = Modifier.requiredSize(ConsentSize),
                )

            PlayerState.Status.ENDED ->
                Icon(
                    painter = painterResource(R.drawable.ic_replay),
                    contentDescription = "Play Again",
                    modifier = Modifier.requiredSize(ConsentSize),
                )
        }
    }
}
