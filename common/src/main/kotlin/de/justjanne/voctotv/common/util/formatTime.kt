package de.justjanne.voctotv.common.util

import androidx.compose.ui.util.fastCoerceAtLeast
import java.util.*

fun formatTime(timeMs: Long): String {
    val actualTimeMs = timeMs.fastCoerceAtLeast(0L)
    val totalSeconds = (actualTimeMs + 500) / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) String.format(Locale.ROOT, "%d:%02d:%02d", hours, minutes, seconds)
    else String.format(Locale.ROOT, "%02d:%02d", minutes, seconds)
}
