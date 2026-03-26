/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastJoinToString
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.TagChip
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoDescriptionVod(
    video: VideoModel.Vod,
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
                video.lecture.title,
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

    if (!video.lecture.subtitle.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            video.lecture.subtitle ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                String.format(Locale.current.platformLocale, "%,d", video.lecture.viewCount),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            Text(
                stringResource(R.string.video_info_views),
                fontSize = 12.sp,
                color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                DateUtils.formatDateTime(
                    LocalContext.current,
                    video.lecture.releaseDate.toInstant().toEpochMilli(),
                    DateUtils.FORMAT_SHOW_DATE or
                        DateUtils.FORMAT_NO_YEAR or
                        DateUtils.FORMAT_ABBREV_MONTH,
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
            )
            Text(
                video.lecture.releaseDate.year.toString(),
                fontSize = 12.sp,
                color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
            )
        }
    }

    if (video.lecture.persons.fastAny { it.isNotBlank() }) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            video.lecture.persons.fastJoinToString(" · "),
            style = MaterialTheme.typography.bodyMedium,
        )
    }

    if (!video.lecture.description.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            video.lecture.description ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
        )
    }

    Spacer(Modifier.height(16.dp))

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Start),
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Top),
    ) {
        for (tag in video.lecture.tags) {
            TagChip { Text(tag) }
        }
    }
}
