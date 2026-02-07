package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Surface

object ModalSideSheetDefaults {
    val Width = 360.dp
    val Elevation = 2.dp
    val Padding = PaddingValues(vertical = 24.dp, horizontal = 16.dp)
    val Shape = RoundedCornerShape(16.dp)
}

@Composable
fun ModalSideSheet(
    modifier: Modifier = Modifier.Companion,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.width(ModalSideSheetDefaults.Width)
            .fillMaxHeight()
            .padding(24.dp),
        tonalElevation = ModalSideSheetDefaults.Elevation,
        shape = ModalSideSheetDefaults.Shape,
    ) {
        content()
    }
}
