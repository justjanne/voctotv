package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.media3.cast.MediaRouteButton
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun PlayerActions(
    viewModel: PlayerViewModel,
    video: VideoModel?,
) {
    if (video == null) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
    ) {
        CaptionSelection(viewModel.mediaSession.player, viewModel.playerState)
        AudioSelection(viewModel.mediaSession.player, viewModel.playerState, video)
        MediaRouteButton()
    }
}
