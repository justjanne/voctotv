/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MimeTypes
import androidx.media3.session.MediaSession
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.previews.PreviewLoader
import de.justjanne.voctotv.common.previews.PreviewPreloader
import de.justjanne.voctotv.common.util.buildMediaItem
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel
@AssistedInject
constructor(
    @Assisted lectureId: String,
    api: VoctowebApi,
    previewLoader: PreviewLoader,
    private val previewPreloader: PreviewPreloader,
    val mediaSession: MediaSession,
) : ViewModel() {
    val lecture =
        flow {
            emit(runCatching { api.lecture.get(lectureId) }.getOrNull())
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val previews =
        lecture
            .map { it?.let { previewLoader.load(it.thumbnailsUrl) }.orEmpty() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val playerState = PlayerState(mediaSession.player)

    init {
        viewModelScope.launch {
            previews.collectLatest {
                previewPreloader.preload(it)
            }
        }
        viewModelScope.launch {
            playerState.observe()
        }
        viewModelScope.launch {
            lecture.collectLatest { lecture ->
                mediaSession.player.clearMediaItems()

                if (lecture != null) {
                    val track =
                        lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 && it.highQuality }
                            ?: lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 }
                    if (track != null) {
                        mediaSession.player.apply {
                            trackSelectionParameters =
                                trackSelectionParameters
                                    .buildUpon()
                                    .setPreferredAudioLanguages(lecture.originalLanguage ?: "")
                                    .setPreferredTextLanguages()
                                    .build()
                            setMediaItem(buildMediaItem(lecture, track))
                            prepare()
                            playWhenReady = true
                            play()
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        mediaSession.player.release()
        mediaSession.release()
    }

    @AssistedFactory
    interface Factory {
        fun create(lectureId: String): PlayerViewModel
    }
}
