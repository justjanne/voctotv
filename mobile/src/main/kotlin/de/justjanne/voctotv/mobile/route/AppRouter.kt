/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import de.justjanne.voctotv.common.viewmodel.ConferenceViewModel
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.common.viewmodel.LiveConferenceViewModel
import de.justjanne.voctotv.common.viewmodel.PlayerLiveViewModel
import de.justjanne.voctotv.common.viewmodel.PlayerVodViewModel
import de.justjanne.voctotv.common.viewmodel.SearchViewModel
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.route.conference.ConferenceRoute
import de.justjanne.voctotv.mobile.route.home.HomeRoute
import de.justjanne.voctotv.mobile.route.liveconference.LiveConferenceRoute
import de.justjanne.voctotv.mobile.route.player.PlayerRoute
import de.justjanne.voctotv.mobile.route.search.SearchRoute
import de.justjanne.voctotv.mobile.route.watchlater.WatchLaterRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRouter(startRoute: NavKey? = null) {
    val startPath = startRoute?.let { arrayOf(Routes.Home, startRoute) } ?: arrayOf(Routes.Home)
    val backStack = rememberNavBackStack(*startPath)

    val navigate: (NavKey) -> Unit =
        remember(backStack) {
            { backStack.add(it) }
        }
    val back: () -> Unit =
        remember(backStack) {
            { backStack.removeAt(backStack.lastIndex) }
        }

    LaunchedEffect(backStack.last()) {
        Log.i("AppRouter", backStack.last().toString())
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators =
            listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
        entryProvider =
            entryProvider {
                entry<Routes.Home> {
                    val viewModel = hiltViewModel<HomeViewModel>()
                    HomeRoute(viewModel, navigate)
                }
                entry<Routes.Conference> { key ->
                    val viewModel =
                        hiltViewModel<ConferenceViewModel, ConferenceViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    ConferenceRoute(viewModel, navigate, back)
                }
                entry<Routes.LiveConference> { key ->
                    val viewModel =
                        hiltViewModel<LiveConferenceViewModel, LiveConferenceViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    LiveConferenceRoute(viewModel, navigate, back)
                }
                entry<Routes.Search> {
                    val viewModel = hiltViewModel<SearchViewModel>()
                    SearchRoute(viewModel, navigate, back)
                }
                entry<Routes.WatchLater> {
                    val viewModel = hiltViewModel<WatchLaterViewModel>()
                    WatchLaterRoute(viewModel, navigate, back)
                }
                entry<Routes.PlayerVod> { key ->
                    val viewModel =
                        hiltViewModel<PlayerVodViewModel, PlayerVodViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    PlayerRoute(viewModel, back)
                }
                entry<Routes.PlayerLive> { key ->
                    val viewModel =
                        hiltViewModel<PlayerLiveViewModel, PlayerLiveViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    PlayerRoute(viewModel, back)
                }
            },
    )
}
