package de.justjanne.voctotv.tv.route.player

import androidx.compose.runtime.Composable
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoTitle(video: VideoModel?) {
    when (video) {
        is VideoModel.Live ->
            VideoTitleLayout(
                video.room.display,
                video.conference.conference,
            )

        is VideoModel.Vod ->
            VideoTitleLayout(
                video.lecture.title,
                video.lecture.conferenceTitle,
            )

        null -> Unit
    }
}
