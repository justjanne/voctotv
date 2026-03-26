/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoDescriptionLive(
    video: VideoModel.Live,
    onClose: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .heightIn(40.dp)
                    .minimumInteractiveComponentSize()
                    .weight(1f, true),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                video.room.display,
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        lineBreak = LineBreak.Heading,
                    ),
            )
        }
        if (onClose != null) {
            IconButton(onClick = onClose) {
                Icon(
                    painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.action_close),
                )
            }
        }
    }

    if (video.conference.conference.isNotBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            video.conference.conference,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (video.conference.description.isNotBlank()) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            video.conference.description,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
        )
    }
}
