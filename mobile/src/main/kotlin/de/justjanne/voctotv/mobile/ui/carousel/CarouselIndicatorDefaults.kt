package de.justjanne.voctotv.mobile.ui.carousel

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object CarouselIndicatorDefaults {
    val IndicatorPadding = 16.dp
    val IndicatorSpacing = 8.dp

    val IndicatorActive = Color.White
    val IndicatorInactive = Color.White.copy(alpha = 0.3f)
    val IndicatorShape = CircleShape
    val IndicatorSize = 8.dp
}
