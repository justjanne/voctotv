/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.common.viewmodel.ConferenceKind
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.common.viewmodel.kind
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarousel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val watchLaterViewModel = hiltViewModel<WatchLaterViewModel>()
    val currentFilter = viewModel.currentFilter.collectAsState().value
    val filteredItems = viewModel.filteredItems.collectAsState().value
    val featuredItems = viewModel.featuredItems.collectAsState().value
    val watchLaterIds = watchLaterViewModel.itemIds.collectAsState().value

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
                actions = {
                    IconButton(
                        onClick = { navigate(Routes.Search) },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_search),
                            contentDescription = stringResource(R.string.action_search),
                        )
                    }
                    IconButton(
                        onClick = { navigate(Routes.WatchLater) },
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_star),
                            contentDescription = stringResource(R.string.action_watch_later),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        val verticalPadding =
            PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding(),
            )
        val layoutDirection = LocalLayoutDirection.current
        val horizontalPadding =
            PaddingValues(
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection),
            )

        LazyColumn(
            contentPadding = horizontalPadding,
            modifier = Modifier.fillMaxSize().padding(verticalPadding),
        ) {
            if (featuredItems.isNotEmpty()) {
                item("featured") {
                    HeroCarousel(count = featuredItems.size) { index ->
                        when (val item = featuredItems[index]) {
                            is FeaturedItem.Lecture ->
                                HeroCarouselLecture(
                                    item = item,
                                    navigate = navigate,
                                    isSaved = watchLaterIds.contains(item.lecture.guid),
                                    onLongClick = { watchLaterViewModel.toggle(item.lecture) },
                                )
                            is FeaturedItem.Stream -> HeroCarouselStream(item, navigate)
                        }
                    }
                }
            }

            stickyHeader("filter") {
                Surface {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                        contentPadding = PaddingValues(16.dp, bottom = 8.dp),
                    ) {
                        item(null) {
                            FilterChip(
                                selected = currentFilter == null,
                                onClick = { viewModel.currentFilter.value = null },
                                label = { Text(stringResource(R.string.filter_everything)) },
                            )
                        }
                        items(ConferenceKind.entries) { filter ->
                            FilterChip(
                                selected = currentFilter == filter,
                                onClick = { viewModel.currentFilter.value = filter },
                                label = {
                                    Text(
                                        when (filter) {
                                            ConferenceKind.CONGRESS -> stringResource(R.string.category_congress)
                                            ConferenceKind.GPN -> stringResource(R.string.category_gpn)
                                            ConferenceKind.CONFERENCE -> stringResource(R.string.category_conference)
                                            ConferenceKind.DOCUMENTARIES -> stringResource(R.string.category_documentary)
                                            ConferenceKind.ERFA -> stringResource(R.string.category_erfa)
                                            ConferenceKind.OTHER -> stringResource(R.string.category_other)
                                        },
                                    )
                                },
                            )
                        }
                    }
                }
            }

            items(filteredItems, key = { "conference-${it.acronym}" }) { item ->
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
