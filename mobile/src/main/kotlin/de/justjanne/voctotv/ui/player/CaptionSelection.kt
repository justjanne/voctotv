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
import androidx.compose.runtime.DisposableEffect
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
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PlayPauseButtonState
import de.justjanne.voctotv.R

fun getLanguages(tracks: Tracks): List<String?> {
    return tracks.groups
        .filter { it.type == C.TRACK_TYPE_TEXT }
        .mapNotNull { it.getTrackFormat(0).language }
        .let { listOf(null).plus(it) }
}

@OptIn(UnstableApi::class)
@Composable
fun CaptionSelection(
    player: Player,
    playPauseState: PlayPauseButtonState,
) {
    val languages = remember {
        mutableStateOf(getLanguages(player.currentTracks))
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onTracksChanged(tracks: Tracks) {
                languages.value = getLanguages(tracks)
            }
        }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    Box {
        val popupOpen = remember { mutableStateOf(false) }

        PlayerPopup(popupOpen.value, { popupOpen.value = false }) {
            val focusRequester = remember { FocusRequester() }

            Column(Modifier.padding(20.dp)) {
                Text(
                    "Captions",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                )

                for (language in languages.value) {
                    val interactionSource = remember { MutableInteractionSource() }
                    val isSelected =
                        language == player.trackSelectionParameters.preferredTextLanguages.firstOrNull()

                    ListItem(
                        headlineContent = {
                            Text(language ?: "None")
                        },
                        trailingContent = {
                            RadioButton(
                                isSelected,
                                interactionSource = interactionSource,
                                onClick = {},
                            )
                        },
                        modifier = Modifier
                            .selectable(isSelected, interactionSource = interactionSource) {
                                player.trackSelectionParameters =
                                    player.trackSelectionParameters.buildUpon()
                                        .let {
                                            if (language == null) {
                                                it.setPreferredTextLanguages()
                                            } else {
                                                it.setPreferredTextLanguages(language)
                                            }
                                        }
                                        .build()
                                popupOpen.value = false
                            }.let {
                                if (isSelected) {
                                    it.focusRequester(focusRequester)
                                } else it
                            }
                    )

                    LaunchedEffect(focusRequester, isSelected) {
                        if (isSelected) {
                            focusRequester.requestFocus()
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
                contentDescription = "Captions",
                tint = LocalContentColor.current,
            )
        }
    }
}
