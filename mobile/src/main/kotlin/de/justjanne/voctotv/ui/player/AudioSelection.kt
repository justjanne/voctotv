package de.justjanne.voctotv.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PlayPauseButtonState
import de.justjanne.voctotv.R
import de.justjanne.voctotv.mediacccde.model.LectureModel

@OptIn(UnstableApi::class)
@Composable
fun AudioSelection(
    player: Player,
    playPauseState: PlayPauseButtonState,
    lecture: LectureModel?,
) {
    Box {
        val popupOpen = remember { mutableStateOf(false) }

        PlayerPopup(popupOpen.value, { popupOpen.value = false }) {
            val focusRequester = remember { FocusRequester() }

            Column(Modifier.padding(20.dp)) {
                val audioTracks = player.currentTracks.groups
                    .filter { it.type == C.TRACK_TYPE_AUDIO }

                Text(
                    "Language",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                )

                for (audioTrack in audioTracks) {
                    audioTrack.getTrackFormat(0).language?.let { language ->
                        val interactionSource = remember { MutableInteractionSource() }

                        ListItem(
                            headlineContent = {
                                if (lecture?.originalLanguage?.startsWith(language) == true) {
                                    Text("${language} (Original)")
                                } else {
                                    Text(language)
                                }
                            },
                            trailingContent = {
                                RadioButton(
                                    audioTrack.isSelected,
                                    interactionSource = interactionSource,
                                    onClick = {},
                                )
                            },
                            modifier = Modifier
                                .selectable(audioTrack.isSelected, interactionSource = interactionSource) {
                                    player.trackSelectionParameters =
                                        player.trackSelectionParameters.buildUpon()
                                            .setPreferredAudioLanguages(language)
                                            .build()
                                    popupOpen.value = false
                                }
                                .let {
                                    if (audioTrack.isSelected) {
                                        it.focusRequester(focusRequester)
                                    } else it
                                }
                        )

                        LaunchedEffect(focusRequester, audioTrack.isSelected) {
                            if (audioTrack.isSelected) {
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
                painter = painterResource(R.drawable.ic_translate),
                contentDescription = "Language",
                tint = LocalContentColor.current,
            )
        }
    }
}
