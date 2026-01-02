package de.justjanne.voctotv.ui.player

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval

val thumb = 16.dp
val focusedHeight = 6.dp
val unfocusedHeight = 3.dp

@Composable
fun Seekbar(
    player: Player
) {
    val progressState = rememberProgressStateWithTickInterval(player)
    val seekbarInteractionSource = remember { MutableInteractionSource() }
    val isFocused = seekbarInteractionSource.collectIsFocusedAsState()
    Box(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .height(20.dp)
            .fillMaxWidth()
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionRight -> {
                            player.seekForward()
                            true
                        }

                        Key.DirectionLeft -> {
                            player.seekBack()
                            true
                        }

                        else -> false
                    }
                } else false
            }
            .clickable(interactionSource = seekbarInteractionSource, indication = null) {
                if (player.isPlaying) player.pause()
                else player.play()
            }
            .drawBehind {
                val thumb = thumb.toPx()
                val focusedHeight = focusedHeight.toPx()
                val unfocusedHeight = unfocusedHeight.toPx()

                val buffered = progressState.bufferedPositionMs.toFloat() / progressState.durationMs.toFloat()
                val progress = progressState.currentPositionMs.toFloat() / progressState.durationMs.toFloat()

                val currentHeight = if (isFocused.value) focusedHeight else unfocusedHeight

                val currentWidth = size.width - thumb

                val barOffset = Offset((size.width - currentWidth) / 2, (size.height - currentHeight) / 2)
                val thumbOffset = Offset(progress * currentWidth + thumb / 2, size.height / 2)

                drawRect(Color.DarkGray, size = Size(currentWidth, currentHeight), topLeft = barOffset)
                drawRect(
                    Color.LightGray,
                    size = Size(buffered * currentWidth, currentHeight),
                    topLeft = barOffset
                )
                drawRect(Color.Red, size = Size(progress * currentWidth, currentHeight), topLeft = barOffset)
                if (isFocused.value) {
                    drawCircle(Color.White, radius = thumb / 2, center = thumbOffset)
                }
            }
    ) {
    }
}