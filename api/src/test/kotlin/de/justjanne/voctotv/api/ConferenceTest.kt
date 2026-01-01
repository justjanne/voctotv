package de.justjanne.voctotv.api

import de.ccc.media.api.ConferenceApi
import de.ccc.media.api.ConferenceModel
import de.ccc.media.api.VoctowebApi
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
    fun parseConference() = runTest {
        println(Json.decodeFromStream<ConferenceModel>(TestUtil.load("conferences_39c3.json")))
    }

    @Test
    fun parseConferenceList() = runTest {
        println(Json.decodeFromStream<ConferenceApi.ConferenceResult>(TestUtil.load("conferences.json")))
    }

    @Test
    fun loadConference() = runTest {
        println(api.conference.get("39c3"))
    }

    @Test
    fun loadConferenceMissing() = runTest {
        assertThrows<HttpException> { api.conference.get("01c3") }
    }
}
