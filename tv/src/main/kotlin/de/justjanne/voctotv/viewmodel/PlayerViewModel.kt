package de.justjanne.voctotv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.DI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = PlayerViewModel.Factory::class)
class PlayerViewModel @AssistedInject constructor(
    @Assisted lectureId: String
) : ViewModel() {
    val lecture = flow {
        emit(DI.api.lecture.get(lectureId))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val mediaItem = lecture
        .map { it?.resources?.first { track -> track.mimeType == "video/mp4" && track.highQuality} }
        .map { it?.let { MediaItem.Builder().setUri(it.recordingUrl).setMediaId(it.filename).build() } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    @AssistedFactory
    interface Factory {
        fun create(lectureId: String): PlayerViewModel
    }
}