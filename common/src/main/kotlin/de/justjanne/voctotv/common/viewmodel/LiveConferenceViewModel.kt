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
import de.justjanne.voctotv.common.service.VoctowebLiveService
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

@HiltViewModel(assistedFactory = LiveConferenceViewModel.Factory::class)
class LiveConferenceViewModel
    @AssistedInject
    constructor(
        @Assisted val conferenceId: String,
        conferenceService: VoctowebLiveService,
    ) : ViewModel() {
        val conference =
            flow { emit(conferenceService.getConference(conferenceId)) }
                .stateIn(viewModelScope, SharingStarted.Eagerly, null)

        @AssistedFactory
        interface Factory {
            fun create(conferenceId: String): LiveConferenceViewModel
        }
    }
