package de.justjanne.voctotv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onLayoutRectChanged
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import de.justjanne.voctotv.ui.theme.Primary

private val thumb = 16.dp
private val focusedHeight = 6.dp
private val unfocusedHeight = 3.dp

@OptIn(UnstableApi::class)
@Composable
fun Seekbar(
    player: Player,
    seekPositionMs: MutableState<Long?>,
) {
    val progressState = rememberProgressStateWithTickInterval(player)

    val scaleFactor = remember { mutableStateOf(0f) }
    val dragState = rememberDraggableState {
        val start = seekPositionMs.value ?: progressState.currentPositionMs
        if (progressState.durationMs > 0) {
            val position = (start + (it * scaleFactor.value).toLong()).coerceIn(0, progressState.durationMs)
            seekPositionMs.value = position
        }
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .padding(start = 32.dp, end = 32.dp, bottom = 20.dp)
            .minimumInteractiveComponentSize()
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {},
                    onTap = {
                        if (progressState.durationMs > 0) {
                            val position = (it.x * scaleFactor.value).toLong().coerceIn(0, progressState.durationMs)
                            player.seekTo(position)
                        }
                        seekPositionMs.value = null
                    },
                )
            }
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                startDragImmediately = seekPositionMs.value != null,
                onDragStarted = {
                    if (progressState.durationMs > 0) {
                        val position = (it.x * scaleFactor.value).toLong().coerceIn(0, progressState.durationMs)
                        seekPositionMs.value = position
                    }
                },
                onDragStopped = {
                    seekPositionMs.value?.let {
                        player.seekTo(it)
                    }
                    seekPositionMs.value = null
                }
            )
            .onLayoutRectChanged {
                scaleFactor.value = progressState.durationMs.toFloat() / it.width.toFloat()
            }
            .drawBehind {
                val thumb = thumb.toPx()
                val focusedHeight = focusedHeight.toPx()
                val unfocusedHeight = unfocusedHeight.toPx()

                val buffered = progressState.bufferedPositionMs.toFloat() / progressState.durationMs.toFloat()
                val progress = progressState.currentPositionMs.toFloat() / progressState.durationMs.toFloat()

                val currentTimestamp = seekPositionMs.value ?: progressState.currentPositionMs
                val seekProgress = currentTimestamp.toFloat() / progressState.durationMs.toFloat()

                val currentHeight = if (seekPositionMs.value != null) focusedHeight else unfocusedHeight

                val currentWidth = size.width - thumb

                val barOffset = Offset((size.width - currentWidth) / 2, (size.height - currentHeight) / 2)
                val thumbOffset = Offset(seekProgress * currentWidth + thumb / 2, size.height / 2)

                drawRect(Color.DarkGray, size = Size(currentWidth, currentHeight), topLeft = barOffset)
                drawRect(
                    Color.LightGray,
                    size = Size(buffered * currentWidth, currentHeight),
                    topLeft = barOffset
                )
                drawRect(Primary, size = Size(progress * currentWidth, currentHeight), topLeft = barOffset)
                drawCircle(Color.White, radius = thumb / 2, center = thumbOffset)
            }
    ) {
    }
}
