/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.tv.material3.IconButton
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.dropdown.DropdownMenuRadioItem
import de.justjanne.voctotv.tv.ui.dropdown.DropdownMenuSubheader

@OptIn(UnstableApi::class)
@Composable
fun CaptionSelection(
    player: Player,
    playerState: PlayerState,
) {
    Box {
        val popupOpen = remember { mutableStateOf(false) }

        PlayerPopup(popupOpen.value, { popupOpen.value = false }) {
            val currentLanguage = player.trackSelectionParameters.preferredTextLanguages.firstOrNull()

            Column(Modifier.padding(20.dp)) {
                DropdownMenuSubheader {
                    Text("Captions")
                }

                DropdownMenuRadioItem(
                    selected = currentLanguage == null,
                    text = {
                        Text("None")
                    },
                    onClick = {
                        popupOpen.value = false
                        player.trackSelectionParameters =
                            player.trackSelectionParameters
                                .buildUpon()
                                .setPreferredTextLanguages()
                                .build()
                    },
                )

                for (language in playerState.captionTracks) {
                    DropdownMenuRadioItem(
                        selected = currentLanguage == language,
                        text = {
                            Text(language)
                        },
                        onClick = {
                            popupOpen.value = false
                            player.trackSelectionParameters =
                                player.trackSelectionParameters
                                    .buildUpon()
                                    .setPreferredTextLanguages(language)
                                    .build()
                        },
                    )
                }
            }
        }

        IconButton(
            enabled = !playerState.loading,
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
