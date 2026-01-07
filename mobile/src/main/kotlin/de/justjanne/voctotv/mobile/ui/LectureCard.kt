package de.justjanne.voctotv.mobile.ui

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.mediacccde.model.LectureModel

@Composable
fun LectureItem(
    item: LectureModel,
    onClick: () -> Unit = {},
) {
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
                    .background(Color.Black),
        ) {
            AsyncImage(
                model = item.thumbUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
            Text(
                text = formatTime(item.duration * 1000),
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black, shape = MaterialTheme.shapes.extraSmall)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                    ),
            )
        }
        Column(Modifier.padding(vertical = 12.dp)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = item.persons.joinToString(" · "),
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
