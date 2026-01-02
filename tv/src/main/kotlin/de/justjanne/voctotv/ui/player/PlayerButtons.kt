package de.justjanne.voctotv.ui.player

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.tv.material3.*
import de.ccc.media.api.LectureModel
import de.justjanne.voctotv.R

@Composable
fun PlayerButtons(
    player: Player,
    lecture: LectureModel?,
) {
    val playPauseState = rememberPlayPauseButtonState(player)

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
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
        ) {
            Icon(
                painter = painterResource(if (playPauseState.showPlay) R.drawable.play_arrow else R.drawable.pause),
                contentDescription = if (playPauseState.showPlay) "Play" else "Pause",
                tint = LocalContentColor.current,
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
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
                                    selected = audioTrack.isSelected,
                                    onClick = {
                                        player.trackSelectionParameters =
                                            player.trackSelectionParameters.buildUpon()
                                                .setPreferredAudioLanguages(language)
                                                .build()
                                        popupOpen.value = false
                                    },
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
                                    interactionSource = interactionSource,
                                    modifier = Modifier.let {
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
                        painter = painterResource(R.drawable.translate),
                        contentDescription = "Language",
                        tint = LocalContentColor.current,
                    )
                }
            }
        }
    }
}
