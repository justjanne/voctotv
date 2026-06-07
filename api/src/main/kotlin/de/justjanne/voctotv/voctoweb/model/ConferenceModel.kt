/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class ConferenceModel(
    @SerialName("acronym") val acronym: String,
    @SerialName("aspect_ratio") val aspectRatio: String,
    @SerialName("created_at") val createdAt: Timestamp? = null,
    @SerialName("updated_at") val updatedAt: Timestamp? = null,
    @SerialName("title") val title: String,
    @SerialName("schedule_url") val scheduleUrl: String? = null,
    @SerialName("slug") val slug: String,
    @SerialName("event_last_released_at") val eventLastReleasedAt: Timestamp? = null,
    @SerialName("link") val link: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("webgen_location") val webgenLocation: String,
    @SerialName("logo_url") val logoUrl: String,
    @SerialName("images_url") val imagesUrl: String,
    @SerialName("recordings_url") val recordingsUrl: String,
    @SerialName("url") val url: String,
    @SerialName("events") val lectures: List<LectureModel>? = null,
)
