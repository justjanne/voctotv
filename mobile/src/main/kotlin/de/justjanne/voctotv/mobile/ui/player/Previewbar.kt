package de.justjanne.voctotv.mobile.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import coil3.compose.AsyncImage
import de.justjanne.voctotv.common.previews.rememberPreviewThumbnail
import de.justjanne.voctotv.common.util.formatTime
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel

private val ThumbSize = 16.dp

@OptIn(UnstableApi::class)
@Composable
fun Previewbar(
    viewModel: PlayerViewModel,
    playerState: PlayerState,
    modifier: Modifier = Modifier,
) {
    val allCues = viewModel.previews.collectAsState()

    val currentCue = remember {
        derivedStateOf {
            val seekingUs = playerState.seekingMs * 1000L
            allCues.value.firstOrNull { it.startUs <= seekingUs && it.endUs >= seekingUs }?.data
        }
    }

    val currentThumbnail = rememberPreviewThumbnail(currentCue)

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .graphicsLayer {
                    val thumb = ThumbSize.toPx()
                    val currentTimestamp = playerState.seekingMs
                    val progress = currentTimestamp.toFloat() / playerState.durationMs.toFloat()
                    val currentWidth = constraints.maxWidth - thumb
                    val translation = (progress * currentWidth + thumb / 2) - size.width / 2
                    translationX = translation.coerceIn(0f, currentWidth - size.width)
                    alpha = if (playerState.seeking) 1f else 0f
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(width = 3.dp, color = Color(red = 28, green = 27, blue = 31, alpha = 204)),
            ) {
                Box(Modifier.height(144.dp).aspectRatio(16f / 9f)) {
                    AsyncImage(
                        model = currentThumbnail.value,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Text(
                text = formatTime(playerState.seekingMs),
                style = MaterialTheme.typography.labelLarge.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
            )
        }
    }
}
