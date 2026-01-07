package de.justjanne.voctotv.tv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import de.justjanne.voctotv.tv.ui.theme.Primary

private val thumb = 16.dp
private val focusedHeight = 6.dp
private val unfocusedHeight = 3.dp

@OptIn(UnstableApi::class)
@Composable
fun Seekbar(
    player: Player,
    interactionSource: MutableInteractionSource,
    seekBack: () -> Unit,
    seekForward: () -> Unit,
) {
    val progressState = rememberProgressStateWithTickInterval(player)
    val isFocused = interactionSource.collectIsFocusedAsState()

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
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.DirectionRight -> {
                            seekForward()
                            true
                        }

                        Key.DirectionLeft -> {
                            seekBack()
                            true
                        }

                        else -> false
                    }
                } else false
            }
            .clickable(interactionSource = interactionSource, indication = null) {
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
                drawRect(Primary, size = Size(progress * currentWidth, currentHeight), topLeft = barOffset)
                if (isFocused.value) {
                    drawCircle(Color.White, radius = thumb / 2, center = thumbOffset)
                }
            }
    ) {
    }
}
