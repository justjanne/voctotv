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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.ConferenceKind
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.common.viewmodel.kind
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.Routes.Player
import de.justjanne.voctotv.mobile.ui.WatchNowButton
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarousel
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarouselContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val currentFilter = viewModel.currentFilter.collectAsState().value
    val filteredItems = viewModel.filteredItems.collectAsState().value
    val featuredLectures = viewModel.featuredLectures.collectAsState().value

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
            if (featuredLectures.isNotEmpty()) {
                item("featured") {
                    HeroCarousel(count = featuredLectures.size) { index ->
                        val lecture = featuredLectures[index]
                        val painter = rememberAsyncImagePainter(lecture.posterUrl)

                        HeroCarouselContent(
                            background = painter,
                            title = {
                                Text(
                                    lecture.title,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 2,
                                )
                            },
                            description = {
                                Text(
                                    lecture.persons.fastJoinToString(" · "),
                                    maxLines = 1,
                                )
                            },
                            action = {
                                WatchNowButton(onClick = {
                                    navigate(Player(lecture.guid))
                                })
                            },
                        )
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
