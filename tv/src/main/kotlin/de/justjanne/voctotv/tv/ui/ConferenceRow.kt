/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.mediacccde.model.ConferenceModel
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.theme.VoctoTvTheme

@Composable
fun ConferenceRow(
    title: String,
    featured: List<ConferenceModel>,
    navigate: (NavKey) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Text(title, modifier = Modifier.padding(horizontal = 20.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.focusRestorer(focusRequester),
    ) {
        itemsIndexed(featured, key = { _, item -> item.acronym }) { index, conference ->
            val modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier

            Column {
                StandardCardContainer(
                    modifier = modifier.width(196.dp),
                    imageCard = { interactionSource ->
                        VoctoTvTheme(isInDarkTheme = false) {
                            Card(
                                onClick = { navigate(Routes.Conference(conference.acronym)) },
                                modifier =
                                    Modifier
                                        .aspectRatio(16f / 9),
                                interactionSource = interactionSource,
                            ) {
                                AsyncImage(
                                    model = conference.logoUrl,
                                    contentDescription = conference.title,
                                    modifier =
                                        Modifier
                                            .fillMaxSize()
                                            .padding(8.dp),
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = conference.title,
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
            }
        }
    }
}
