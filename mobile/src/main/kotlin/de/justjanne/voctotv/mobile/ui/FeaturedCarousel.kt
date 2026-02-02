/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.util.CarouselIndicator
import de.justjanne.voctotv.voctoweb.model.LectureModel

@Composable
fun FeaturedCarousel(
    lectures: List<LectureModel>,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState { lectures.size }

    if (lectures.isNotEmpty()) {
        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(pagerState, modifier = Modifier.fillMaxWidth()) { index ->
                val lecture = lectures[index]
                val painter = rememberAsyncImagePainter(lecture.posterUrl)
                FeaturedCarouselItem(
                    onClick = { navigate(Routes.Player(lecture.guid)) },
                    background = painter,
                    title = lecture.title,
                    persons = lecture.persons,
                )
            }
            CarouselIndicator(
                pagerState,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

