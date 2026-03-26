package de.justjanne.voctotv.tv.route.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.tv.ui.ModalSideSheet
import de.justjanne.voctotv.tv.ui.ModalSideSheetDefaults
import de.justjanne.voctotv.tv.ui.scrollable
import de.justjanne.voctotv.tv.ui.scrollbar
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun InfoSideSheet(video: VideoModel) {
    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(video) {
        focusRequester.requestFocus()
    }

    ModalSideSheet {
        Column(
            Modifier
                .scrollbar(scrollState, 16.dp)
                .scrollable(scrollState)
                .padding(ModalSideSheetDefaults.Padding)
                .padding(end = 16.dp)
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
                is VideoModel.Live -> VideoDescriptionLive(video)
                is VideoModel.Vod -> VideoDescriptionVod(video)
            }
        }
    }
}
