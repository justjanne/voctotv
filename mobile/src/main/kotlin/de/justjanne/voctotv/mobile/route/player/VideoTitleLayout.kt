package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.mobile.ui.theme.textShadow

@Composable
fun VideoTitleLayout(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(start = 6.dp)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    onClick()
                }
                .padding(horizontal = 6.dp),
    ) {
        Text(
            text = subtitle ?: "",
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = SubtitleAlpha),
                    shadow = MaterialTheme.colorScheme.textShadow,
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    shadow = MaterialTheme.colorScheme.textShadow,
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
