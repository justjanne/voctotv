package de.justjanne.voctotv.common.viewmodel

import de.justjanne.voctotv.voctoweb.model.LectureModel
import de.justjanne.voctotv.voctoweb.model.LiveConferenceModel
import de.justjanne.voctotv.voctoweb.model.LiveRoomModel

sealed interface FeaturedItem {
    data class Lecture(
        val lecture: LectureModel,
    ) : FeaturedItem

    data class Stream(
        val conference: LiveConferenceModel,
        val rooms: List<LiveRoomModel>,
    ) : FeaturedItem
}
