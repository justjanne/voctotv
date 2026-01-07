/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.previews

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.text.CuesWithTiming
import androidx.media3.extractor.text.SubtitleParser
import androidx.media3.extractor.text.webvtt.WebvttParser

@OptIn(UnstableApi::class)
fun WebvttParser.parse(
    data: ByteArray,
    options: SubtitleParser.OutputOptions,
): List<CuesWithTiming> =
    buildList {
        parse(data, options) {
            add(it)
        }
    }
