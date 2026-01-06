package de.justjanne.voctotv.ui.player

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastRoundToInt
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun PlayerPopup(
    isOpen: Boolean,
    onClose: () -> Unit,
    content: @Composable () -> Unit,
) {
    if (isOpen) {
        Popup(
            alignment = Alignment.TopEnd,
            offset = IntOffset(0, with(LocalDensity.current) { 64.dp.toPx() }.fastRoundToInt()),
            properties = PopupProperties(
                dismissOnBackPress = true,
                focusable = true,
            ),
            onDismissRequest = { onClose() },
        ) {
            Surface(
                modifier = Modifier.wrapContentHeight().width(244.dp),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 16.dp,
            ) {
                content()
            }
        }
    }
}
