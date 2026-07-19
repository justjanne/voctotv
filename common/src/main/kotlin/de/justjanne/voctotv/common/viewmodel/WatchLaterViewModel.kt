package de.justjanne.voctotv.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.watchlater.WatchLaterItem
import de.justjanne.voctotv.common.watchlater.WatchLaterRepository
import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WatchLaterViewModel
    @Inject
    constructor(
        private val repository: WatchLaterRepository,
    ) : ViewModel() {
        val items: StateFlow<List<WatchLaterItem>> =
            repository.items
                .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        val itemIds: StateFlow<Set<String>> =
            items
                .map { it.map { item -> item.guid }.toSet() }
                .stateIn(viewModelScope, SharingStarted.Eagerly, emptySet())

        fun remove(guid: String) {
            repository.remove(guid)
        }

        fun toggle(lecture: LectureModel) {
            repository.toggle(lecture)
        }
    }
