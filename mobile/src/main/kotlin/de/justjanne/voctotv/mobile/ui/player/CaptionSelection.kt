package de.justjanne.voctotv.mobile.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.R

fun getLanguages(tracks: Tracks): List<String?> =
    tracks.groups
        .filter { it.type == C.TRACK_TYPE_TEXT }
        .mapNotNull { it.getTrackFormat(0).language }
        .let { listOf(null).plus(it) }

@OptIn(UnstableApi::class)
@Composable
fun CaptionSelection(
    player: Player,
    playerState: PlayerState,
) {
    val popupOpen = remember { mutableStateOf(false) }

    LaunchedEffect(playerState.casting) {
        if (playerState.casting) {
            popupOpen.value = false
        }
    }

    val languages =
        remember {
            mutableStateOf(getLanguages(player.currentTracks))
        }

    DisposableEffect(player) {
        val listener =
            object : Player.Listener {
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
        DropdownMenu(
            expanded = popupOpen.value,
            onDismissRequest = { popupOpen.value = false },
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Captions",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            for (language in languages.value) {
                val interactionSource = remember { MutableInteractionSource() }
                val isSelected =
                    language == player.trackSelectionParameters.preferredTextLanguages.firstOrNull()

                DropdownMenuItem(
                    text = {
                        Text(language ?: "None")
                    },
                    trailingIcon = {
                        RadioButton(
                            isSelected,
                            interactionSource = interactionSource,
                            onClick = {
                                popupOpen.value = false
                                player.trackSelectionParameters =
                                    player.trackSelectionParameters
                                        .buildUpon()
                                        .let {
                                            if (language == null) {
                                                it.setPreferredTextLanguages()
                                            } else {
                                                it.setPreferredTextLanguages(language)
                                            }
                                        }.build()
                            },
                        )
                    },
                    interactionSource = interactionSource,
                    onClick = {
                        popupOpen.value = false
                        player.trackSelectionParameters =
                            player.trackSelectionParameters
                                .buildUpon()
                                .let {
                                    if (language == null) {
                                        it.setPreferredTextLanguages()
                                    } else {
                                        it.setPreferredTextLanguages(language)
                                    }
                                }.build()
                    },
                )
            }
        }

        IconToggleButton(
            enabled = !playerState.loading && !playerState.casting,
            checked = popupOpen.value,
            onCheckedChange = { popupOpen.value = true },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_closed_caption),
                contentDescription = "Captions",
            )
        }
    }
}
