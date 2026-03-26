package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class LiveRoomModel(
    @SerialName("guid")
    val guid: String,
    @SerialName("slug")
    val slug: String,
    @SerialName("schedulename")
    val schedulename: String,
    @SerialName("thumb")
    val thumb: String,
    @SerialName("poster")
    val poster: String,
    @SerialName("link")
    val link: String,
    @SerialName("display")
    val display: String,
    @SerialName("stream")
    val stream: String,
    //@SerialName("talks")
    //val talks: LiveTalksModel,
    @SerialName("streams")
    val streams: List<LiveStreamModel>,
)
