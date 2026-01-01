@file:OptIn(ExperimentalUuidApi::class)

package de.ccc.media.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface LectureApi {
    @GET("public/events")
    suspend fun list(): LectureResult

    @GET("public/events/recent")
    suspend fun listRecent(): LectureResult

    @GET("public/events/{id}")
    suspend fun get(@Path("id") id: String): LectureModel?

    @GET("public/events/search")
    suspend fun search(@Query("q") query: String): LectureResult

    @Serializable
    data class LectureResult(
        @SerialName("events")
        val lectures: List<LectureModel>,
    )
}
