package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveGroupModel(
    @SerialName("group")
    val group: String,
    @SerialName("rooms")
    val rooms: List<LiveRoomModel>,
)
