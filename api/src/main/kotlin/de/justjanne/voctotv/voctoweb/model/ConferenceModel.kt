/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConferenceModel(
    @SerialName("acronym") val acronym: String,
    @SerialName("aspect_ratio") val aspectRatio: String,
    @SerialName("updated_at") val updatedAt: Timestamp,
    @SerialName("title") val title: String,
    @SerialName("schedule_url") val scheduleUrl: String?,
    @SerialName("slug") val slug: String,
    @SerialName("event_last_released_at") val eventLastReleasedAt: Timestamp?,
    @SerialName("link") val link: String?,
    @SerialName("description") val description: String?,
    @SerialName("webgen_location") val webgenLocation: String,
    @SerialName("logo_url") val logoUrl: String,
    @SerialName("images_url") val imagesUrl: String,
    @SerialName("recordings_url") val recordingsUrl: String,
    @SerialName("url") val url: String,
    @SerialName("events") val lectures: List<LectureModel>? = null,
)
