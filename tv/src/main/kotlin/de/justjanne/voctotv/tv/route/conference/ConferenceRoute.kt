/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.conference

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.viewmodel.ConferenceViewModel
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.LectureCard
import de.justjanne.voctotv.tv.ui.theme.GridGutter
import de.justjanne.voctotv.tv.ui.theme.GridPadding
import de.justjanne.voctotv.tv.ui.theme.textShadow

@Composable
fun ConferenceRoute(
    viewModel: ConferenceViewModel,
    navigate: (NavKey) -> Unit,
) {
    val conference by viewModel.conference.collectAsState()
    val popular by viewModel.popular.collectAsState()
    val recent by viewModel.recent.collectAsState()
    val itemsByTrack by viewModel.itemsByTrack.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        conference?.let { conference ->
            item("header") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = GridGutter, end = GridGutter, top = 32.dp, bottom = GridPadding),
                ) {
                    Text(
                        text = conference.title,
                        style =
                            MaterialTheme.typography.titleLarge.copy(
                                shadow = MaterialTheme.colorScheme.textShadow,
                            ),
                        maxLines = 2,
                    )
                }
            }
        }

        item("recent") {
            Text(
                stringResource(R.string.category_recent),
                modifier = Modifier.padding(horizontal = GridGutter),
            )

            val focusRequester = remember("recent") { FocusRequester() }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(GridPadding),
                contentPadding = PaddingValues(horizontal = GridGutter, vertical = GridPadding),
                modifier = Modifier.focusRestorer(focusRequester),
            ) {
                itemsIndexed(recent, key = { _, lecture -> lecture.guid }) { index, lecture ->
                    LectureCard(
                        lecture,
                        navigate,
                        if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    )
                }
            }
        }

        item("popular") {
            Text(
                stringResource(R.string.category_popular),
                modifier = Modifier.padding(horizontal = GridGutter),
            )

            val focusRequester = remember("popular") { FocusRequester() }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(GridPadding),
                contentPadding = PaddingValues(horizontal = GridGutter, vertical = GridPadding),
                modifier = Modifier.focusRestorer(focusRequester),
            ) {
                itemsIndexed(popular, key = { _, lecture -> lecture.guid }) { index, lecture ->
                    LectureCard(
                        lecture,
                        navigate,
                        if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    )
                }
            }
        }

        items(itemsByTrack.entries.sortedBy { it.key }.toList(), key = { it.key }) { (track, items) ->
            Text(
                track,
                modifier = Modifier.padding(horizontal = GridGutter),
            )

            val focusRequester = remember(track) { FocusRequester() }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(GridPadding),
                contentPadding = PaddingValues(horizontal = GridGutter, vertical = GridPadding),
                modifier = Modifier.focusRestorer(focusRequester),
            ) {
                itemsIndexed(items, key = { _, lecture -> lecture.guid }) { index, lecture ->
                    LectureCard(
                        lecture,
                        navigate,
                        if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                    )
                }
            }
        }
    }
}
