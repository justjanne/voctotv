package de.justjanne.voctotv.tv.route.player

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoDescriptionLive(video: VideoModel.Live) {
    Text(
        video.room.display,
        style =
            MaterialTheme.typography.titleLarge.copy(
                lineBreak = LineBreak.Heading,
            ),
    )

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

    Spacer(modifier = Modifier.height(24.dp))

    video.room.talks.current?.let {
        Text(
            stringResource(R.string.schedule_current),
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
        )
        TalkCard(it)
    }

    video.room.talks.next?.let {
        Text(
            stringResource(R.string.schedule_next),
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
        )
        TalkCard(it)
    }
}
