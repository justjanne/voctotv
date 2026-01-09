/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.subtitles

import android.text.Layout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.media3.common.text.Cue
import androidx.media3.common.util.UnstableApi

@OptIn(UnstableApi::class)
@Composable
fun ColumnScope.LineDisplay(
    cue: Cue,
    lineHeight: TextUnit,
    containerColor: Color,
    contentColor: Color,
) {
    LaunchedEffect(cue) {
        println(
            "Cue(text=${cue.text}, textAlignment=${cue.textAlignment}, multiRowAlignment=${cue.multiRowAlignment}, bitmap=${cue.bitmap == null}, line=${cue.line}, lineType=${cue.lineType}, lineAnchor=${cue.lineAnchor}, position=${cue.position}, positionAnchor=${cue.positionAnchor}, textSizeType=${cue.textSizeType}, textSize=${cue.textSize}, size=${cue.size}, bitmapHeight=${cue.bitmapHeight}, windowColorSet=${cue.windowColorSet}, windowColor=${cue.windowColor}, verticalType=${cue.verticalType}, shearDegrees=${cue.shearDegrees}, zIndex=${cue.zIndex})",
        )
    }

    cue.text?.let { text ->
        Surface(
            modifier =
                Modifier.align(
                    when (cue.positionAnchor) {
                        Cue.ANCHOR_TYPE_START -> Alignment.Start
                        Cue.ANCHOR_TYPE_END -> Alignment.End
                        else -> Alignment.CenterHorizontally
                    },
                ),
            color = containerColor,
            contentColor = contentColor,
        ) {
            Text(
                text = text.toString(),
                modifier = Modifier.padding(horizontal = 4.dp),
                textAlign =
                    when (cue.textAlignment) {
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
