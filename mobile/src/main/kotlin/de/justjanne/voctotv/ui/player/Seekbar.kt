package de.justjanne.voctotv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.layout
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
        seekPositionMs.value = start + (it * scaleFactor.value).toLong()
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .focusRequester(focusRequester)
            .padding(horizontal = 32.dp)
            .height(20.dp)
            .fillMaxWidth()
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                onDragStarted = {
                    seekPositionMs.value = progressState.currentPositionMs
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
