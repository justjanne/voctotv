/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.api

import de.justjanne.voctotv.mediacccde.api.ResourceApi
import de.justjanne.voctotv.mediacccde.api.VoctowebApi
import de.justjanne.voctotv.mediacccde.model.ResourceModel
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import kotlin.test.Test

@OptIn(ExperimentalSerializationApi::class)
class ResourceTest {
    private val api: VoctowebApi = TestUtil.buildApi()

    @Test
    fun parseResource() =
        runTest {
            println(Json.decodeFromStream<ResourceModel>(TestUtil.load("recordings_94376.json")))
        }

    @Test
    fun parseResourceList() =
        runTest {
            println(Json.decodeFromStream<ResourceApi.ResourceResult>(TestUtil.load("recordings.json")))
        }

    @Test
    fun loadResource() =
        runTest {
            println(api.resource.get("94736"))
        }

    @Test
    fun loadResourceMissing() =
        runTest {
            assertThrows<HttpException> { api.lecture.get("0") }
        }
}
