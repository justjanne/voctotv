/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.route.player

import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import de.justjanne.voctotv.mobile.ui.theme.PlayerScrimBottom
import de.justjanne.voctotv.mobile.ui.theme.PlayerScrimTop

@OptIn(UnstableApi::class)
@Composable
fun PlayerOverlayLayout(
    visible: Boolean,
    centerVisible: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    top: @Composable RowScope.() -> Unit,
    bottom: @Composable ColumnScope.() -> Unit,
    center: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
) {
    val layoutDirection = LocalLayoutDirection.current

    val mainInteractionSource = remember { MutableInteractionSource() }

    val transition = updateTransition(visible, label = "overlay visibility state")

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable(mainInteractionSource, indication = null, enabled = enabled) { onClick() },
    ) {
        transition.AnimatedVisibility(
            visible = { it },
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 }),
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Row(
                modifier =
                    Modifier
                        .background(PlayerScrimTop)
                        .fillMaxWidth()
                        .then(modifier)
                        .padding(8.dp)
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            top = contentPadding.calculateTopPadding(),
                        ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                top()
            }
        }

        transition.AnimatedVisibility(
            visible = { it },
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            Column(
                modifier =
                    Modifier
                        .background(PlayerScrimBottom)
                        .fillMaxWidth()
                        .then(modifier)
                        .padding(bottom = 8.dp)
                        .padding(
                            start = contentPadding.calculateStartPadding(layoutDirection),
                            end = contentPadding.calculateEndPadding(layoutDirection),
                            bottom = contentPadding.calculateBottomPadding(),
                        ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
            ) {
                bottom()
            }
        }

        transition.AnimatedVisibility(
            visible = { it && centerVisible },
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.Center),
        ) {
            center()
        }
    }
}
