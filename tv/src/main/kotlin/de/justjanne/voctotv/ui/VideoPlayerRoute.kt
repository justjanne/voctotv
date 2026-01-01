package de.justjanne.voctotv

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AndroidUiDispatcher
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.PlayerSurface
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun VideoPlayerRoute(id: String) {
    var player by remember { mutableStateOf<Player?>(null) }
    val context = LocalContext.current

    DisposableEffect(id, context) {
        val job = GlobalScope.launch(AndroidUiDispatcher.Main) {
            val lecture = requireNotNull(DI.api.lecture.get(id))
            val resource = requireNotNull(lecture.resources).first {
                it.mimeType == "video/mp4" && it.highQuality
            }
            val mediaItem = MediaItem.Builder().setUri(resource.recordingUrl).setMediaId(id).build()
            player = ExoPlayer.Builder(context).build().apply {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
                play()
            }
        }
        onDispose {
            job.cancel()
            player?.release()
        }
    }

    player?.let {
        PlayerSurface(
            player = it,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )
    }
}