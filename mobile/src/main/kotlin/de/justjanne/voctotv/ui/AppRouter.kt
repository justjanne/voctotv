package de.justjanne.voctotv.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRouter() {
    val backStack = rememberNavBackStack(Routes.Home)
    val navigate: (NavKey) -> Unit = remember(backStack) {
        { backStack.add(it) }
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<Routes.Home> {
                val viewModel = hiltViewModel<HomeViewModel>()
                HomeRoute(viewModel, navigate)
            }
            entry<Routes.Conference> { key ->
                val viewModel = hiltViewModel<ConferenceViewModel, ConferenceViewModel.Factory> { factory ->
                    factory.create(key.id)
                }
                ConferenceRoute(viewModel, navigate, back = { backStack.removeAt(backStack.lastIndex) })
            }
            entry<Routes.Lecture> { key ->
                Text("Lecture")
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
