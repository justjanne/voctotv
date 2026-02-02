/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.voctoweb.api.VoctowebApi
import de.justjanne.voctotv.voctoweb.model.ConferenceModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        val api: VoctowebApi,
    ) : ViewModel() {
        val conferenceResult =
            flow {
                emit(
                    runCatching { api.conference.list().conferences }
                        .onFailure { it.printStackTrace() }
                        .getOrNull()
                        .orEmpty(),
                )
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        val recentResult =
            flow {
                emit(
                    runCatching { api.lecture.listRecent().lectures }
                        .onFailure { it.printStackTrace() }
                        .getOrNull()
                        .orEmpty(),
                )
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        val promotedResult =
            flow {
                emit(
                    runCatching { api.lecture.listPromoted().lectures }
                        .onFailure { it.printStackTrace() }
                        .getOrNull()
                        .orEmpty(),
                )
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        val allConferences =
            conferenceResult
                .map {
                    it
                        .filter { it.eventLastReleasedAt != null }
                        .sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        val conferences: StateFlow<Map<ConferenceKind, List<ConferenceModel>>> =
            conferenceResult
                .map {
                    it
                        .filter { it.eventLastReleasedAt != null }
                        .groupBy { it.kind() }
                        .mapValues { it.value.sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 } }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyMap())

        val featuredLectures =
            promotedResult
                .map { it.sortedByDescending { it.releaseDate } }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        val currentFilter = MutableStateFlow<ConferenceKind?>(null)

        val filteredItems: StateFlow<List<ConferenceModel>> =
            combine(currentFilter, allConferences, conferences) { filter, all, items ->
                if (filter == null) all else items[filter].orEmpty()
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }
