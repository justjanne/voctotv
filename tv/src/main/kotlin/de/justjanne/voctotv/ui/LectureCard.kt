package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.tv.material3.CompactCard
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.ccc.media.api.LectureModel

@Composable
fun LectureCard(
    lecture: LectureModel,
    openLecture: (String) -> Unit,
) {
    CompactCard(
        onClick = {
            openLecture(lecture.guid)
        },
        modifier = Modifier
            .width(268.dp)
            .aspectRatio(16f / 9),
        title = {
            Text(
                text = lecture.title,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 6.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        },
        subtitle = {
            Text(
                text = lecture.persons.fastJoinToString(" · "),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp),
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
    )
}