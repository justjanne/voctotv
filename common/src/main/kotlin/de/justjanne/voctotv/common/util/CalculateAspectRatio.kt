package de.justjanne.voctotv.common.util

import androidx.media3.common.VideoSize

fun calculateAspectRatio(videoSize: VideoSize): Float =
    if (videoSize.width > 0 && videoSize.height > 0 && videoSize.pixelWidthHeightRatio > 0) {
        videoSize.width.toFloat() / videoSize.height.toFloat() * videoSize.pixelWidthHeightRatio
    } else {
        0f
    }
