package de.justjanne.voctotv.voctoweb.model

sealed interface VideoModel {
    data class Vod(val lecture: LectureModel) : VideoModel
    data class Live(val conference: LiveConferenceModel, val room: LiveRoomModel) : VideoModel
}
