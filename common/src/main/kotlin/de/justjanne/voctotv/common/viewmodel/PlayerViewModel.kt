package de.justjanne.voctotv.common.viewmodel

import androidx.media3.session.MediaSession
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.previews.TimedCue
import de.justjanne.voctotv.voctoweb.model.VideoModel
import kotlinx.coroutines.flow.StateFlow

interface PlayerViewModel {
    val video: StateFlow<VideoModel?>
    val previews: StateFlow<List<TimedCue>>
    val mediaSession: MediaSession
    val playerState: PlayerState
}
