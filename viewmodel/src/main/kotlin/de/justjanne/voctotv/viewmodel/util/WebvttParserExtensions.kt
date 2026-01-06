package de.justjanne.voctotv.viewmodel.util

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.text.CuesWithTiming
import androidx.media3.extractor.text.SubtitleParser
import androidx.media3.extractor.text.webvtt.WebvttParser

@OptIn(UnstableApi::class)
fun WebvttParser.parse(
    data: ByteArray,
    options: SubtitleParser.OutputOptions
): List<CuesWithTiming> = buildList {
    parse(data, options) {
        add(it)
    }
}
