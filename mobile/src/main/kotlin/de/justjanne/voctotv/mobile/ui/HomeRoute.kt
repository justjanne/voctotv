/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.common.viewmodel.ConferenceKind
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.common.viewmodel.kind
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val currentFilter = viewModel.currentFilter.collectAsState().value
    val filteredItems = viewModel.filteredItems.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painterResource(R.drawable.ic_mediacccde),
                        contentDescription = "media.ccc.de",
                        modifier = Modifier.height(with(LocalDensity.current) { LocalTextStyle.current.lineHeight.toDp() }),
                    )
                },
            )
        },
    ) { contentPadding ->
        Column {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                contentPadding = PaddingValues(
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
                        label = { Text("Everything") },
                    )
                }
                items(ConferenceKind.entries) { filter ->
                    FilterChip(
                        selected = currentFilter == filter,
                        onClick = { viewModel.currentFilter.value = filter },
                        label = {
                            Text(
                                when (filter) {
                                    ConferenceKind.CONGRESS -> "Congress"
                                    ConferenceKind.GPN -> "GPN"
                                    ConferenceKind.CONFERENCE -> "Conferences"
                                    ConferenceKind.DOCUMENTARIES -> "Documentaries"
                                    ConferenceKind.ERFA -> "Erfas"
                                    ConferenceKind.OTHER -> "Other"
                                },
                            )
                        },
                    )
                }
            }

            val direction = LocalLayoutDirection.current
            LazyColumn(
                contentPadding = PaddingValues(
                    start = contentPadding.calculateStartPadding(direction),
                    end = contentPadding.calculateEndPadding(direction),
                    bottom = contentPadding.calculateBottomPadding()
                ),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(filteredItems, key = { it.acronym }) { item ->
                    ConferenceItem(
                        item = item,
                        showYear = item.kind() != ConferenceKind.ERFA,
                        onClick = {
                            navigate(Routes.Conference(item.acronym))
                        },
                    )
                }
            }
        }
    }
}
