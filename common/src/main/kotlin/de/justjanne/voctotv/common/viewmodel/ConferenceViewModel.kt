/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.service.VoctowebConferenceService
import de.justjanne.voctotv.common.util.calculateTracks
import de.justjanne.voctotv.voctoweb.model.LectureModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = ConferenceViewModel.Factory::class)
class ConferenceViewModel
@AssistedInject
constructor(
    @Assisted val conferenceId: String,
    conferenceService: VoctowebConferenceService,
) : ViewModel() {
    val conference =
        flow { emit(conferenceService.getConference(conferenceId)) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val popular =
        conference
            .map { conference ->
                conference
                    ?.lectures
                    ?.filter { it.viewCount > 1 }
                    ?.sortedByDescending { it.viewCount }
                    .orEmpty()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val recent =
        conference
            .map { conference ->
                conference
                    ?.lectures
                    ?.sortedByDescending { it.releaseDate }
                    .orEmpty()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val allItems: StateFlow<List<LectureModel>> =
        conference
            .map { conference ->
                conference?.lectures?.sortedByDescending { it.releaseDate }.orEmpty()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val itemsByTrack: StateFlow<Map<String, List<LectureModel>>> =
        conference
            .map { conference ->
                buildMap {
                    conference?.lectures?.forEach { lecture ->
                        val tracks = calculateTracks(conference, lecture)
                        for (track in tracks) {
                            getOrPut(track, ::mutableListOf).add(lecture)
                        }
                    }
                    for (value in values) {
                        value.sortByDescending { it.releaseDate }
                    }
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

    val currentFilter = MutableStateFlow<String?>(null)

    val filterOptions =
        itemsByTrack
            .map { items ->
                items.keys.toList().sorted()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val filteredItems: StateFlow<List<LectureModel>> =
        combine(currentFilter, allItems, itemsByTrack) { filter, all, items ->
            if (filter == null) all else items[filter].orEmpty()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    @AssistedFactory
    interface Factory {
        fun create(conferenceId: String): ConferenceViewModel
    }
}
