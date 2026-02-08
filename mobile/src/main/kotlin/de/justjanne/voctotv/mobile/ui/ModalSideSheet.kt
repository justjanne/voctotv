package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp

object ModalSideSheetDefaults {
    val Width = 312.dp
    val Padding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 24.dp)
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
                .fillMaxHeight(),
        color = ModalSideSheetDefaults.ContainerColor,
        contentColor = ModalSideSheetDefaults.ContentColor,
        shape = ModalSideSheetDefaults.Shape,
    ) {
        content()
    }
}
