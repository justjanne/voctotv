/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui.player

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun BoxScope.TitleOverlay(
    visible: Boolean,
    lecture: LectureModel?,
) {
    AnimatedVisibility(
        visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
        modifier = Modifier.align(Alignment.TopStart),
    ) {
        lecture?.let {
            Column(
                modifier =
                    Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(red = 28, green = 27, blue = 31, alpha = 204),
                                    Color(red = 28, green = 27, blue = 31, alpha = 0),
                                ),
                            ),
                        ).padding(32.dp)
                        .fillMaxWidth(),
            ) {
                Text(
                    text = lecture.conferenceTitle,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.65f),
                            shadow =
                                Shadow(
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                    offset = Offset(x = 2f, y = 4f),
                                    blurRadius = 2f,
                                ),
                        ),
                    maxLines = 1,
                )
                Text(
                    text = lecture.title,
                    style =
                        MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            shadow =
                                Shadow(
                                    color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
                                    offset = Offset(x = 2f, y = 4f),
                                    blurRadius = 2f,
                                ),
                        ),
                    maxLines = 2,
                )
            }
        }
    }
}
