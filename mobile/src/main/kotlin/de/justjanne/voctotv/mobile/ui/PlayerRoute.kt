/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import de.justjanne.voctotv.common.player.rememberPlayerState
import de.justjanne.voctotv.common.subtitles.SubtitleDisplay
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.ui.player.PlayerOverlay

@OptIn(UnstableApi::class)
@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel,
    back: () -> Unit,
) {
    val lecture by viewModel.lecture.collectAsState()
    rememberPlayerState(viewModel)

    Scaffold { contentPadding ->
        Box(Modifier.fillMaxSize()) {
            // Workaround to reset surface after cast switch
            if (viewModel.playerState.casting) {
                ContentFrame(
                    player = viewModel.mediaSession.player,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                    surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                )
            } else {
                ContentFrame(
                    player = viewModel.mediaSession.player,
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(contentPadding),
                    surfaceType = SURFACE_TYPE_SURFACE_VIEW,
                )
            }

            SubtitleDisplay(viewModel.mediaSession.player, viewModel.playerState, contentPadding)
            PlayerOverlay(viewModel, lecture, contentPadding, back)
        }
    }
}
