package de.justjanne.voctotv.common.util

import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import de.justjanne.voctotv.voctoweb.model.LectureModel

const val FilterKeyOther = "Other"

fun calculateTracks(
    conference: ConferenceModel,
    lecture: LectureModel,
): List<String> {
    val tags =
        lecture.tags
            .filter { !it.all(Char::isDigit) }
            .filter { !it.startsWith("${conference.acronym}-") }
            .filter { it != conference.acronym }
    return tags.ifEmpty { listOf(FilterKeyOther) }
}
