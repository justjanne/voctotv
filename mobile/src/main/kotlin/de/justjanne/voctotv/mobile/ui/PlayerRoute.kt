package de.justjanne.voctotv.mobile.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import de.justjanne.voctotv.common.subtitles.SubtitleDisplay
import de.justjanne.voctotv.common.player.rememberPlayerState
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.ui.player.PlayerOverlay

@OptIn(UnstableApi::class)
@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel,
    back: () -> Unit,
) {
    val mediaItem by viewModel.mediaItem.collectAsState()
    val lecture by viewModel.lecture.collectAsState()

    LaunchedEffect(viewModel.mediaSession.player, lecture, mediaItem) {
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

    val playerState = rememberPlayerState(viewModel.mediaSession.player)

    Scaffold { contentPadding ->
        Box(Modifier.fillMaxSize()) {
        ContentFrame(
            player = viewModel.mediaSession.player,
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )

        SubtitleDisplay(viewModel.mediaSession.player, contentPadding)
        PlayerOverlay(viewModel, lecture, playerState, contentPadding, back)
	}
    }
}
