/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import de.justjanne.voctotv.common.viewmodel.SearchViewModel
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.LectureItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchRoute(
    viewModel: SearchViewModel,
    navigate: (NavKey) -> Unit,
    back: () -> Unit,
) {
    val watchLaterViewModel = hiltViewModel<WatchLaterViewModel>()
    val query by viewModel.query.collectAsState()
    val results = viewModel.results.collectAsLazyPagingItems()
    val watchLaterIds = watchLaterViewModel.itemIds.collectAsState().value

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SearchLayout(
        searchField = {
            SearchField(
                query = query,
                onChange = { viewModel.query.value = it },
                onBack = back,
                modifier = Modifier.focusRequester(focusRequester),
            )
        },
    ) { contentPadding ->
        if (results.itemCount == 0) {
            EmptyState(
                modifier = Modifier.padding(contentPadding),
                query = query,
            )
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize(),
            ) {
                items(
                    count = results.itemCount,
                    key = results.itemKey { it.guid },
                ) { index ->
                    val item = results[index]
                    if (item != null) {
                        LectureItem(
                            item = item,
                            isSaved = watchLaterIds.contains(item.guid),
                            onClick = { navigate(Routes.PlayerVod(item.guid)) },
                            onLongClick = { watchLaterViewModel.toggle(item) },
                        )
                    }
                }
            }
        }
    }
}
