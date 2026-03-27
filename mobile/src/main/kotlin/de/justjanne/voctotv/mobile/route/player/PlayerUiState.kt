package de.justjanne.voctotv.mobile.route.player

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import de.justjanne.voctotv.common.player.PlayerState

class PlayerUiState(
    playerState: PlayerState,
    configuration: Configuration,
) {
    val isLandscapeUI = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val isLandscapeVideo = derivedStateOf { playerState.aspectRatio > 1f }

    val descriptionVisible = mutableStateOf(!isLandscapeUI)

    val isFullscreen: State<Boolean> =
        derivedStateOf {
            isLandscapeUI || !descriptionVisible.value
        }

    fun toggleDescription(visible: Boolean) {
        descriptionVisible.value = visible
    }
}

@Composable
fun rememberPlayerUiState(playerState: PlayerState): PlayerUiState {
    val configuration = LocalConfiguration.current

    return remember(playerState, configuration) {
        PlayerUiState(playerState, configuration)
    }
}
