/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.tv.material3.MaterialTheme

@OptIn(UnstableApi::class)
@Composable
fun SeekbarLive() {
    val progressColor = MaterialTheme.colorScheme.primaryContainer

    Box(
        modifier =
            Modifier
                .padding(horizontal = 32.dp)
                .height(20.dp)
                .fillMaxWidth()
                .drawBehind {
                    val thumb = SeekbarDefaults.ThumbSize.toPx()
                    val unfocusedHeight = SeekbarDefaults.UnfocusedHeight.toPx()

                    val currentWidth = size.width - thumb

                    val barOffset = Offset((size.width - currentWidth) / 2, (size.height - unfocusedHeight) / 2)

                    drawRect(progressColor, size = Size(currentWidth, unfocusedHeight), topLeft = barOffset)
                },
    ) {
    }
}
