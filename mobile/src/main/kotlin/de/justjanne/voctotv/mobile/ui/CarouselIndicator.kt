/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CarouselIndicator(
    itemCount: Int,
    activeItemIndex: Int,
    modifier: Modifier = Modifier.Companion,
    spacing: Dp = 8.dp,
    alignment: Alignment.Horizontal = Alignment.Companion.Start,
    indicator: @Composable (isActive: Boolean) -> Unit = { isActive ->
        val activeColor = Color.Companion.White
        val inactiveColor = activeColor.copy(alpha = 0.3f)
        Box(
            modifier =
                Modifier.Companion.size(8.dp)
                    .background(
                        color = if (isActive) activeColor else inactiveColor,
                        shape = CircleShape,
                    ),
        )
    }
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing, alignment = alignment),
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = modifier,
    ) {
        repeat(itemCount) {
            val isActive = it == activeItemIndex
            indicator(isActive)
        }
    }
}

@Composable
fun CarouselIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp,
    alignment: Alignment.Horizontal = Alignment.Start,
) {
    CarouselIndicator(
        pagerState.pageCount,
        pagerState.currentPage,
        modifier,
        spacing,
        alignment,
    )
}
