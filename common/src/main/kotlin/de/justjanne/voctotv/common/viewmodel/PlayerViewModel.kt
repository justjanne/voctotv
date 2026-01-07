package de.justjanne.voctotv.common.viewmodel

import android.annotation.SuppressLint
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.session.MediaSession
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.previews.PreviewLoader
import de.justjanne.voctotv.common.previews.PreviewPreloader
import de.justjanne.voctotv.mediacccde.api.VoctowebApi
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

        val mediaItem =
            lecture
                .map { lecture ->
                    lecture?.let { lecture ->
                        val track =
                            lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 && it.highQuality }
                                ?: lecture.resources?.firstOrNull { it.mimeType == MimeTypes.VIDEO_MP4 }
                        track?.let {
                            MediaItem
                                .Builder()
                                .setUri(it.recordingUrl)
                                .setMediaId(it.filename)
                                .setSubtitleConfigurations(
                                    buildList {
                                        lecture.resources?.filter { it.mimeType == MimeTypes.TEXT_VTT }?.forEach {
                                            add(
                                                MediaItem.SubtitleConfiguration
                                                    .Builder(
                                                        it.recordingUrl
                                                            .replace(
                                                                "https://cdn.media.ccc.de/",
                                                                "https://static.media.ccc.de/media/",
                                                            ).toUri(),
                                                    ).setMimeType(it.mimeType)
                                                    .setRoleFlags(C.ROLE_FLAG_CAPTION)
                                                    .setLabel(it.language)
                                                    .setLanguage(it.language)
                                                    .setSelectionFlags(0)
                                                    .build(),
                                            )
                                        }
                                        lecture.resources?.filter { it.mimeType == MimeTypes.APPLICATION_SUBRIP }?.forEach {
                                            add(
                                                MediaItem.SubtitleConfiguration
                                                    .Builder(it.recordingUrl.toUri())
                                                    .setMimeType(it.mimeType)
                                                    .setRoleFlags(C.ROLE_FLAG_CAPTION)
                                                    .setLabel(it.language)
                                                    .setLanguage(it.language)
                                                    .setSelectionFlags(0)
                                                    .build(),
                                            )
                                        }
                                    },
                                ).build()
                        }
                    }
                }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

        init {
            viewModelScope.launch {
                previews.collectLatest {
                    previewPreloader.preload(it)
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
