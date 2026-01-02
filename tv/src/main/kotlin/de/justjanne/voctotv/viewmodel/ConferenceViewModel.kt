package de.justjanne.voctotv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.ccc.media.api.LectureModel
import de.justjanne.voctotv.DI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.component3

@HiltViewModel(assistedFactory = ConferenceViewModel.Factory::class)
class ConferenceViewModel @AssistedInject constructor(
    @Assisted conferenceId: String
) : ViewModel() {
    val conference = flow {
        emit(DI.api.conference.get(conferenceId))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val featured = conference.map { conference ->
        conference?.lectures?.filter { it.promoted }?.sortedByDescending { it.releaseDate }.orEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val itemsByTrack = conference.map { conference ->
        buildMap {
            conference?.lectures?.forEach { lecture ->
                val tags = lecture.tags
                    .filter { !it.all(Char::isDigit) }
                    .filter { !it.startsWith("${conference.acronym}-") }
                    .filter { it != conference.acronym }
                for (tag in tags) {
                    getOrPut(tag, ::mutableListOf).add(lecture)
                }
                if (tags.isEmpty()) {
                    getOrPut("Other", ::mutableListOf).add(lecture)
                }
            }
            for (value in values) {
                value.sortByDescending { it.releaseDate }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

    @AssistedFactory
    interface Factory {
        fun create(conferenceId: String): ConferenceViewModel
    }
}
