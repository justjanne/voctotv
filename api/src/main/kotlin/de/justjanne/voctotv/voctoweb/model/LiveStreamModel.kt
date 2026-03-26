package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveStreamModel(
    @SerialName("slug")
    val slug: String,
    @SerialName("display")
    val display: String,
    @SerialName("type")
    val type: String,
    @SerialName("isTranslated")
    val isTranslated: Boolean,
    @SerialName("videoSize")
    val videoSize: Size?,
    @SerialName("urls")
    val urls: Map<String, LiveResourceModel>,
)
