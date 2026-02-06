package de.justjanne.voctotv.common.service

import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import javax.inject.Inject

class VoctowebConferenceService
    @Inject
    constructor(
        private val api: VoctowebApi,
    ) {
        suspend fun getConference(conferenceId: String): ConferenceModel? =
            try {
                api.conference.get(conferenceId)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

        suspend fun listConferences(): List<ConferenceModel> =
            try {
                api.conference.list().conferences
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
    }
