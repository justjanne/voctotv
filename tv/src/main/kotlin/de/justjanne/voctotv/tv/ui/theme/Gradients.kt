package de.justjanne.voctotv.tv.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.tv.material3.ColorScheme
import androidx.tv.material3.MaterialTheme


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
