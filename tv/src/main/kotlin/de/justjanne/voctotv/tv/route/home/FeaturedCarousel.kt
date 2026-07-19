/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.ExperimentalTvMaterial3Api
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.voctoweb.model.LectureModel
import de.justjanne.voctotv.tv.ui.carousel.HeroCarousel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
    items: List<FeaturedItem>,
    navigate: (NavKey) -> Unit,
    isLectureSaved: (String) -> Boolean,
    onLectureLongClick: (LectureModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    HeroCarousel(items.size, modifier = modifier) { index ->
        when (val item = items[index]) {
            is FeaturedItem.Lecture ->
                HeroCarouselLecture(
                    item = item,
                    navigate = navigate,
                    isSaved = isLectureSaved(item.lecture.guid),
                    onLongClick = { onLectureLongClick(item.lecture) },
                )
            is FeaturedItem.Stream -> HeroCarouselStream(item, navigate)
        }
    }
}
