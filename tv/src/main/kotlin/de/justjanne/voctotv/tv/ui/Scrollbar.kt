package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor

@Composable
fun Modifier.scrollbar(scrollState: ScrollState, padding: Dp): Modifier {
    val contentColor = LocalContentColor.current

    return this then Modifier.drawBehind {
        if (scrollState.maxValue > 0) {
            val padding = padding.toPx()

            val trackWidth = 2.dp.toPx()
            val trackHeight = size.height - padding - padding

            val throbberWidth = 8.dp.toPx()

            val offsetX = size.width - padding
            val offsetY = padding

            val contentHeight = scrollState.viewportSize.toFloat() + scrollState.maxValue.toFloat()
            val containerHeight = scrollState.viewportSize.toFloat()

            val maxPosition = scrollState.maxValue.toFloat()
            val position = scrollState.value.toFloat()

            val throbberHeight = containerHeight / contentHeight * trackHeight
            val maxThrobberPosition = trackHeight - throbberHeight
            val throbberPosition = position / maxPosition * maxThrobberPosition

            drawRoundRect(
                contentColor,
                topLeft = Offset(offsetX - trackWidth / 2, offsetY),
                size = Size(trackWidth, trackHeight),
                cornerRadius = CornerRadius(trackWidth / 2, trackWidth / 2),
            )

            drawRoundRect(
                contentColor,
                topLeft = Offset(offsetX - throbberWidth / 2, offsetY + throbberPosition),
                size = Size(throbberWidth, throbberHeight),
                cornerRadius = CornerRadius(throbberWidth / 2, throbberWidth / 2),
            )
        }
    }
}
