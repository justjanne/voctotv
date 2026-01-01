package de.justjanne.voctotv

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.tv.material3.Text

@Composable
fun AppRouter() {
    val backStack: MutableList<NavKey> = rememberNavBackStack(Routes.Home)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Routes.Home> {
                HomeRoute(
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
                Text("Conference: ${key.id}")
            }
            entry<Routes.Lecture> { key ->
                Text("Lecture: ${key.id}")
            }
            entry<Routes.Player> { key ->
                VideoPlayerRoute(key.id)
            }
        }
    )
}
