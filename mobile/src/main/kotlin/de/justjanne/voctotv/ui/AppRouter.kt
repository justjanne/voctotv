package de.justjanne.voctotv.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import de.justjanne.voctotv.Routes
import de.justjanne.voctotv.viewmodel.ConferenceViewModel
import de.justjanne.voctotv.viewmodel.HomeViewModel
import de.justjanne.voctotv.viewmodel.PlayerViewModel

@Composable
fun AppRouter() {
    val backStack: MutableList<NavKey> = rememberNavBackStack(Routes.Home)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Routes.Home> {
                val viewModel = hiltViewModel<HomeViewModel>()

                HomeRoute(
                    viewModel,
                    openConference = {
                        backStack.add(Routes.Conference(it))
                    },
                    openLecture = {
                        backStack.add(Routes.Lecture(it))
                    },
                    openPlayer = {
                        backStack.add(Routes.Player(it))
                    }
                )
            }
            entry<Routes.Conference> { key ->
                val viewModel = hiltViewModel<ConferenceViewModel, ConferenceViewModel.Factory> { factory ->
                    factory.create(key.id)
                }

                ConferenceRoute(
                    viewModel,
                    openLecture = {
                        backStack.add(Routes.Lecture(it))
                    },
                    openPlayer = {
                        backStack.add(Routes.Player(it))
                    }
                )
            }
            entry<Routes.Lecture> { key ->
                Text("Lecture: ${key.id}")
            }
            entry<Routes.Player> { key ->
                val viewModel = hiltViewModel<PlayerViewModel, PlayerViewModel.Factory> { factory ->
                    factory.create(key.id)
                }
                PlayerRoute(viewModel)
            }
        }
    )
}
