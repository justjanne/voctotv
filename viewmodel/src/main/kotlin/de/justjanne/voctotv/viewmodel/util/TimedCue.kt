package de.justjanne.voctotv.viewmodel.util

import okhttp3.HttpUrl

data class TimedCue(
    val startUs: Long,
    val endUs: Long,
    val data: HttpUrl,
)
