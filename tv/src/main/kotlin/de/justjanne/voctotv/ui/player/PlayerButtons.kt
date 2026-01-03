package de.justjanne.voctotv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.tv.material3.IconButton
import androidx.tv.material3.IconButtonDefaults
import androidx.tv.material3.LocalContentColor
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.R

@OptIn(UnstableApi::class)
@Composable
fun PlayerButtons(
    player: Player,
    playPauseState: PlayPauseButtonState,
    lecture: LectureModel?,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Start,
        ) {

        }

        IconButton(
            onClick = {
                if (player.isPlaying) {
                    player.pause()
                } else player.play()
            },
            modifier = Modifier.requiredSize(IconButtonDefaults.LargeButtonSize)
        ) {
            Icon(
                painter = painterResource(if (playPauseState.showPlay) R.drawable.ic_play_arrow else R.drawable.ic_pause),
                contentDescription = if (playPauseState.showPlay) "Play" else "Pause",
                tint = LocalContentColor.current,
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
        ) {
            //SubtitleSelection(player, playPauseState)
            AudioSelection(player, playPauseState, lecture)
        }
    }
}
