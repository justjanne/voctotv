/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui.player

import androidx.annotation.OptIn
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.voctoweb.model.LectureModel

@OptIn(UnstableApi::class)
@Composable
fun AudioSelection(
    player: Player,
    playerState: PlayerState,
    lecture: LectureModel?,
) {
    val popupOpen = remember { mutableStateOf(false) }

    LaunchedEffect(playerState.casting) {
        if (playerState.casting) {
            popupOpen.value = false
        }
    }

    Box(Modifier.wrapContentSize()) {
        DropdownMenu(
            expanded = popupOpen.value,
            onDismissRequest = { popupOpen.value = false },
            tonalElevation = 16.dp,
        ) {
            val audioTracks =
                player.currentTracks.groups
                    .filter { it.type == C.TRACK_TYPE_AUDIO }

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Language",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }

            for (audioTrack in audioTracks) {
                audioTrack.getTrackFormat(0).language?.let { language ->
                    val interactionSource = remember { MutableInteractionSource() }

                    DropdownMenuItem(
                        text = {
                            if (lecture?.originalLanguage?.startsWith(language) == true) {
                                Text("$language (Original)")
                            } else {
                                Text(language)
                            }
                        },
                        trailingIcon = {
                            RadioButton(
                                audioTrack.isSelected,
                                interactionSource = interactionSource,
                                onClick = {
                                    popupOpen.value = false
                                    player.trackSelectionParameters =
                                        player.trackSelectionParameters
                                            .buildUpon()
                                            .setPreferredAudioLanguages(language)
                                            .build()
                                },
                            )
                        },
                        interactionSource = interactionSource,
                        onClick = {
                            popupOpen.value = false
                            player.trackSelectionParameters =
                                player.trackSelectionParameters
                                    .buildUpon()
                                    .setPreferredAudioLanguages(language)
                                    .build()
                        },
                    )
                }
            }
        }

        IconToggleButton(
            enabled = !playerState.loading && !playerState.casting,
            checked = popupOpen.value,
            onCheckedChange = { popupOpen.value = true },
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_translate),
                contentDescription = "Language",
            )
        }
    }
}
