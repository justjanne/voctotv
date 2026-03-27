/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.api

import de.justjanne.voctotv.voctoweb.model.LiveConferenceModel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class StreamingTest {
    @Test
    fun parseConferences1() =
        runTest {
            println(Json.decodeFromStream<List<LiveConferenceModel>>(TestUtil.load("streaming1.json")))
        }

    @Test
    fun parseConferences2() =
        runTest {
            println(Json.decodeFromStream<List<LiveConferenceModel>>(TestUtil.load("streaming2.json")))
        }

    @Test
    fun parseConferences3() =
        runTest {
            println(Json.decodeFromStream<List<LiveConferenceModel>>(TestUtil.load("streaming3.json")))
        }
}
