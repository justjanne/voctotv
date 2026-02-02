/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.scrimBrush

@Composable
fun FeaturedCarouselItem(
    onClick: () -> Unit,
    background: Painter,
    title: String,
    persons: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.fillMaxWidth()) {
            val scrimBrush = MaterialTheme.colorScheme.scrimBrush
            Image(
                painter = background,
                contentDescription = null,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .drawWithContent {
                            drawContent()
                            drawRect(scrimBrush)
                        },
            )
            Column(
                modifier = modifier.align(Alignment.BottomCenter).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style =
                        MaterialTheme.typography.titleLarge
                            .copy(lineBreak = LineBreak.Heading),
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                )
                Text(
                    text = persons.fastJoinToString(" · "),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                    textAlign = TextAlign.Center,
                )
            }
        }

        WatchNowButton(onClick = onClick)
    }
}

@Composable
private fun WatchNowButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        modifier = Modifier.padding(top = 12.dp),
        colors =
            ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
            ),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_play_arrow),
            contentDescription = null,
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = "Watch now",
            style = MaterialTheme.typography.titleSmall,
        )
    }
}
