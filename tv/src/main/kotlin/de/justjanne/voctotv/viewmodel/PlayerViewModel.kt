package de.justjanne.voctotv.viewmodel

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.ccc.media.api.VoctowebApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted lectureId: String,
    api: VoctowebApi,
) : ViewModel() {
    val lecture = flow {
        emit(runCatching { api.lecture.get(lectureId) }.getOrNull())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val mediaItem = lecture
        .map { lecture ->
            lecture?.let { lecture ->
                lecture.resources.orEmpty()
                    .firstOrNull { track -> track.mimeType == "video/mp4" && track.highQuality }
                    ?.let {
                        MediaItem.Builder()
                            .setUri(it.recordingUrl)
                            .setMediaId(it.filename)
                            .setSubtitleConfigurations(
                                listOf(
                                    MediaItem.SubtitleConfiguration.Builder(lecture.thumbnailsUrl.toUri())
                                        .setMimeType("text/vtt")
                                        .setRoleFlags(C.ROLE_FLAG_TRICK_PLAY)
                                        .setSelectionFlags(C.SELECTION_FLAG_FORCED)
                                        .build()
                                )
                                /*
                                lecture.resources.orEmpty()
                                    .filter { it.mimeType == "text/vtt" }
                                    .map {
                                        MediaItem.SubtitleConfiguration.Builder(it.recordingUrl.toUri())
                                            .setLanguage(it.language)
                                            .setMimeType(it.mimeType)
                                            .setRoleFlags(C.ROLE_FLAG_SUBTITLE)
                                            .setSelectionFlags(C.SELECTION_FLAG_DEFAULT)
                                            .build()
                                    }
                                 */
                            )
                            .build()
                    }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @AssistedFactory
    interface Factory {
        fun create(lectureId: String): PlayerViewModel
    }
}