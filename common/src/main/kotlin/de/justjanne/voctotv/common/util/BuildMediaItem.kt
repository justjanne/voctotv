package de.justjanne.voctotv.common.util

import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import de.justjanne.voctotv.voctoweb.model.LectureModel
import de.justjanne.voctotv.voctoweb.model.LiveConferenceModel
import de.justjanne.voctotv.voctoweb.model.LiveResourceModel
import de.justjanne.voctotv.voctoweb.model.LiveRoomModel
import de.justjanne.voctotv.voctoweb.model.LiveStreamModel
import de.justjanne.voctotv.voctoweb.model.ResourceModel

fun buildMediaItem(
    lecture: LectureModel,
    track: ResourceModel,
): MediaItem =
    MediaItem
        .Builder()
        .setUri(track.recordingUrl)
        .setMimeType(track.mimeType)
        .setMediaId(track.filename)
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(lecture.title)
                .setArtist(lecture.conferenceTitle)
                .setArtworkUri(lecture.posterUrl.toUri())
                .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
                .build(),
        ).setSubtitleConfigurations(
            buildList {
                lecture.resources?.filter { it.mimeType == MimeTypes.TEXT_VTT }?.forEach {
                    add(
                        MediaItem.SubtitleConfiguration
                            .Builder(
                                it.recordingUrl
                                    .replace(
                                        "https://cdn.media.ccc.de/",
                                        "https://static.media.ccc.de/media/",
                                    ).toUri(),
                            ).setMimeType(it.mimeType)
                            .setRoleFlags(C.ROLE_FLAG_CAPTION)
                            .setLabel(it.language)
                            .setLanguage(it.language)
                            .setSelectionFlags(0)
                            .build(),
                    )
                }
                lecture.resources?.filter { it.mimeType == MimeTypes.APPLICATION_SUBRIP }?.forEach {
                    add(
                        MediaItem.SubtitleConfiguration
                            .Builder(it.recordingUrl.toUri())
                            .setMimeType(it.mimeType)
                            .setRoleFlags(C.ROLE_FLAG_CAPTION)
                            .setLabel(it.language)
                            .setLanguage(it.language)
                            .setSelectionFlags(0)
                            .build(),
                    )
                }
            },
        ).build()

fun buildMediaItem(
    conference: LiveConferenceModel,
    room: LiveRoomModel,
    stream: LiveStreamModel,
    track: LiveResourceModel,
): MediaItem =
    MediaItem
        .Builder()
        .setLiveConfiguration(MediaItem.LiveConfiguration.Builder().build())
        .setUri(track.url.toUri())
        .setMediaId("${room.guid}-${stream.slug}")
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(room.display)
                .setArtist(conference.conference)
                .setArtworkUri(room.poster.toUri())
                .setMediaType(MediaMetadata.MEDIA_TYPE_VIDEO)
                .build(),
        ).build()
