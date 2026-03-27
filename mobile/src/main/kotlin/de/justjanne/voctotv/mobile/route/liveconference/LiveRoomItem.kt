/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.liveconference

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.LiveRoomModel
import de.justjanne.voctotv.voctoweb.model.LiveTalkModel

@Composable
fun LiveRoomItem(
    item: LiveRoomModel,
    onClick: () -> Unit = {},
) {
    val currentItem = item.talks.current

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .align(Alignment.CenterVertically)
                    .width(160.dp)
                    .aspectRatio(16f / 9f)
                    .clip(MaterialTheme.shapes.extraSmall)
                    .background(MaterialTheme.colorScheme.surface),
        ) {
            AsyncImage(
                model = item.poster,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Column(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = item.display,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (currentItem != null) {
                when (currentItem) {
                    is LiveTalkModel.Talk -> {
                        Text(
                            text = currentItem.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = currentItem.speaker,
                            style =
                                MaterialTheme.typography.bodyMedium.copy(
                                    color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
                                ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    is LiveTalkModel.Break -> {
                        Text(
                            text = currentItem.title ?: "Break",
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }
        }
    }
}
