package de.justjanne.voctotv.tv.route.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.tv.ui.ModalSideSheetDefaults

@Composable
fun PlayerContainer(
    playerState: PlayerState,
    sidebarVisible: MutableState<Boolean>,
    sidebar: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    BackHandler(sidebarVisible.value) {
        sidebarVisible.value = false
    }

    val padding = animateDpAsState(
        targetValue = if (sidebarVisible.value) 24.dp else 0.dp
    )
    val sidebarWidth = animateDpAsState(
        targetValue = if (sidebarVisible.value) ModalSideSheetDefaults.Width else 0.dp
    )

    BoxWithConstraints {
        val aspectRatio = playerState.aspectRatio
        val sidebarVisible = sidebarVisible.value || sidebarWidth.value > 0.dp

        val aspectModifier = if (aspectRatio == 0f) Modifier.Companion else Modifier.aspectRatio(aspectRatio)
        val scaleModifier = if (!sidebarVisible) Modifier.Companion else Modifier.graphicsLayer {
            val containerWidth = constraints.maxWidth.toFloat()
            val padding = padding.value.toPx()
            val sidebarWidth = sidebarWidth.value.toPx()
            val contentWidth = containerWidth - padding - sidebarWidth
            val scale = contentWidth / containerWidth
            scaleY = scale
            scaleX = scale
            transformOrigin = TransformOrigin(0f, 0.5f)
            translationX = padding
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .then(aspectModifier)
                .then(scaleModifier),
            content = content,
        )

        Box(
            modifier = Modifier.align(Alignment.CenterEnd).graphicsLayer {
                translationX = ModalSideSheetDefaults.Width.toPx() - sidebarWidth.value.toPx()
            },
        ) {
            sidebar()
        }
    }
}
