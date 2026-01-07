/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mediacccde.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResourceModel(
    @SerialName("size") val size: Int?,
    @SerialName("length") val length: Int?,
    @SerialName("mime_type") val mimeType: String,
    @SerialName("language") val language: String,
    @SerialName("filename") val filename: String,
    @SerialName("state") val state: String,
    @SerialName("folder") val folder: String,
    @SerialName("high_quality") val highQuality: Boolean,
    @SerialName("width") val width: Int?,
    @SerialName("height") val height: Int?,
    @SerialName("updated_at") val updatedAt: Timestamp,
    @SerialName("recording_url") val recordingUrl: String,
    @SerialName("url") val url: String,
    @SerialName("event_url") val eventUrl: String,
    @SerialName("conference_url") val conferenceUrl: String,
)
