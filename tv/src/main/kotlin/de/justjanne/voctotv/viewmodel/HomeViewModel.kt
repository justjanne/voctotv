package de.justjanne.voctotv.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.DI
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val result = flow {
        emit(DI.api.conference.list().conferences)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val conferences = result.map {
        it.filter { it.eventLastReleasedAt != null}
            .filter { it.slug.startsWith("congress/") || it.slug.startsWith("conferences/") }
            .sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val erfas = result.map {
        it.filter { it.slug.startsWith("erfas/") }
            .sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val recent = flow {
        emit(DI.api.lecture.listRecent().lectures)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val featuredLectures = recent.map {
        it.filter { it.promoted }.sortedByDescending { it.releaseDate }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}
