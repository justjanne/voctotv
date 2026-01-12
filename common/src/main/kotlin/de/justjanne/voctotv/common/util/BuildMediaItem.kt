package de.justjanne.voctotv.common.util

import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import de.justjanne.voctotv.voctoweb.model.LectureModel
import de.justjanne.voctotv.voctoweb.model.ResourceModel

fun buildMediaItem(
    lecture: LectureModel,
    track: ResourceModel,
): MediaItem =
    MediaItem
        .Builder()
        .setUri(track.recordingUrl)
        .setMediaId(track.filename)
        .setSubtitleConfigurations(
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
