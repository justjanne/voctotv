/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class LectureModel(
    @SerialName("guid") val guid: String,
    @SerialName("title") val title: String,
    @SerialName("subtitle") val subtitle: String?,
    @SerialName("slug") val slug: String,
    @SerialName("link") val link: String?,
    @SerialName("description") val description: String?,
    @SerialName("original_language") val originalLanguage: String,
    @SerialName("persons") val persons: List<String>,
    @SerialName("tags") val tags: List<String>,
    @SerialName("view_count") val viewCount: Long,
    @SerialName("promoted") val promoted: Boolean,
    @SerialName("date") val date: String?,
    @SerialName("release_date") val releaseDate: Timestamp,
    @SerialName("updated_at") val updatedAt: Timestamp,
    @SerialName("length") val length: Long,
    @SerialName("duration") val duration: Long,
    @SerialName("thumb_url") val thumbUrl: String,
    @SerialName("poster_url") val posterUrl: String,
    @SerialName("timeline_url") val timelineUrl: String,
    @SerialName("thumbnails_url") val thumbnailsUrl: String,
    @SerialName("frontend_link") val frontendLink: String,
    @SerialName("url") val url: String,
    @SerialName("conference_title") val conferenceTitle: String,
    @SerialName("conference_url") val conferenceUrl: String,
    @SerialName("related") val related: List<Related>,
    @SerialName("recordings") val resources: List<ResourceModel>? = null,
) {
    @Serializable
    data class Related(
        @SerialName("event_id") val lectureId: Int,
        @SerialName("event_guid") val lectureGuid: String,
        @SerialName("weight") val weight: Int,
    )
}
