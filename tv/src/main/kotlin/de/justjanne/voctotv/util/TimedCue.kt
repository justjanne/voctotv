package de.justjanne.voctotv.util

import okhttp3.HttpUrl

data class TimedCue(
    val startUs: Long,
    val endUs: Long,
    val data: HttpUrl,
)
