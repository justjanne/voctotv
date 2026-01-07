package de.justjanne.voctotv.common.subtitles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.media3.common.Player
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup

@Composable
fun rememberCueState(player: Player): List<Cue> {
    val currentCue = remember { mutableStateOf<List<Cue>>(emptyList()) }
    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onCues(cueGroup: CueGroup) {
                currentCue.value = cueGroup.cues
            }
        }

        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }
    return currentCue.value
}
