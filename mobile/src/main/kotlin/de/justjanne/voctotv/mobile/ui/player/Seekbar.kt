package de.justjanne.voctotv.mobile.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.ui.theme.Primary

private val thumb = 16.dp
private val focusedHeight = 6.dp
private val unfocusedHeight = 3.dp

@OptIn(UnstableApi::class)
@Composable
fun Seekbar(
    player: Player,
    playerState: PlayerState,
) {
    val scaleFactor = remember { mutableStateOf(0f) }
    val dragState = rememberDraggableState {
        val start = if (playerState.seeking) playerState.seekingMs else playerState.progressMs
        playerState.seek((start + it * scaleFactor.value).toLong())
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
                        playerState.seek((it.x * scaleFactor.value).toLong())
                        playerState.commitSeek(player)
                    },
                )
            }
            .draggable(
                state = dragState,
                orientation = Orientation.Horizontal,
                startDragImmediately = playerState.seeking,
                onDragStarted = {
                    playerState.seek((it.x * scaleFactor.value).toLong())
                },
                onDragStopped = {
                    playerState.commitSeek(player)
                }
            )
            .onLayoutRectChanged {
                scaleFactor.value = playerState.durationMs.toFloat() / it.width.toFloat()
            }
            .drawBehind {
                val thumb = thumb.toPx()
                val focusedHeight = focusedHeight.toPx()
                val unfocusedHeight = unfocusedHeight.toPx()

                val buffered = playerState.bufferedMs.toFloat() / playerState.durationMs.toFloat()
                val progress = playerState.progressMs.toFloat() / playerState.durationMs.toFloat()

                val currentTimestamp = if (playerState.seeking) playerState.seekingMs else playerState.progressMs
                val seekProgress = currentTimestamp.toFloat() / playerState.durationMs.toFloat()

                val currentHeight = if (playerState.seeking) focusedHeight else unfocusedHeight

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
