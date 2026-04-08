package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.CardBorder
import androidx.tv.material3.CardColors
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.CardGlow
import androidx.tv.material3.CardScale
import androidx.tv.material3.CardShape
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.ProvideTextStyle
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.theme.DescriptionAlpha
import de.justjanne.voctotv.tv.ui.theme.SubtitleAlpha

@Composable
internal fun CompactCard(
    onClick: () -> Unit,
    image: @Composable BoxScope.() -> Unit,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier.Companion,
    onLongClick: (() -> Unit)? = null,
    subtitle: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    badge: @Composable () -> Unit = {},
    showWatchLaterStar: Boolean = false,
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
        Box(contentAlignment = Alignment.Companion.BottomStart) {
            Box(
                modifier =
                    Modifier.Companion.drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = scrimBrush)
                        }
                    },
                contentAlignment = Alignment.Companion.Center,
                content = image,
            )
            if (showWatchLaterStar) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = stringResource(R.string.action_watch_later),
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(20.dp),
                )
            }
            Column {
                ProvideTextStyle(
                    MaterialTheme.typography.titleMedium,
                    content = title,
                )
                Row(
                    verticalAlignment = Alignment.Companion.CenterVertically,
                    modifier = Modifier.Companion.fillMaxWidth().padding(bottom = 6.dp),
                ) {
                    Column(
                        Modifier.Companion.weight(1f, true),
                    ) {
                        ProvideTextStyle(
                            MaterialTheme.typography.bodySmall.copy(
                                color = LocalContentColor.current.copy(alpha = SubtitleAlpha),
                            ),
                            content = subtitle,
                        )
                        ProvideTextStyle(
                            MaterialTheme.typography.bodySmall.copy(
                                color = LocalContentColor.current.copy(alpha = DescriptionAlpha),
                            ),
                            content = description,
                        )
                    }
                    badge()
                }
            }
        }
    }
}
