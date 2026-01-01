package de.justjanne.voctotv.api

import de.ccc.media.api.ResourceApi
import de.ccc.media.api.ResourceModel
import de.ccc.media.api.VoctowebApi
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
    fun parseResource() = runTest {
        println(Json.decodeFromStream<ResourceModel>(TestUtil.load("recordings_94376.json")))
    }

    @Test
    fun parseResourceList() = runTest {
        println(Json.decodeFromStream<ResourceApi.ResourceResult>(TestUtil.load("recordings.json")))
    }

    @Test
    fun loadResource() = runTest {
        println(api.resource.get("94736"))
    }

    @Test
    fun loadResourceMissing() = runTest {
        assertThrows<HttpException> { api.lecture.get("0") }
    }
}
