/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun PlayerUi(
    viewModel: PlayerViewModel,
    contentPadding: PaddingValues,
    uiState: PlayerUiState,
    video: VideoModel?,
    back: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    if (uiState.isLandscapeUI && LocalActivity.current?.isInMultiWindowMode == true) {
        PlayerUiMinimalFullscreen(
            viewModel = viewModel,
            contentPadding = contentPadding,
            showFullscreenButton = false,
            uiState = uiState,
            video = video,
            content = content,
            back = back,
        )
    } else if (uiState.isLandscapeUI) {
        PlayerUiLandscapeFullscreen(
            viewModel = viewModel,
            contentPadding = contentPadding,
            uiState = uiState,
            video = video,
            content = content,
        )
    } else if (uiState.descriptionVisible.value) {
        PlayerUiPortrait(
            viewModel = viewModel,
            contentPadding = contentPadding,
            uiState = uiState,
            video = video,
            content = content,
            back = back,
        )
    } else {
        PlayerUiMinimalFullscreen(
            viewModel = viewModel,
            contentPadding = contentPadding,
            showFullscreenButton = true,
            uiState = uiState,
            video = video,
            content = content,
            back = back,
        )
    }
}
