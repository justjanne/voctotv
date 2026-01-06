package de.justjanne.voctotv.ui.player

import android.text.Layout
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.media3.common.Player
import androidx.media3.common.text.Cue
import androidx.media3.common.text.CueGroup
import de.justjanne.voctotv.viewmodel.PlayerViewModel

@Composable
fun SubtitleDisplay(viewModel: PlayerViewModel) {
    val currentCue = remember { mutableStateOf<CueGroup?>(null) }
    DisposableEffect(viewModel.mediaSession.player) {
        val listener = object : Player.Listener {
            override fun onCues(cueGroup: CueGroup) {
                currentCue.value = cueGroup
            }
        }
        viewModel.mediaSession.player.addListener(listener)
        onDispose {
            viewModel.mediaSession.player.removeListener(listener)
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val lineSize = constraints.maxHeight / 20f
        val padding = with(LocalDensity.current) { lineSize.toDp() }
        val lineHeight = with(LocalDensity.current) { lineSize.toSp() }

        Column(Modifier.align(Alignment.TopCenter).padding(top = padding, start = padding, end = padding)) {
            currentCue.value?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_START && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
        Column(Modifier.align(Alignment.Center).padding(start = padding, end = padding)) {
            currentCue.value?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_MIDDLE && it.line >= 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
        Column(Modifier.align(Alignment.BottomCenter).padding(bottom = padding, start = padding, end = padding)) {
            currentCue.value?.cues
                ?.filter { it.lineAnchor == Cue.ANCHOR_TYPE_END || it.line < 0 }
                ?.forEach { LineDisplay(it, lineHeight) }
        }
    }
}

@Composable
fun ColumnScope.LineDisplay(
    cue: Cue,
    lineHeight: TextUnit,
) {
    cue.text?.let { text ->
        Surface(
            modifier = Modifier
                .align(when (cue.positionAnchor) {
                    Cue.ANCHOR_TYPE_START -> Alignment.Start
                    Cue.ANCHOR_TYPE_END -> Alignment.End
                    else -> Alignment.CenterHorizontally
                }),
            color = Color.Black.copy(alpha = 0.8f),
            contentColor = Color.White,
        ) {
            Text(
                text = text.toString(),
                modifier = Modifier.padding(horizontal = 4.dp),
                textAlign = when (cue.textAlignment) {
                    Layout.Alignment.ALIGN_CENTER -> TextAlign.Center
                    Layout.Alignment.ALIGN_NORMAL -> TextAlign.Start
                    Layout.Alignment.ALIGN_OPPOSITE -> TextAlign.End
                    null -> TextAlign.Start
                },
                fontSize = lineHeight / 1.25,
                lineHeight = lineHeight,
            )
        }
    }
}
