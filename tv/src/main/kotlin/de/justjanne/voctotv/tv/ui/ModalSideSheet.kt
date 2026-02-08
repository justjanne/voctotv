package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults

object ModalSideSheetDefaults {
    val Width = 360.dp
    val Padding = PaddingValues(vertical = 24.dp, horizontal = 16.dp)
    val Shape = RoundedCornerShape(16.dp)
    val ContainerColor: Color
        @Composable get() =
            MaterialTheme.colorScheme.surfaceVariant
                .copy(alpha = 0.25f)
                .compositeOver(MaterialTheme.colorScheme.surface)
    val ContentColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
}

@Composable
fun ModalSideSheet(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier =
            modifier
                .width(ModalSideSheetDefaults.Width)
                .fillMaxHeight()
                .padding(24.dp),
        colors =
            SurfaceDefaults.colors(
                containerColor = ModalSideSheetDefaults.ContainerColor,
                contentColor = ModalSideSheetDefaults.ContentColor,
            ),
        shape = ModalSideSheetDefaults.Shape,
    ) {
        content()
    }
}
