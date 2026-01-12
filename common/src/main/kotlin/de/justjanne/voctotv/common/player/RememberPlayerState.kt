package de.justjanne.voctotv.common.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@UnstableApi
@Composable
fun UsePlayerState(viewModel: PlayerViewModel) {
    LaunchedEffect(viewModel.playerState, viewModel.playerState.status) {
        viewModel.playerState.update()
        while (viewModel.playerState.status == PlayerState.Status.PLAYING) {
            delay(50)
            viewModel.playerState.update()
        }
    }
}
