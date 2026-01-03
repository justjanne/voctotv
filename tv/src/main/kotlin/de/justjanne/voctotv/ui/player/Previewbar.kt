package de.justjanne.voctotv.ui.player

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.text.CueGroup
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberPlaybackSpeedState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import androidx.tv.material3.Border
import androidx.tv.material3.Surface
import coil3.Image
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.SizeResolver
import coil3.transform.Transformation
import de.justjanne.voctotv.mediacccde.model.LectureModel
import okhttp3.HttpUrl.Companion.toHttpUrl

private val thumb = 16.dp

@OptIn(UnstableApi::class)
@Composable
fun Previewbar(
    lecture: LectureModel?,
    player: Player,
    interactionSource: InteractionSource,
    visible: State<Boolean>,
    modifier: Modifier = Modifier,
) {
    val playbackState = rememberPlayPauseButtonState(player)
    val progressState = rememberProgressStateWithTickInterval(player)
    val isFocused = interactionSource.collectIsFocusedAsState()
    val currentCue = remember { mutableStateOf<String?>(null) }

    DisposableEffect(lecture, player) {
        val listener = object : Player.Listener {
            override fun onCues(cueGroup: CueGroup) {
                lecture?.let {
                    cueGroup.cues.firstNotNullOfOrNull { it.text }?.toString()?.let { cue ->
                        val baseUrl = lecture.thumbnailsUrl.toHttpUrl()
                        currentCue.value = baseUrl.resolve(cue).toString()
                    }
                }
            }
        }

        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    val previousThumbnail = remember { mutableStateOf<Image?>(null) }

    val context = LocalContext.current
    val currentThumbnail = remember(context) {
        derivedStateOf {
            currentCue.value?.let { cue ->
                try {
                    val url = cue.substringBefore("?xywh=")
                    val (x, y, w, h) = cue.substringAfter("?xywh=", "").split(',')

                    ImageRequest.Builder(context)
                        .data(url)
                        .placeholder(previousThumbnail.value)
                        .size(SizeResolver.ORIGINAL)
                        .transformations(SpritesheetTransformation(w.toInt(), h.toInt(), x.toInt(), y.toInt()))
                        .listener(onSuccess = { _, result ->
                            previousThumbnail.value = result.image
                        })
                        .build()
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
        if (visible.value && playbackState.showPlay) {
            Surface(
                modifier = Modifier
                    .graphicsLayer {
                        val thumb = thumb.toPx()
                        val progress = progressState.currentPositionMs.toFloat() / progressState.durationMs.toFloat()
                        val currentWidth = constraints.maxWidth - thumb
                        val translation = (progress * currentWidth + thumb / 2) - size.width / 2
                        translationX = translation.coerceIn(0f, currentWidth - size.width)
                        translationY = -size.height + 24.dp.toPx()
                        alpha = if (isFocused.value) 1f else 0f
                    },
                shape = RoundedCornerShape(8.dp),
                border = Border(BorderStroke(width = 3.dp, color = Color(red = 28, green = 27, blue = 31, alpha = 204)))
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
