package de.justjanne.voctotv.common.subtitles

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup

@Composable
fun BoxScope.SubtitleDisplay(
    player: Player,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val aspectRatio = remember { mutableFloatStateOf(16f / 9f) }
    val currentCue = remember { mutableStateOf<CueGroup?>(null) }
    DisposableEffect(player) {
        val listener =
            object : Player.Listener {
                override fun onCues(cueGroup: CueGroup) {
                    currentCue.value = cueGroup
                }

                override fun onVideoSizeChanged(videoSize: VideoSize) {
                    aspectRatio.floatValue = videoSize.width.toFloat() / videoSize.height.toFloat() * videoSize.pixelWidthHeightRatio
                }
            }
        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
        }
    }

    BoxWithConstraints(
        modifier =
            Modifier
                .align(Alignment.Center)
                .aspectRatio(aspectRatio.floatValue)
                .fillMaxSize()
                .padding(contentPadding),
    ) {
        val lineSize = constraints.maxHeight / 20f
        val padding = with(LocalDensity.current) { lineSize.toDp() }
        val lineHeight = with(LocalDensity.current) { lineSize.toSp() }

        Column(Modifier.align(Alignment.TopCenter).padding(top = padding, start = padding, end = padding)) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_START && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
        Column(Modifier.align(Alignment.Center).padding(start = padding, end = padding)) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_MIDDLE && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
        Column(Modifier.align(Alignment.BottomCenter).padding(bottom = padding, start = padding, end = padding)) {
            currentCue.value
                ?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_END || it.line < 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
    }
}
