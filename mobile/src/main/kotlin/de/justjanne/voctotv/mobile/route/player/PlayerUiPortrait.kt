package de.justjanne.voctotv.mobile.route.player

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import de.justjanne.voctotv.common.viewmodel.PlayerViewModel
import de.justjanne.voctotv.mobile.util.switchScreenOrientation
import de.justjanne.voctotv.voctoweb.model.VideoModel

@Composable
fun PlayerUiPortrait(
    viewModel: PlayerViewModel,
    contentPadding: PaddingValues,
    uiState: PlayerUiState,
    video: VideoModel?,
    back: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val context = LocalActivity.current
    val onFullscreen: () -> Unit =
        remember(uiState, context) {
            {
                if (uiState.isLandscapeVideo.value) {
                    context?.switchScreenOrientation(Configuration.ORIENTATION_LANDSCAPE)
                } else {
                    uiState.toggleDescription(false)
                }
            }
        }

    BackHandler(onBack = back)

    Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
        val aspectRatio =
            if (viewModel.playerState.aspectRatio > 0f) {
                viewModel.playerState.aspectRatio.coerceAtLeast(4 / 3f)
            } else {
                16 / 9f
            }

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio),
        ) {
            Box(
                modifier = Modifier.clipToBounds(),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .aspectRatio(
                                if (viewModel.playerState.aspectRatio > 0f) viewModel.playerState.aspectRatio else 16 / 9f,
                                matchHeightConstraintsFirst = true,
                            ),
                ) {
                    content()
                    PreviewOverlay(viewModel)
                }
                PlayerOverlay(
                    viewModel = viewModel,
                    video = video,
                    showTitle = false,
                    showPreview = false,
                    isFullscreen = false,
                    onFullscreen = onFullscreen,
                    onDescription = uiState::toggleDescription,
                    back = back,
                )
            }
        }

        if (video != null) {
            VideoDescription(
                video = video,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
