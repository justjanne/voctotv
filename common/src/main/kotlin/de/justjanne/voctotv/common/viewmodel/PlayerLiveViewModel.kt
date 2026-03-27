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
import androidx.media3.session.MediaSession
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.common.previews.TimedCue
import de.justjanne.voctotv.common.service.VoctowebLiveService
import de.justjanne.voctotv.common.util.buildMediaItem
import de.justjanne.voctotv.voctoweb.model.VideoModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@SuppressLint("UnsafeOptInUsageError")
@HiltViewModel(assistedFactory = PlayerLiveViewModel.Factory::class)
class PlayerLiveViewModel
    @AssistedInject
    constructor(
        @Assisted roomId: String,
        liveService: VoctowebLiveService,
        override val mediaSession: MediaSession,
    ) : ViewModel(),
        PlayerViewModel {
        override val video: StateFlow<VideoModel.Live?> =
            flow { emit(liveService.getRoom(roomId)) }
                .stateIn(viewModelScope, SharingStarted.Eagerly, null)

        override val previews: StateFlow<List<TimedCue>> = MutableStateFlow(emptyList())

        override val playerState = PlayerState(mediaSession.player)

        init {
            viewModelScope.launch {
                playerState.observe()
            }
            viewModelScope.launch {
                video.collectLatest { video ->
                    mediaSession.player.clearMediaItems()

                    if (video != null) {
                        val stream = video.room.streams.firstOrNull { it.videoSize != null }
                        val track = stream?.urls["hls"]
                        if (track != null) {
                            mediaSession.player.apply {
                                trackSelectionParameters =
                                    trackSelectionParameters.buildUpon().build()
                                val mediaItem = buildMediaItem(video.conference, video.room, stream, track)
                                setMediaItem(mediaItem)
                                println("Setting media item: $track $mediaItem")
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
            fun create(roomId: String): PlayerLiveViewModel
        }
    }
