package de.ccc.media.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ResourceApi {
    @GET("public/recordings")
    suspend fun list(): ResourceResult

    @GET("public/recordings/{id}")
    suspend fun get(@Path("id") id: String): ResourceModel?

    @Serializable
    data class ResourceResult(
        @SerialName("recordings")
        val recordings: List<ResourceModel>,
    )
}
