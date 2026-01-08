/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.voctoweb.api

import de.justjanne.voctotv.voctoweb.model.ResourceModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path

interface ResourceApi {
    @GET("public/recordings")
    suspend fun list(): ResourceResult

    @GET("public/recordings/{id}")
    suspend fun get(
        @Path("id") id: String,
    ): ResourceModel?

    @Serializable
    data class ResourceResult(
        @SerialName("recordings")
        val recordings: List<ResourceModel>,
    )
}
