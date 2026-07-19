/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.viewmodel.ConferenceKind
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.LectureCard
import de.justjanne.voctotv.tv.ui.theme.GridGutter
import de.justjanne.voctotv.tv.ui.theme.GridPadding

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val watchLaterViewModel = hiltViewModel<WatchLaterViewModel>()
    val conferences by viewModel.conferences.collectAsState()
    val recent by viewModel.recentResult.collectAsState()
    val popular by viewModel.popularResult.collectAsState()
    val featuredItems by viewModel.featuredItems.collectAsState()
    val watchLaterIds by watchLaterViewModel.itemIds.collectAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        item("header") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = GridGutter, end = GridGutter, top = 32.dp, bottom = GridPadding),
            ) {
                Image(
                    painterResource(R.drawable.ic_mediacccde),
                    contentDescription = null,
                    modifier = Modifier.height(32.dp),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                )
                IconButton(
                    onClick = { navigate(Routes.WatchLater) },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_star),
                        contentDescription = stringResource(R.string.action_watch_later),
                    )
                }
            }
        }

        if (featuredItems.isNotEmpty()) {
            item("featured") {
                FeaturedCarousel(
                    items = featuredItems,
                    navigate = navigate,
                    isLectureSaved = { guid -> watchLaterIds.contains(guid) },
                    onLectureLongClick = { lecture -> watchLaterViewModel.toggle(lecture) },
                    modifier = Modifier.focusRequester(focusRequester),
                )
            }
        }

        if (recent.isNotEmpty()) {
            item("recent-lectures") {
                Text(
                    stringResource(R.string.category_recent),
                    modifier = Modifier.padding(horizontal = GridGutter),
                )

                val focusRequester = remember("recent") { FocusRequester() }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(GridPadding),
                    contentPadding = PaddingValues(vertical = GridPadding, horizontal = GridGutter),
                    modifier = Modifier.focusRestorer(focusRequester),
                ) {
                    itemsIndexed(recent, key = { _, lecture -> lecture.guid }) { index, lecture ->
                        LectureCard(
                            lecture,
                            navigate,
                            if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                            onLongClick = { watchLaterViewModel.toggle(lecture) },
                            isSaved = watchLaterIds.contains(lecture.guid),
                        )
                    }
                }
            }
        }

        if (popular.isNotEmpty()) {
            item("popular-lectures") {
                Text(
                    stringResource(R.string.category_popular),
                    modifier = Modifier.padding(horizontal = GridGutter),
                )

                val focusRequester = remember("popular") { FocusRequester() }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(GridPadding),
                    contentPadding = PaddingValues(vertical = GridPadding, horizontal = GridGutter),
                    modifier = Modifier.focusRestorer(focusRequester),
                ) {
                    itemsIndexed(popular, key = { _, lecture -> lecture.guid }) { index, lecture ->
                        LectureCard(
                            lecture,
                            navigate,
                            if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                            onLongClick = { watchLaterViewModel.toggle(lecture) },
                            isSaved = watchLaterIds.contains(lecture.guid),
                        )
                    }
                }
            }
        }

        for (kind in ConferenceKind.entries) {
            val lectures = conferences[kind].orEmpty()
            if (lectures.isNotEmpty()) {
                item(kind) {
                    ConferenceRow(
                        when (kind) {
                            ConferenceKind.CONGRESS -> stringResource(R.string.category_congress)
                            ConferenceKind.GPN -> stringResource(R.string.category_gpn)
                            ConferenceKind.CONFERENCE -> stringResource(R.string.category_conference)
                            ConferenceKind.DOCUMENTARIES -> stringResource(R.string.category_documentary)
                            ConferenceKind.ERFA -> stringResource(R.string.category_erfa)
                            ConferenceKind.OTHER -> stringResource(R.string.category_other)
                        },
                        lectures,
                        navigate,
                    )
                }
            }
        }
    }
}
