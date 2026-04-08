package de.justjanne.voctotv.common.watchlater

import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.serialization.Serializable

@Serializable
data class WatchLaterItem(
    val guid: String,
    val title: String,
    val persons: List<String>,
    val duration: Long,
    val thumbUrl: String,
    val conferenceTitle: String,
) {
    companion object {
        fun fromLecture(lecture: LectureModel): WatchLaterItem =
            WatchLaterItem(
                guid = lecture.guid,
                title = lecture.title,
                persons = lecture.persons,
                duration = lecture.duration,
                thumbUrl = lecture.thumbUrl,
                conferenceTitle = lecture.conferenceTitle,
            )
    }
}

fun WatchLaterItem.toLectureModel(): LectureModel =
    LectureModel(
        guid = guid,
        title = title,
        subtitle = null,
        slug = guid,
        link = null,
        description = null,
        originalLanguage = "",
        persons = persons,
        tags = emptyList(),
        viewCount = 0,
        promoted = false,
        date = null,
        releaseDate = null,
        updatedAt = null,
        length = duration,
        duration = duration,
        thumbUrl = thumbUrl,
        posterUrl = thumbUrl,
        timelineUrl = "",
        thumbnailsUrl = "",
        frontendLink = "",
        url = "",
        conferenceTitle = conferenceTitle,
        conferenceUrl = "",
        related = emptyList(),
        resources = null,
    )
