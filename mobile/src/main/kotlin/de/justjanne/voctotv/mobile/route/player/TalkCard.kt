package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.voctoweb.model.LiveTalkModel

@Composable
fun TalkCard(talk: LiveTalkModel) {
    when (talk) {
        is LiveTalkModel.Break -> TalkCardBreak(talk)
        is LiveTalkModel.Talk -> TalkCardTalk(talk)
    }
}

@Composable
fun TalkCardBreak(talk: LiveTalkModel.Break) {
    Card(Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp)) {
        Column(Modifier.padding(16.dp, 8.dp)) {
            Text(
                text = talk.title ?: stringResource(R.string.schedule_break),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${talk.startText} – ${talk.endText}",
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = 0.6f),
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
fun TalkCardTalk(talk: LiveTalkModel.Talk) {
    val uriHandler = LocalUriHandler.current

    Card(
        onClick = {
            uriHandler.openUri(talk.url)
        },
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 16.dp),
    ) {
        Column(Modifier.padding(16.dp, 8.dp)) {
            Text(
                text = talk.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = talk.speaker,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
                    ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${talk.startText} – ${talk.endText}",
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                    ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
