package de.justjanne.voctotv.tv.ui.carousel

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ShapeDefaults
import de.justjanne.voctotv.tv.ui.theme.SoftScrim

object HeroCarouselDefaults {
    val ContainerHeight = 361.dp
    val ContainerPadding = PaddingValues(start = 32.dp, end = 32.dp, bottom = 20.dp)

    val BackgroundPadding = 288.dp
    val ContentWidth = 418.dp
    val ContentPadding = PaddingValues(32.dp)
    val TitlePadding = 4.dp
    val DescriptionPadding = 12.dp
    val ActionPadding = 20.dp

    val BackgroundGradient = listOf(SoftScrim.copy(alpha = 0f), SoftScrim)

    val BackgroundColor = SoftScrim.compositeOver(Color.Black)

    val ActiveBorder = BorderStroke(3.dp, Color.White)
    val InactiveBorder = BorderStroke(3.dp, Color.Transparent)

    fun border(active: Boolean) = if (active) ActiveBorder else InactiveBorder

    val Shape = ShapeDefaults.Medium
}
