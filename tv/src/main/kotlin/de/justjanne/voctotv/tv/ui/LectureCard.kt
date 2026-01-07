package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardGlow
import androidx.tv.material3.CardScale
import androidx.tv.material3.CardShape
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.tv.Routes

private const val SubtitleAlpha = 0.6f
private const val DescriptionAlpha = 0.8f

@Composable
fun LectureCard(
    lecture: LectureModel,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    CompactCard(
        onClick = {
            navigate(Routes.Player(lecture.guid))
        },
        modifier =
            modifier
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
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(Color.Black, shape = MaterialTheme.shapes.extraSmall)
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                style =
                    MaterialTheme.typography.labelLarge.copy(
                        color = Color.White,
                    ),
            )
        },
    )
}

@Composable
private fun CompactCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    badge: @Composable BoxScope.() -> Unit = {},
    shape: CardShape = CardDefaults.shape(),
    colors: CardColors = CardDefaults.compactCardColors(),
    scale: CardScale = CardDefaults.scale(),
    border: CardBorder = CardDefaults.border(),
    glow: CardGlow = CardDefaults.glow(),
    scrimBrush: Brush = CardDefaults.ScrimBrush,
    interactionSource: MutableInteractionSource? = null,
) {
    Card(
        onClick = onClick,
        onLongClick = onLongClick,
        modifier = modifier,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        scale = scale,
        border = border,
        glow = glow,
    ) {
        Box(contentAlignment = Alignment.BottomStart) {
            Box(
                modifier =
                    Modifier.drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = scrimBrush)
                        }
                    },
                contentAlignment = Alignment.Center,
                content = image,
            )
            Column { CardContent(title = title, subtitle = subtitle, description = description) }
            badge()
        }
    }
}

@Composable
private fun CardContent(
    title: @Composable () -> Unit,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
) {
    ProvideTextStyle(MaterialTheme.typography.titleMedium) { title.invoke() }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = SubtitleAlpha }) { subtitle.invoke() }
    }
    ProvideTextStyle(MaterialTheme.typography.bodySmall) {
        Box(Modifier.graphicsLayer { alpha = DescriptionAlpha }) { description.invoke() }
    }
}
