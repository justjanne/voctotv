package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.ScrubbingModeParameters
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import de.justjanne.voctotv.ui.player.PlayerOverlay
import de.justjanne.voctotv.viewmodel.PlayerViewModel

@Composable
fun rememberPlayer(): Player {
    val context = LocalContext.current
    val player = remember(context) {
        ExoPlayer.Builder(context)
            .setScrubbingModeParameters(
                ScrubbingModeParameters.DEFAULT.buildUpon()
                    .setDisabledTrackTypes(setOf(C.TRACK_TYPE_AUDIO))
                    .build()
            )
            .build()
    }
    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }
    return player
}

@Composable
fun PlayerRoute(
    viewModel: PlayerViewModel,
) {
    val mediaItem by viewModel.mediaItem.collectAsState()
    val lecture by viewModel.lecture.collectAsState()
    val player = rememberPlayer()

    LaunchedEffect(player, lecture,mediaItem) {
        player.clearMediaItems()
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .setPreferredAudioLanguages(lecture?.originalLanguage ?: "")
            .setPreferredTextRoleFlags(C.ROLE_FLAG_TRICK_PLAY)
            .build()
        mediaItem?.let {
            player.setMediaItem(it)
        }
        player.prepare()
        player.playWhenReady = true
        player.play()
    }

    Box(Modifier.fillMaxSize()) {
        ContentFrame(
            player = player,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        )

        PlayerOverlay(lecture, player)
    }
}
