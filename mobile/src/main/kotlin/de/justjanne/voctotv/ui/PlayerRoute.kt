package de.justjanne.voctotv.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import de.justjanne.voctotv.ui.player.PlayerOverlay
import de.justjanne.voctotv.ui.player.SubtitleDisplay
import de.justjanne.voctotv.viewmodel.PlayerViewModel

@OptIn(UnstableApi::class)
@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel,
) {
    val mediaItem by viewModel.mediaItem.collectAsState()
    val lecture by viewModel.lecture.collectAsState()

    LaunchedEffect(viewModel.mediaSession.player, lecture,mediaItem) {
        viewModel.mediaSession.player.apply {
            clearMediaItems()
            trackSelectionParameters = trackSelectionParameters.buildUpon()
                .setPreferredAudioLanguages(lecture?.originalLanguage ?: "")
                .setPreferredTextLanguages()
                .build()
            mediaItem?.let {
                setMediaItem(it)
            }
            prepare()
            playWhenReady = true
            play()
        }
    }

    Box(Modifier.fillMaxSize()) {
        ContentFrame(
            player = viewModel.mediaSession.player,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )

        SubtitleDisplay(viewModel)
        PlayerOverlay(viewModel, lecture)
    }
}
