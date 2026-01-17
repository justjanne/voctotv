/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@file:OptIn(ExperimentalUuidApi::class)

package de.justjanne.voctotv.voctoweb.api

import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import kotlin.uuid.ExperimentalUuidApi

interface LectureApi {
    @GET("public/events")
    suspend fun list(): LectureResult

    @GET("public/events/recent")
    suspend fun listRecent(): LectureResult

    @GET("public/events/promoted")
    suspend fun listPromoted(): LectureResult

    @GET("public/events/{id}")
    suspend fun get(
        @Path("id") id: String,
    ): LectureModel?

    @GET("public/events/search")
    suspend fun search(
        @Query("q") query: String,
    ): LectureResult

    @Serializable
    data class LectureResult(
        @SerialName("events")
        val lectures: List<LectureModel>,
    )
}
