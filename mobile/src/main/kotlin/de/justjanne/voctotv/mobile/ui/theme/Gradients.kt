package de.justjanne.voctotv.mobile.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow


val ColorScheme.scrimBrush: Brush
    @Composable get() =
        Brush.verticalGradient(
            listOf(
                MaterialTheme.colorScheme.background.copy(alpha = 0f),
                MaterialTheme.colorScheme.background,
            ),
        )

val SoftScrim: Color
    get() = Color(red = 28, green = 27, blue = 31, alpha = 204)

val PlayerScrimTop: Brush =
    Brush.verticalGradient(
        listOf(SoftScrim, SoftScrim.copy(alpha = 0f)),
    )

val PlayerScrimBottom: Brush =
    Brush.verticalGradient(
        listOf(SoftScrim.copy(alpha = 0f), SoftScrim),
    )

val ColorScheme.textShadow: Shadow
    @Composable get() =
        Shadow(
            color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f),
            offset = Offset(x = 2f, y = 4f),
            blurRadius = 2f,
        )
