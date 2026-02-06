package de.justjanne.voctotv.common.service

import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.LectureModel
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class VoctowebLectureService
@Inject constructor(
    private val api: VoctowebApi,
) {
    suspend fun getLecture(lectureId: String): LectureModel? = try {
        api.lecture.get(lectureId)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    suspend fun listPopular(): List<LectureModel> = try {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val cutoff = now.minus(1, ChronoUnit.YEARS)

        val current = api.lecture.listPopular(now.year).lectures
        val previous = api.lecture.listPopular(now.year - 1).lectures

        current.plus(previous)
            .filter { it.releaseDate.isAfter(cutoff) }
            .sortedByDescending { it.viewCount }
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun listRecent(): List<LectureModel> = try {
        api.lecture.listRecent().lectures
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }

    suspend fun listPromoted(): List<LectureModel> = try {
        api.lecture.listPromoted().lectures
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
