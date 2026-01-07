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
import de.justjanne.voctotv.mediacccde.api.VoctowebApi
import de.justjanne.voctotv.mediacccde.model.ConferenceModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
        val result =
            flow {
                emit(runCatching { api.conference.list().conferences }.getOrNull().orEmpty())
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        val allConferences =
            result
                .map {
                    it
                        .filter { it.eventLastReleasedAt != null }
                        .sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        val conferences: StateFlow<List<Map.Entry<ConferenceKind, List<ConferenceModel>>>> =
            result
                .map {
                    it
                        .filter { it.eventLastReleasedAt != null }
                        .groupBy { it.kind() }
                        .mapValues { it.value.sortedByDescending { it.eventLastReleasedAt?.toEpochSecond() ?: 0 } }
                        .entries
                        .filter { it.value.isNotEmpty() }
                        .sortedBy { it.key }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

        val recent =
            flow {
                emit(runCatching { api.lecture.listRecent().lectures }.getOrNull().orEmpty())
            }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        val featuredLectures =
            recent
                .map {
                    it.filter { it.promoted }.sortedByDescending { it.releaseDate }
                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }
