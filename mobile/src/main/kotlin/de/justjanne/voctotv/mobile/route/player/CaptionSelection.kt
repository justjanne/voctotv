/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.ui.dropdown.DropdownMenuRadioItem
import de.justjanne.voctotv.mobile.ui.dropdown.DropdownMenuSubheader

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

    Box {
        DropdownMenu(
            expanded = popupOpen.value,
            onDismissRequest = { popupOpen.value = false },
        ) {
            val currentLanguage = player.trackSelectionParameters.preferredTextLanguages.firstOrNull()

            DropdownMenuSubheader {
                Text(stringResource(R.string.player_captions))
            }

            DropdownMenuRadioItem(
                text = { Text(stringResource(R.string.player_captions_none)) },
                selected = currentLanguage == null,
                onClick = {
                    popupOpen.value = false
                    player.trackSelectionParameters = player.trackSelectionParameters
                        .buildUpon()
                        .setPreferredTextLanguages()
                        .build()
                },
            )

            for (language in playerState.captionTracks) {
                DropdownMenuRadioItem(
                    text = { Text(language) },
                    selected = currentLanguage == language,
                    onClick = {
                        popupOpen.value = false
                        player.trackSelectionParameters = player.trackSelectionParameters
                            .buildUpon()
                            .setPreferredTextLanguages(language)
                            .build()
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
                contentDescription = stringResource(R.string.player_captions),
            )
        }
    }
}
