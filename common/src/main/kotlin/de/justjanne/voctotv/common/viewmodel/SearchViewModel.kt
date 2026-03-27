/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import de.justjanne.voctotv.common.paging.SearchPagingSource
import de.justjanne.voctotv.common.service.VoctowebSearchService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val searchService: VoctowebSearchService,
    ) : ViewModel() {
        val query = MutableStateFlow("")

        val results =
            query
                .debounce(300.milliseconds)
                .flatMapLatest { query ->
                    Pager(PagingConfig(pageSize = 20)) {
                        SearchPagingSource(searchService, query)
                    }.flow
                }.cachedIn(viewModelScope)
    }
