package de.justjanne.voctotv.api

import de.ccc.media.api.LectureApi
import de.ccc.media.api.LectureModel
import de.ccc.media.api.VoctowebApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.junit.jupiter.api.assertThrows
import retrofit2.HttpException
import kotlin.test.Test


@OptIn(ExperimentalSerializationApi::class)
class LectureTest {
    private val api: VoctowebApi = TestUtil.buildApi()

    @Test
    fun parseLecture() = runTest {
        println(Json.decodeFromStream<LectureModel>(TestUtil.load("events_1840.json")))
    }

    @Test
    fun parseLectureList() = runTest {
        println(Json.decodeFromStream<LectureApi.LectureResult>(TestUtil.load("events.json")))
    }

    @Test
    fun parseRecentLectureList() = runTest {
        println(Json.decodeFromStream<LectureApi.LectureResult>(TestUtil.load("events_recent.json")))
    }

    @Test
    fun loadLecture() = runTest {
        println(api.lecture.get("341961a3-599d-52b9-8262-34c1757c9698"))
    }

    @Test
    fun loadLectureMissing() = runTest {
        assertThrows<HttpException> { api.lecture.get("00000000-0000-0000-0000-000000000000") }
    }

    @Test
    fun searchLecture() = runTest {
        println(api.lecture.search("gpn"))
    }
}
