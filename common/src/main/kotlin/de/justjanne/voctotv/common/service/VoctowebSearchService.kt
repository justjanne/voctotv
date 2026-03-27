package de.justjanne.voctotv.common.service

import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.LectureModel
import javax.inject.Inject

class VoctowebSearchService
    @Inject
    constructor(
        private val api: VoctowebApi,
    ) {
        suspend fun search(
            query: String,
            page: Int = 1,
        ): List<LectureModel>? =
            try {
                api.lecture.search(query, page).lectures
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
    }
