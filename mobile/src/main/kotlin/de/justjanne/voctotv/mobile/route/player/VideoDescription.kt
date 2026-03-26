/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import de.justjanne.voctotv.mobile.ui.ModalSideSheetDefaults
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun VideoDescription(
    video: VideoModel,
    modifier: Modifier = Modifier,
    onClose: (() -> Unit)? = null,
) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(video) {
        focusRequester.requestFocus()
    }

    Column(
        modifier
            .verticalScroll(scrollState)
            .padding(ModalSideSheetDefaults.Padding)
            .focusRequester(focusRequester)
            .onPreviewKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    when (it.key) {
                        Key.MediaFastForward, Key.MediaStepForward, Key.MediaSkipForward,
                        Key.MediaRewind, Key.MediaStepBackward, Key.MediaSkipBackward,
                            -> {
                            true
                        }

                        else -> false
                    }
                } else {
                    false
                }
            },
    ) {
        when (video) {
            is VideoModel.Vod -> VideoDescriptionVod(video, onClose)
            is VideoModel.Live -> VideoDescriptionLive(video, onClose)
        }
    }
}
