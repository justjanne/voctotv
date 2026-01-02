package de.justjanne.voctotv.ui.player

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.Text
import de.justjanne.voctotv.R

@Composable
fun SubtitleSelection(
    player: Player,
    playPauseState: PlayPauseButtonState,
) {
    Box {
        val popupOpen = remember { mutableStateOf(false) }

        PlayerPopup(popupOpen.value, { popupOpen.value = false }) {
            val focusRequester = remember { FocusRequester() }

            Column(Modifier.padding(20.dp)) {
                val tracks = player.currentTracks.groups
                    .filter { it.type == C.TRACK_TYPE_TEXT }

                Text(
                    "Subtitles",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                )

                for (track in tracks) {
                    track.getTrackFormat(0).language?.let { language ->
                        val interactionSource = remember { MutableInteractionSource() }

                        ListItem(
                            selected = track.isSelected,
                            onClick = {
                                player.trackSelectionParameters =
                                    player.trackSelectionParameters.buildUpon()
                                        .setPreferredAudioLanguages(language)
                                        .build()
                                popupOpen.value = false
                            },
                            headlineContent = {
                                Text(language)
                            },
                            trailingContent = {
                                RadioButton(
                                    track.isSelected,
                                    interactionSource = interactionSource,
                                    onClick = {},
                                )
                            },
                            interactionSource = interactionSource,
                            modifier = Modifier.let {
                                if (track.isSelected) {
                                    it.focusRequester(focusRequester)
                                } else it
                            }
                        )

                        LaunchedEffect(focusRequester, track.isSelected) {
                            if (track.isSelected) {
                                focusRequester.requestFocus()
                            }
                        }
                    }
                }
            }
        }

        IconButton(
            enabled = playPauseState.isEnabled,
            onClick = {
                popupOpen.value = true
            },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_closed_caption),
                contentDescription = "Subtitles",
                tint = LocalContentColor.current,
            )
        }
    }
}