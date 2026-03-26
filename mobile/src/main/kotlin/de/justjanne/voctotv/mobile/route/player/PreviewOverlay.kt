/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.previews.rememberPreviewThumbnail
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PreviewOverlay(
    viewModel: PlayerViewModel,
    modifier: Modifier = Modifier,
) {
    val allCues = viewModel.previews.collectAsState()

    val currentCue =
        remember {
            derivedStateOf {
                val seekingUs = viewModel.playerState.seekingMs * 1000L
                allCues.value.firstOrNull { it.startUs <= seekingUs && it.endUs >= seekingUs }?.data
            }
        }

    val currentThumbnail = rememberPreviewThumbnail(currentCue)

    AsyncImage(
        model = currentThumbnail.value,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
    )
}
