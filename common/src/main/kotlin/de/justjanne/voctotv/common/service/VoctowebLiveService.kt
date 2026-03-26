package de.justjanne.voctotv.common.service

import android.provider.MediaStore
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.LiveConferenceModel
import de.justjanne.voctotv.voctoweb.model.LiveRoomModel
import de.justjanne.voctotv.voctoweb.model.VideoModel
import javax.inject.Inject

class VoctowebLiveService
@Inject
constructor(
    private val api: VoctowebApi,
) {
    suspend fun listConferences(): List<LiveConferenceModel>? =
        try {
            api.streaming.streams()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    suspend fun listRooms(): List<VideoModel.Live>? =
        try {
            api.streaming.streams()
                .flatMap { conference ->
                    conference.groups.flatMap { it.rooms }
                        .map { VideoModel.Live(conference, it) }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    suspend fun getRoom(roomId: String): VideoModel.Live? {
        val conferences = listConferences() ?: return null
        for (conference in conferences) {
            for (group in conference.groups) {
                for (room in group.rooms) {
                    if (room.guid == roomId) {
                        return VideoModel.Live(conference, room)
                    }
                }
            }
        }
        return null
    }
}
