package de.justjanne.voctotv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ccc.media.api.ConferenceModel
import de.justjanne.voctotv.DI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel : ViewModel() {
    val conferences = flow {
        emit(DI.api.conference.list().conferences)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val featured = conferences.map {
        it.filter { it.eventLastReleasedAt != null}
            .filter { it.slug.startsWith("congress/") || it.slug.startsWith("conferences/") }
            .groupBy { it.slug.substringBeforeLast('/', missingDelimiterValue = "") }
            .map { it.value.maxBy { it.eventLastReleasedAt?.toEpochSecond() ?: 0 } }
            .sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val recent = flow {
        emit(DI.api.lecture.listRecent().lectures)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
}