/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.api

import de.justjanne.voctotv.voctoweb.api.ConferenceApi
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class ConferenceTest {
    private val api: VoctowebApi = TestUtil.buildApi()

    @Test
    fun parseConference() =
        runTest {
            println(Json.decodeFromStream<ConferenceModel>(TestUtil.load("conferences_39c3.json")))
        }

    @Test
    fun parseConferenceList() =
        runTest {
            println(Json.decodeFromStream<ConferenceApi.ConferenceResult>(TestUtil.load("conferences.json")))
        }

    @Test
    fun loadConference() =
        runTest {
            println(api.conference.get("39c3"))
        }

    @Test
    fun loadConferenceMissing() =
        runTest {
            assertThrows<HttpException> { api.conference.get("01c3") }
        }
}
