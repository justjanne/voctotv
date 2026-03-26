/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.previews.rememberPreviewThumbnail
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.ui.theme.SoftScrim
import de.justjanne.voctotv.mobile.ui.theme.textShadow

object PreviewbarDefaults {
    val ThumbSize = 16.dp

    val PreviewShape = RoundedCornerShape(8.dp)
    val PreviewBorder = BorderStroke(3.dp, SoftScrim)
    val PreviewHeight = 144.dp
}

@OptIn(UnstableApi::class)
@Composable
fun Previewbar(
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

    BoxWithConstraints(
        modifier =
            modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth(),
    ) {
        Column(
            modifier =
                Modifier
                    .graphicsLayer {
                        val thumb = PreviewbarDefaults.ThumbSize.toPx()
                        val currentTimestamp = viewModel.playerState.seekingMs
                        val progress = currentTimestamp.toFloat() / viewModel.playerState.durationMs.toFloat()
                        val padding = 32.dp.toPx()
                        val currentWidth = constraints.maxWidth - thumb - padding

                        val thumbOffset = progress * currentWidth + thumb / 2
                        val translation = thumbOffset - size.width / 2 - 4.dp.toPx()
                        translationX = translation.coerceIn(0f, currentWidth - size.width + thumb + padding)
                        alpha = if (viewModel.playerState.seeking) 1f else 0f
                    },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = PreviewbarDefaults.PreviewShape,
                border = PreviewbarDefaults.PreviewBorder,
            ) {
                Box(
                    Modifier
                        .height(PreviewbarDefaults.PreviewHeight)
                        .aspectRatio(16f / 9f),
                ) {
                    AsyncImage(
                        model = currentThumbnail.value,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            Text(
                text = formatTime(viewModel.playerState.seekingMs),
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        shadow = MaterialTheme.colorScheme.textShadow,
                    ),
            )
        }
    }
}
