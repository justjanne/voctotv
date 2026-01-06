package de.justjanne.voctotv.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import coil3.compose.AsyncImage
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.util.formatTime

@Composable
fun LectureCard(
    lecture: LectureModel,
    openLecture: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompactCard(
        onClick = {
            openLecture(lecture.guid)
        },
        modifier = modifier
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
        badge = {
            Text(
                text = formatTime(lecture.duration * 1000),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .background(Color.Black, shape = MaterialTheme.shapes.extraSmall)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = Color.White,
                ),
            )
        }
    )
}

@Composable
private fun CompactCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    badge: @Composable BoxScope.() -> Unit = {},
    shape: androidx.compose.ui.graphics.Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        elevation = elevation,
        colors = colors,
        border = border,
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            Box(
                modifier =
                    Modifier.drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = ScrimBrush)
                        }
                    },
                contentAlignment = Alignment.Center,
                content = image
            )
            Column { CardContent(title = title, subtitle = subtitle, description = description) }
            badge()
        }
    }
}

private val ScrimBrush =
    Brush.verticalGradient(
        listOf(
            Color(red = 28, green = 27, blue = 31, alpha = 0),
            Color(red = 28, green = 27, blue = 31, alpha = 204)
        )
    )

@Composable
private fun CardContent(
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {}
) {
    ProvideTextStyle(MaterialTheme.typography.titleMedium) { title.invoke() }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = SubtitleAlpha }) { subtitle.invoke() }
    }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = DescriptionAlpha }) { description.invoke() }
    }
}

private const val SubtitleAlpha = 0.6f
private const val DescriptionAlpha = 0.8f
