package de.justjanne.voctotv.mobile.route.player

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.util.switchScreenOrientation
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun PlayerUiLandscapeFullscreen(
    viewModel: PlayerViewModel,
    contentPadding: PaddingValues,
    uiState: PlayerUiState,
    video: VideoModel?,
    content: @Composable BoxScope.() -> Unit,
) {
    val context = LocalActivity.current
    val onFullscreen: () -> Unit =
        remember(context) {
            {
                context?.switchScreenOrientation(Configuration.ORIENTATION_PORTRAIT)
            }
        }

    BackHandler(onBack = onFullscreen)

    PlayerContainer(
        contentPadding = contentPadding,
        playerState = viewModel.playerState,
        sidebarVisible = uiState.descriptionVisible,
        sidebar = {
            if (video != null) {
                VideoDescription(
                    video = video,
                    onClose = { uiState.toggleDescription(false) },
                )
            }
        },
        overlay = { contentPadding ->
            PlayerOverlay(
                contentPadding = contentPadding,
                viewModel = viewModel,
                video = video,
                showPreview = true,
                showTitle = !uiState.descriptionVisible.value,
                isFullscreen = true,
                onFullscreen = onFullscreen,
                onDescription = uiState::toggleDescription,
                back = onFullscreen,
            )
        },
        content = content,
    )
}
