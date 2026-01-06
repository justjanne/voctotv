package de.justjanne.voctotv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import de.justjanne.voctotv.mediacccde.model.ConferenceModel

@Composable
fun ConferenceItem(
    item: ConferenceModel,
    showYear: Boolean = true,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp, 4.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(120.dp)
                .aspectRatio(16f / 9f)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(Color.White)
        ) {
            AsyncImage(
                model = item.logoUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(8.dp),
            )
        }
        Column(Modifier.align(Alignment.CenterVertically), verticalArrangement = Arrangement.Center) {
            val year = item.eventLastReleasedAt?.year?.toString()
            if (year == null || !showYear) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            } else {
                Text(
                    text = item.title.removeSuffix(" $year"),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = year,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = LocalContentColor.current.copy(alpha = 0.6f)
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}