/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

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
    suspend fun get(
        @Path("id") id: String,
    ): ConferenceModel?

    @Serializable
    data class ConferenceResult(
        @SerialName("conferences")
        val conferences: List<ConferenceModel>,
    )
}
