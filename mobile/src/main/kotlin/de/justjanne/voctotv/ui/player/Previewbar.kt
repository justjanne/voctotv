package de.justjanne.voctotv.ui.player

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.SizeResolver
import coil3.transform.Transformation
import de.justjanne.voctotv.viewmodel.PlayerViewModel

private val thumb = 16.dp

@OptIn(UnstableApi::class)
@Composable
fun Previewbar(
    viewModel: PlayerViewModel,
    player: Player,
    seekPositionMs: MutableState<Long?>,
    modifier: Modifier = Modifier,
) {
    val playbackState = rememberPlayPauseButtonState(player)
    val progressState = rememberProgressStateWithTickInterval(player)
    val allCues = viewModel.previews.collectAsState()

    val currentCue = remember {
        derivedStateOf {
            seekPositionMs.value?.let { timestamp ->
                val currentTimestamp = timestamp * 1000
                allCues.value.firstOrNull { it.startUs <= currentTimestamp && it.endUs >= currentTimestamp }?.data
            }
        }
    }

    val previousThumbnail = remember { mutableStateOf<Image?>(null) }

    val context = LocalContext.current
    val currentThumbnail = remember(context) {
        derivedStateOf {
            currentCue.value?.let { url ->
                try {
                    url.queryParameter("xywh")?.let { fragment ->
                        val (x, y, w, h) = fragment.split(',')
                        ImageRequest.Builder(context)
                            .data(url.toString())
                            .placeholder(previousThumbnail.value)
                            .size(SizeResolver.ORIGINAL)
                            .transformations(SpritesheetTransformation(w.toInt(), h.toInt(), x.toInt(), y.toInt()))
                            .listener(onSuccess = { _, result ->
                                previousThumbnail.value = result.image
                            })
                            .build()
                    } ?: run {
                        ImageRequest.Builder(context)
                            .data(url.toString())
                            .placeholder(previousThumbnail.value)
                            .size(SizeResolver.ORIGINAL)
                            .listener(onSuccess = { _, result ->
                                previousThumbnail.value = result.image
                            })
                            .build()
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .height(96.dp)
            .fillMaxWidth()
    ) {
        if (seekPositionMs.value != null && playbackState.showPlay) {
            Surface(
                modifier = Modifier
                    .graphicsLayer {
                        val thumb = thumb.toPx()
                        val currentTimestamp = seekPositionMs.value ?: progressState.currentPositionMs
                        val progress = currentTimestamp.toFloat() / progressState.durationMs.toFloat()
                        val currentWidth = constraints.maxWidth - thumb
                        val translation = (progress * currentWidth + thumb / 2) - size.width / 2
                        translationX = translation.coerceIn(0f, currentWidth - size.width)
                        translationY = -size.height + 24.dp.toPx()
                        alpha = if (seekPositionMs.value != null) 1f else 0f
                    },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(width = 3.dp, color = Color(red = 28, green = 27, blue = 31, alpha = 204))
            ) {
                Box(Modifier.height(90.dp).aspectRatio(16f / 9f)) {
                    AsyncImage(
                        model = currentThumbnail.value,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

class SpritesheetTransformation(
    private val w: Int,
    private val h: Int,
    private val x: Int,
    private val y: Int,
) : Transformation() {
    override val cacheKey: String = "spritesheet-$w-$h-$x-$y"

    override suspend fun transform(input: Bitmap, size: coil3.size.Size): Bitmap {
        val cropped = Bitmap.createBitmap(input, x, y, w, h)
        input.recycle()
        return cropped
    }
}
