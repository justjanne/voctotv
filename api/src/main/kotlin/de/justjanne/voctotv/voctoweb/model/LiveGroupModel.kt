package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonIgnoreUnknownKeys
data class LiveGroupModel(
    @SerialName("group")
    val group: String,
    @SerialName("rooms")
    val rooms: List<LiveRoomModel>,
)
