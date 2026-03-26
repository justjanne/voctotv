package de.justjanne.voctotv.mobile.route.player

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import de.justjanne.voctotv.mobile.ui.useSystemUi

@Composable
fun SystemUiController(
    uiState: PlayerUiState,
    isCasting: Boolean,
) {
    val systemUi = useSystemUi()
    val isDarkTheme = isSystemInDarkTheme()

    DisposableEffect(uiState.isFullscreen.value, isCasting, isDarkTheme) {
        if (!isCasting) {
            if (uiState.isFullscreen.value) {
                systemUi?.hideSystemUi()
            } else {
                systemUi?.showSystemUi()
            }
        }

        onDispose {
            systemUi?.showSystemUi()
        }
    }
}
