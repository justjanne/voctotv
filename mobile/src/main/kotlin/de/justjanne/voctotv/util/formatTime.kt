package de.justjanne.voctotv.util

import androidx.media3.common.C
import java.util.*
import kotlin.math.abs

fun formatTime(timeMs: Long): String {
    if (timeMs == C.TIME_UNSET) {
        return formatTime(0);
    }
    val prefix = if (timeMs < 0) "-" else ""
    val timeMsValue = abs(timeMs)
    val totalSeconds = (timeMsValue + 500) / 1000
    val seconds = totalSeconds % 60
    val minutes = (totalSeconds / 60) % 60
    val hours = totalSeconds / 3600
    return if (hours > 0) String.format(Locale.ROOT, "%s%d:%02d:%02d", prefix, hours, minutes, seconds)
    else String.format(Locale.ROOT, "%s%02d:%02d", prefix, minutes, seconds)
}
