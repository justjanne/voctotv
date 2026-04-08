/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun LectureCard(
    lecture: LectureModel,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    isSaved: Boolean = false,
) {
    CompactCard(
        onClick = {
            navigate(Routes.PlayerVod(lecture.guid))
        },
        onLongClick = onLongClick,
        showWatchLaterStar = isSaved,
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
