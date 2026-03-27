package de.justjanne.voctotv.voctoweb.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

@Serializable(with = LiveTalkModel.Serializer::class)
sealed interface LiveTalkModel {
    @Serializable
    data class Break(
        @SerialName("fstart")
        val startTimestamp: Timestamp,
        @SerialName("fend")
        val endTimestamp: Timestamp,
        @SerialName("tstart")
        val startText: String,
        @SerialName("tend")
        val endText: String,
        @SerialName("start")
        val startInstant: Int,
        @SerialName("end")
        val endInstant: Int,
        @SerialName("offset")
        val offset: Int,
        @SerialName("duration")
        val duration: Int,
        @SerialName("special")
        val kind: String,
        @SerialName("guid")
        val guid: String? = null,
        @SerialName("code")
        val code: String? = null,
        @SerialName("track")
        val track: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("speaker")
        val speaker: String? = null,
        @SerialName("room_known")
        val roomKnown: Boolean? = null,
        @SerialName("optout")
        val optOut: Boolean? = null,
        @SerialName("url")
        val url: String? = null,
    ) : LiveTalkModel

    @Serializable
    data class Talk(
        @SerialName("fstart")
        val startTimestamp: Timestamp,
        @SerialName("fend")
        val endTimestamp: Timestamp,
        @SerialName("tstart")
        val startText: String,
        @SerialName("tend")
        val endText: String,
        @SerialName("start")
        val startInstant: Int,
        @SerialName("end")
        val endInstant: Int,
        @SerialName("offset")
        val offset: Int,
        @SerialName("duration")
        val duration: Int,
        @SerialName("guid")
        val guid: String,
        @SerialName("code")
        val code: String,
        @SerialName("track")
        val track: String,
        @SerialName("title")
        val title: String,
        @SerialName("speaker")
        val speaker: String,
        @SerialName("room_known")
        val roomKnown: Boolean,
        @SerialName("optout")
        val optOut: Boolean,
        @SerialName("url")
        val url: String,
    ) : LiveTalkModel

    object Serializer : JsonContentPolymorphicSerializer<LiveTalkModel>(LiveTalkModel::class) {
        override fun selectDeserializer(element: JsonElement): KSerializer<out LiveTalkModel> {
            return if ("special" in element.jsonObject) {
                Break.serializer()
            } else {
                Talk.serializer()
            }
        }
    }
}
