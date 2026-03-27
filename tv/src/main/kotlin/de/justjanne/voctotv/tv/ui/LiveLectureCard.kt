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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardGlow
import androidx.tv.material3.CardScale
import androidx.tv.material3.CardShape
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.voctoweb.model.LiveRoomModel

@Composable
fun LiveRoomCardCard(
    room: LiveRoomModel,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompactCard2(
        onClick = {
            navigate(Routes.PlayerLive(room.guid))
        },
        modifier =
            modifier
                .width(268.dp)
                .aspectRatio(16f / 9),
        title = {
            Text(
                text = room.display,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        image = {
            AsyncImage(
                model = room.poster,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        },
        badge = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier =
                    Modifier
                        .padding(end = 6.dp)
                        .background(MaterialTheme.colorScheme.scrim, shape = MaterialTheme.shapes.extraSmall)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Icon(
                    painterResource(R.drawable.ic_live),
                    contentDescription = null,
                    tint = Color.Red,
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "LIVE",
                    style =
                        MaterialTheme.typography.labelLarge.copy(
                            color = Color.White,
                        ),
                )
            }
        },
    )
}

@Composable
private fun CompactCard2(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier.Companion,
    onLongClick: (() -> Unit)? = null,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
            ) {
                Column(
                    Modifier.weight(1f, true),
                ) {
                    ProvideTextStyle(
                        MaterialTheme.typography.titleMedium,
                        content = title,
                    )
                }
                badge()
            }
        }
    }
}
