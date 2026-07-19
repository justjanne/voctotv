/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route

import androidx.compose.runtime.Composable
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
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.route.conference.ConferenceRoute
import de.justjanne.voctotv.tv.route.home.HomeRoute
import de.justjanne.voctotv.tv.route.liveconference.LiveConferenceRoute
import de.justjanne.voctotv.tv.route.player.PlayerRoute
import de.justjanne.voctotv.tv.route.watchlater.WatchLaterRoute

@Composable
fun AppRouter() {
    val backStack: MutableList<NavKey> = rememberNavBackStack(Routes.Home)
    val navigate: (NavKey) -> Unit =
        remember(backStack) {
            { backStack.add(it) }
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

                    ConferenceRoute(viewModel, navigate)
                }
                entry<Routes.LiveConference> { key ->
                    val viewModel =
                        hiltViewModel<LiveConferenceViewModel, LiveConferenceViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }

                    LiveConferenceRoute(viewModel, navigate)
                }
                entry<Routes.WatchLater> {
                    val viewModel = hiltViewModel<WatchLaterViewModel>()
                    WatchLaterRoute(viewModel, navigate)
                }
                entry<Routes.PlayerVod> { key ->
                    val viewModel =
                        hiltViewModel<PlayerVodViewModel, PlayerVodViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    PlayerRoute(viewModel)
                }
                entry<Routes.PlayerLive> { key ->
                    val viewModel =
                        hiltViewModel<PlayerLiveViewModel, PlayerLiveViewModel.Factory> { factory ->
                            factory.create(key.id)
                        }
                    PlayerRoute(viewModel)
                }
            },
    )
}
