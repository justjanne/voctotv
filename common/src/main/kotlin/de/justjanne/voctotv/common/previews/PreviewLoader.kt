/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.previews

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.extractor.text.SubtitleParser
import androidx.media3.extractor.text.webvtt.WebvttParser
import de.justjanne.voctotv.common.util.await
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class PreviewLoader
    @Inject
    constructor(
        private val client: OkHttpClient,
    ) {
        @OptIn(UnstableApi::class)
        suspend fun load(url: String): List<TimedCue>? {
            val baseUrl = url.toHttpUrlOrNull() ?: return null
            val parser = WebvttParser()
            val response = client.newCall(Request.Builder().url(url).build()).await()
            val data = response.body?.bytes() ?: return null
            return parser.parse(data, SubtitleParser.OutputOptions.allCues()).mapNotNull {
                it.cues.firstOrNull()?.text?.let { baseUrl.resolve(it.toString()) }?.let { url ->
                    TimedCue(it.startTimeUs, it.endTimeUs, url)
                }
            }
        }
    }
