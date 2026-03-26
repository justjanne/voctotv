package de.justjanne.voctotv.mobile.route.player

import androidx.compose.runtime.Composable
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoTitle(
    video: VideoModel?,
    onClick: () -> Unit,
) {
    when (video) {
        is VideoModel.Live ->
            VideoTitleLayout(
                video.room.display,
                video.conference.conference,
                onClick,
            )

        is VideoModel.Vod ->
            VideoTitleLayout(
                video.lecture.title,
                video.lecture.conferenceTitle,
                onClick,
            )

        null -> Unit
    }
}
