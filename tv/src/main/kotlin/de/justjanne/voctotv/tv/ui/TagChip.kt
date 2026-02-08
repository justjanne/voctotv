package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults

object TagChipDefaults {
    val Shape = RoundedCornerShape(8.0.dp)
    val ContainerColor: Color
        @Composable get() = MaterialTheme.colorScheme.surfaceVariant
    val ContentColor: Color
        @Composable get() = MaterialTheme.colorScheme.onSurface
    val ContainerHeight = 32.dp
    val Padding = PaddingValues(horizontal = 8.dp)
    val TextStyle: TextStyle
        @Composable get() = MaterialTheme.typography.labelLarge
}

@Composable
fun TagChip(content: @Composable () -> Unit) {
    Surface(
        shape = TagChipDefaults.Shape,
        colors =
            SurfaceDefaults.colors(
                containerColor = TagChipDefaults.ContainerColor,
                contentColor = TagChipDefaults.ContentColor,
            ),
    ) {
        Box(
            modifier =
                Modifier
                    .heightIn(TagChipDefaults.ContainerHeight)
                    .padding(TagChipDefaults.Padding),
            contentAlignment = Alignment.Center,
        ) {
            ProvideTextStyle(TagChipDefaults.TextStyle, content)
        }
    }
}
