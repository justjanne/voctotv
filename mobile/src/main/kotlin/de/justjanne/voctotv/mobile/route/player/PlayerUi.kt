/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun PlayerUi(
    viewModel: PlayerViewModel,
    contentPadding: PaddingValues,
    uiState: PlayerUiState,
    lecture: LectureModel?,
    back: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    if (uiState.isLandscapeUI) {
        PlayerUiLandscapeFullscreen(
            viewModel = viewModel,
            contentPadding = contentPadding,
            uiState = uiState,
            lecture = lecture,
            content = content,
        )
    } else if (uiState.descriptionVisible.value) {
        PlayerUiPortrait(
            viewModel = viewModel,
            contentPadding = contentPadding,
            uiState = uiState,
            lecture = lecture,
            content = content,
            back = back,
        )
    } else {
        PlayerUiPortraitFullscreen(
            viewModel = viewModel,
            contentPadding = contentPadding,
            uiState = uiState,
            lecture = lecture,
            content = content,
        )
    }
}
