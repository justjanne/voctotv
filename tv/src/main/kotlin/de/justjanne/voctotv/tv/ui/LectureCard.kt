/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardGlow
import androidx.tv.material3.CardScale
import androidx.tv.material3.CardShape
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun LectureCard(
    lecture: LectureModel,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompactCard(
        onClick = {
            navigate(Routes.Player(lecture.guid))
        },
        modifier =
            modifier
                .width(268.dp)
                .aspectRatio(16f / 9),
        title = {
            Text(
                text = lecture.title,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        subtitle = {
            Text(
                text = lecture.persons.fastJoinToString(" · "),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        image = {
            AsyncImage(
                model = lecture.thumbUrl,
                contentDescription = lecture.title,
                modifier = Modifier.fillMaxSize(),
            )
        },
        badge = {
            Text(
                text = formatTime(lecture.duration * 1000),
                modifier =
                    Modifier
                        .padding(end = 6.dp)
                        .background(MaterialTheme.colorScheme.scrim, shape = MaterialTheme.shapes.extraSmall)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                    ),
            )
        },
    )
}

@Composable
private fun CompactCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    badge: @Composable () -> Unit = {},
    shape: CardShape = CardDefaults.shape(),
    colors: CardColors = CardDefaults.compactCardColors(),
    scale: CardScale = CardDefaults.scale(),
    border: CardBorder = CardDefaults.border(),
    glow: CardGlow = CardDefaults.glow(),
    scrimBrush: Brush = CardDefaults.ScrimBrush,
    interactionSource: MutableInteractionSource? = null,
) {
    Card(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        scale = scale,
        border = border,
        glow = glow,
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            Box(
                modifier =
                    Modifier.drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = scrimBrush)
                        }
                    },
                contentAlignment = Alignment.Center,
                content = image,
            )
            Column {
                ProvideTextStyle(
                    MaterialTheme.typography.titleMedium,
                    content = title,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                ) {
                    Column(
                        Modifier.weight(1f, true),
                    ) {
                        ProvideTextStyle(
                            MaterialTheme.typography.bodySmall.copy(
                                color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
                            ),
                            content = subtitle,
                        )
                        ProvideTextStyle(
                            MaterialTheme.typography.bodySmall.copy(
                                color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                            ),
                            content = description,
                        )
                    }
                    badge()
                }
            }
        }
    }
}
