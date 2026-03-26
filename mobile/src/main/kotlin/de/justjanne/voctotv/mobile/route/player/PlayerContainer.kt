package de.justjanne.voctotv.mobile.route.player

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import de.justjanne.voctotv.common.player.PlayerState
import de.justjanne.voctotv.mobile.ui.ModalSideSheet
import de.justjanne.voctotv.mobile.ui.ModalSideSheetDefaults

@Composable
fun PlayerContainer(
    contentPadding: PaddingValues,
    playerState: PlayerState,
    sidebarVisible: MutableState<Boolean>,
    sidebar: @Composable () -> Unit,
    overlay: @Composable (contentPadding: PaddingValues) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    BackHandler(sidebarVisible.value) {
        sidebarVisible.value = false
    }

    val progress =
        animateFloatAsState(
            if (sidebarVisible.value) 1f else 0f,
            animationSpec =
                spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMediumLow,
                ),
        )

    val localLayoutDirection = LocalLayoutDirection.current
    val startContentPadding = contentPadding.calculateStartPadding(localLayoutDirection)
    val endContentPadding = contentPadding.calculateEndPadding(localLayoutDirection)
    val topContentPadding = contentPadding.calculateTopPadding()
    val bottomContentPadding = contentPadding.calculateBottomPadding()

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val aspectRatio =
            playerState.aspectRatio.let {
                if (it == 0f) {
                    constraints.maxWidth.toFloat() / constraints.maxHeight.toFloat()
                } else {
                    it
                }
            }

        val largeFrameWidth = maxWidth
        val largeFrameHeight = maxHeight
        val largeFrameAspect = largeFrameWidth / largeFrameHeight

        val largeTargetWidth = if (largeFrameAspect > aspectRatio) largeFrameHeight * aspectRatio else largeFrameWidth
        val largeTargetHeight = if (largeFrameAspect > aspectRatio) largeFrameHeight else largeFrameWidth / aspectRatio

        val startPadding =
            with(LocalDensity.current) {
                WindowInsets.safeDrawing.getLeft(LocalDensity.current, LocalLayoutDirection.current).toDp()
            }
        val modalSheetWidth = ModalSideSheetDefaults.Width + endContentPadding

        val smallFrameWidth = maxWidth - startPadding - modalSheetWidth
        val smallFrameHeight = maxHeight

        val padding = lerp(0.dp, startPadding, progress.value)
        val sidebarWidth = lerp(0.dp, modalSheetWidth, progress.value)

        val frameWidth = lerp(largeFrameWidth, smallFrameWidth, progress.value)
        val frameHeight = lerp(largeFrameHeight, smallFrameHeight, progress.value)

        val frameAspect = frameWidth / frameHeight

        val contentWidth = if (frameAspect > aspectRatio) frameHeight * aspectRatio else frameWidth
        val contentHeight = if (frameAspect > aspectRatio) frameHeight else frameWidth / aspectRatio

        val endContentPadding = lerp(endContentPadding, 0.dp, progress.value)

        val scaleModifier =
            Modifier.graphicsLayer {
                scaleY = contentWidth / largeTargetWidth
                scaleX = contentHeight / largeTargetHeight
                transformOrigin = TransformOrigin(0f, 0.5f)
                translationX = (frameWidth - contentWidth).toPx() / 2 + padding.toPx()
            }

        Box(
            modifier =
                Modifier
                    .align(Alignment.CenterStart)
                    .size(largeTargetWidth, largeTargetHeight)
                    .then(scaleModifier),
        ) {
            content()
        }

        Box(modifier = Modifier.width(maxWidth - sidebarWidth).fillMaxHeight()) {
            overlay(
                PaddingValues(
                    start = startContentPadding,
                    end = endContentPadding,
                    top = topContentPadding,
                    bottom = bottomContentPadding,
                )
            )
        }

        if (sidebarVisible.value || sidebarWidth > 0.dp) {
            Box(
                modifier =
                    Modifier.align(Alignment.CenterEnd).graphicsLayer {
                        translationX = ModalSideSheetDefaults.Width.toPx() - sidebarWidth.toPx()
                    },
            ) {
                ModalSideSheet(Modifier.padding(top = topContentPadding, bottom = bottomContentPadding)) {
                    sidebar()
                }
            }
        }
    }
}
