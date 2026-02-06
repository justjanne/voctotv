/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.R

object PlayPauseButtonDefaults {
    val ContentSize = 32.dp
    val ContainerSize = 96.dp

    val Shape = CircleShape
    val Elevation = 8.dp
}

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
                containerColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface,
            ),
        modifier =
            Modifier
                .requiredSize(PlayPauseButtonDefaults.ContainerSize)
                .shadow(elevation = PlayPauseButtonDefaults.Elevation, shape = PlayPauseButtonDefaults.Shape),
    ) {
        when (status) {
            PlayerState.Status.BUFFERING ->
                CircularProgressIndicator(
                    modifier = Modifier.requiredSize(PlayPauseButtonDefaults.ContentSize),
                )

            PlayerState.Status.PLAYING ->
                Icon(
                    painter = painterResource(R.drawable.ic_pause),
                    contentDescription = stringResource(R.string.action_pause),
                    modifier = Modifier.requiredSize(PlayPauseButtonDefaults.ContentSize),
                )

            PlayerState.Status.PAUSED ->
                Icon(
                    painter = painterResource(R.drawable.ic_play_arrow),
                    contentDescription = stringResource(R.string.action_play),
                    modifier = Modifier.requiredSize(PlayPauseButtonDefaults.ContentSize),
                )

            PlayerState.Status.ENDED ->
                Icon(
                    painter = painterResource(R.drawable.ic_replay),
                    contentDescription = stringResource(R.string.action_restart),
                    modifier = Modifier.requiredSize(PlayPauseButtonDefaults.ContentSize),
                )
        }
    }
}
