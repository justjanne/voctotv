package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LiveResourceModel(
    @SerialName("display")
    val display: String,
    @SerialName("tech")
    val tech: String,
    @SerialName("url")
    val url: String,
)
