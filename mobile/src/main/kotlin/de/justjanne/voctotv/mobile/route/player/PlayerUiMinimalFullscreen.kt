package de.justjanne.voctotv.mobile.route.player

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun PlayerUiMinimalFullscreen(
    viewModel: PlayerViewModel,
    contentPadding: PaddingValues,
    showFullscreenButton: Boolean,
    uiState: PlayerUiState,
    video: VideoModel?,
    back: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val onFullscreen: () -> Unit =
        remember(uiState) {
            {
                uiState.toggleDescription(true)
            }
        }

    BackHandler(enabled = showFullscreenButton, onBack = onFullscreen)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier =
                Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .aspectRatio(
                        if (viewModel.playerState.aspectRatio > 0f) viewModel.playerState.aspectRatio else 16 / 9f,
                        matchHeightConstraintsFirst = true,
                    ).fillMaxSize(),
        ) {
            content()
            PreviewOverlay(viewModel)
        }
        PlayerOverlay(
            contentPadding = contentPadding,
            viewModel = viewModel,
            video = video,
            showTitle = false,
            showPreview = false,
            showFullscreenButton = showFullscreenButton,
            isFullscreen = showFullscreenButton,
            onFullscreen = onFullscreen,
            onDescription = uiState::toggleDescription,
            back = if (showFullscreenButton) onFullscreen else back,
        )
    }
}
