/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.liveconference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.common.viewmodel.LiveConferenceViewModel
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveConferenceRoute(
    viewModel: LiveConferenceViewModel,
    navigate: (NavKey) -> Unit,
    back: () -> Unit,
) {
    val conference = viewModel.conference.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(conference?.conference ?: stringResource(R.string.placeholder_conference)) },
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
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize(),
            ) {
                for (group in conference.groups) {
                    item("group-${group.group}") {
                        Text(
                            text = group.group,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(16.dp, 4.dp).heightIn(min = 32.dp),
                        )
                    }
                    items(group.rooms, key = { it.guid }) { item ->
                        LiveRoomItem(
                            item = item,
                            onClick = {
                                navigate(Routes.PlayerLive(item.guid))
                            },
                        )
                    }
                }
            }
        }
    }
}
