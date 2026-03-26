package de.justjanne.voctotv.tv.route.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.tv.ui.theme.PlayerScrimTop
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.tv.ui.theme.textShadow

@Composable
fun VideoTitleLayout(
    title: String,
    subtitle: String,
) {
    Column(
        modifier =
            Modifier
                .background(PlayerScrimTop)
                .padding(32.dp)
                .fillMaxWidth(),
    ) {
        Text(
            text = subtitle,
            style =
                MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White.copy(alpha = SubtitleAlpha),
                    shadow = MaterialTheme.colorScheme.textShadow,
                ),
            maxLines = 1,
        )
        Text(
            text = title,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    color = Color.White,
                    shadow = MaterialTheme.colorScheme.textShadow,
                ),
            maxLines = 2,
        )
    }
}
