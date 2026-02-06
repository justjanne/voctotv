/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.conference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.common.viewmodel.ConferenceViewModel
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.LectureItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConferenceRoute(
    viewModel: ConferenceViewModel,
    navigate: (NavKey) -> Unit,
    back: () -> Unit,
) {
    val conference = viewModel.conference.collectAsState().value
    val currentFilter = viewModel.currentFilter.collectAsState().value
    val filterOptions = viewModel.filterOptions.collectAsState().value
    val filteredItems = viewModel.filteredItems.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(conference?.title ?: stringResource(R.string.placeholder_conference)) },
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        if (conference == null) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                    Text(
                        text = stringResource(R.string.placeholder_loading),
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = LocalContentColor.current.copy(alpha = 0.6f),
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        } else {
            Column {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                    contentPadding =
                        PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = contentPadding.calculateTopPadding(),
                            bottom = 8.dp,
                        ),
                ) {
                    item(null) {
                        FilterChip(
                            selected = currentFilter == null,
                            onClick = { viewModel.currentFilter.value = null },
                            label = { Text(stringResource(R.string.filter_everything)) },
                        )
                    }
                    items(filterOptions) { filter ->
                        FilterChip(
                            selected = currentFilter == filter,
                            onClick = { viewModel.currentFilter.value = filter },
                            label = { Text(filter) },
                        )
                    }
                }

                val direction = LocalLayoutDirection.current
                LazyColumn(
                    contentPadding =
                        PaddingValues(
                            start = contentPadding.calculateStartPadding(direction),
                            end = contentPadding.calculateEndPadding(direction),
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(filteredItems, key = { it.guid }) { item ->
                        LectureItem(
                            item = item,
                            onClick = {
                                navigate(Routes.Player(item.guid))
                            },
                        )
                    }
                }
            }
        }
    }
}
