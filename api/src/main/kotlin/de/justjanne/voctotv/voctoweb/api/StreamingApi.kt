/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

@file:OptIn(ExperimentalUuidApi::class)

package de.justjanne.voctotv.voctoweb.api

import de.justjanne.voctotv.voctoweb.model.LiveConferenceModel
import retrofit2.http.GET
import kotlin.uuid.ExperimentalUuidApi

interface StreamingApi {
    @GET("streams/v2.json")
    suspend fun streams(): List<LiveConferenceModel>
}
