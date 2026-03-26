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
import de.justjanne.voctotv.common.previews.TimedCue
import de.justjanne.voctotv.common.service.VoctowebLectureService
import de.justjanne.voctotv.common.util.buildMediaItem
import de.justjanne.voctotv.voctoweb.model.VideoModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@HiltViewModel(assistedFactory = PlayerVodViewModel.Factory::class)
class PlayerVodViewModel
@AssistedInject
constructor(
    @Assisted lectureId: String,
    lectureService: VoctowebLectureService,
    previewLoader: PreviewLoader,
    private val previewPreloader: PreviewPreloader,
    override val mediaSession: MediaSession,
) : ViewModel(), PlayerViewModel {
    override val video: StateFlow<VideoModel.Vod?> = flow { emit(lectureService.getLecture(lectureId)) }
        .map { it?.let { VideoModel.Vod(it) } }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    override val previews: StateFlow<List<TimedCue>> =
        video
            .map { it?.lecture?.let { previewLoader.load(it.thumbnailsUrl) }.orEmpty() }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    override val playerState = PlayerState(mediaSession.player)

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
            video.collectLatest { video ->
                mediaSession.player.clearMediaItems()

                if (video != null) {
                    val track =
                        video.lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 && it.highQuality }
                            ?: video.lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 }
                    if (track != null) {
                        mediaSession.player.apply {
                            trackSelectionParameters =
                                trackSelectionParameters
                                    .buildUpon()
                                    .setPreferredAudioLanguages(video.lecture.originalLanguage ?: "")
                                    .setPreferredTextLanguages()
                                    .build()
                            setMediaItem(buildMediaItem(video.lecture, track))
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
        fun create(lectureId: String): PlayerVodViewModel
    }
}
