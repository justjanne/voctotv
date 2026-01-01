package de.ccc.media.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

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
