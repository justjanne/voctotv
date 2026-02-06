/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.scrimBrush

@Composable
fun HeroCarouselContent(
    background: Painter,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    description: @Composable () -> Unit = {},
    action: @Composable () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(HeroCarouselDefaults.VerticalPadding),
    ) {
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
                modifier = modifier.align(Alignment.BottomCenter)
                    .padding(horizontal = HeroCarouselDefaults.HorizontalPadding),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ProvideTextStyle(
                    MaterialTheme.typography.titleLarge.copy(
                        lineBreak = LineBreak.Heading,
                        textAlign = TextAlign.Center,
                    )
                ) {
                    title()
                }
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                        textAlign = TextAlign.Center,
                    )
                ) {
                    description()
                }
            }
        }

        action()
    }
}
