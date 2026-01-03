package de.justjanne.voctotv.mediacccde.api

import de.justjanne.voctotv.mediacccde.model.ConferenceModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ConferenceApi {
    @GET("public/conferences")
    suspend fun list(): ConferenceResult

    @GET("public/conferences/{id}")
    suspend fun get(@Path("id") id: String): ConferenceModel?

    @Serializable
    data class ConferenceResult(
        @SerialName("conferences")
        val conferences: List<ConferenceModel>,
    )
}
