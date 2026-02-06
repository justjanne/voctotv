/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui.carousel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle
import de.justjanne.voctotv.tv.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.tv.ui.theme.textShadow

@Composable
fun HeroCarouselContent(
    background: Painter,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit = {},
    byline: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    action: (@Composable () -> Unit)? = null,
) {
    Box(modifier) {
        Image(
            painter = background,
            contentDescription = null,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(start = HeroCarouselDefaults.BackgroundPadding)
                    .drawWithContent {
                        drawContent()
                        drawRect(
                            Brush.radialGradient(
                                center = Offset(Float.POSITIVE_INFINITY, 0f),
                                colors = HeroCarouselDefaults.BackgroundGradient,
                                radius = size.maxDimension,
                            ),
                            size = size
                        )
                    },
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .width(HeroCarouselDefaults.ContentWidth)
                        .padding(HeroCarouselDefaults.ContentPadding),
                verticalArrangement = Arrangement.Bottom,
            ) {
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = SubtitleAlpha),
                        shadow = MaterialTheme.colorScheme.textShadow,
                    )
                ) {
                    label()
                }
                Spacer(Modifier.height(HeroCarouselDefaults.TitlePadding))
                ProvideTextStyle(
                    MaterialTheme.typography.titleLarge.copy(
                        shadow = MaterialTheme.colorScheme.textShadow,
                    )
                ) {
                    title()
                }
                Spacer(Modifier.height(HeroCarouselDefaults.TitlePadding))
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = SubtitleAlpha),
                        shadow = MaterialTheme.colorScheme.textShadow,
                    )
                ) {
                    byline()
                }
                Spacer(Modifier.height(HeroCarouselDefaults.DescriptionPadding))
                ProvideTextStyle(
                    MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White.copy(alpha = DescriptionAlpha),
                        shadow = MaterialTheme.colorScheme.textShadow,
                    )
                ) {
                    description()
                }
                if (action != null) {
                    Spacer(Modifier.height(HeroCarouselDefaults.ActionPadding))
                    action()
                }
            }
        }
    }
}
