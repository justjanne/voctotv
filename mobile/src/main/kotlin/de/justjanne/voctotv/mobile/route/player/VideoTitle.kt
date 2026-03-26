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
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.mobile.ui.theme.SubtitleAlpha
import de.justjanne.voctotv.mobile.ui.theme.textShadow
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun VideoTitle(
    lecture: LectureModel?,
    onClick: () -> Unit,
) {
    lecture
        ?.let {
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
                    text = lecture.conferenceTitle,
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = SubtitleAlpha),
                            shadow = MaterialTheme.colorScheme.textShadow,
                        ),
                    maxLines = 1,
                )
                Text(
                    text = lecture.title,
                    style =
                        MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            shadow = MaterialTheme.colorScheme.textShadow,
                        ),
                    maxLines = 2,
                )
            }
        }
}
