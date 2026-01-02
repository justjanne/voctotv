package de.justjanne.voctotv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.ccc.media.api.VoctowebApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = ConferenceViewModel.Factory::class)
class ConferenceViewModel @AssistedInject constructor(
    @Assisted conferenceId: String,
    api: VoctowebApi,
) : ViewModel() {
    val conference = flow {
        emit(runCatching { api.conference.get(conferenceId) }.getOrNull())
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
