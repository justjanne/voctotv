/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.tv.route.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.carousel.HeroCarousel
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselContent
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselDefaults
import de.justjanne.voctotv.voctoweb.model.LectureModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun FeaturedCarousel(
    lectures: List<LectureModel>,
    navigate: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    showConference: Boolean = true,
) {
    HeroCarousel(lectures.size, modifier = modifier) { index ->
        val lecture = lectures[index]

        Card(
            onClick = { navigate(Routes.Player(lecture.guid)) },
            colors =
                CardDefaults.colors(
                    containerColor = HeroCarouselDefaults.BackgroundColor,
                    focusedContainerColor = HeroCarouselDefaults.BackgroundColor,
                    pressedContainerColor = HeroCarouselDefaults.BackgroundColor,
                ),
        ) {
            val background = rememberAsyncImagePainter(lecture.posterUrl)
            HeroCarouselContent(
                background = background,
                label = {
                    if (showConference) {
                        Text(
                            lecture.conferenceTitle,
                            maxLines = 1,
                        )
                    }
                },
                title = {
                    Text(
                        lecture.title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2,
                    )
                },
                byline = {
                    Text(lecture.persons.fastJoinToString(" · "))
                },
                description = {
                    lecture.description?.let {
                        Text(
                            it.replace(Regex("\n\n+"), "\n"),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                action = {
                    WatchNowButton(onClick = {
                        navigate(Routes.Player(lecture.guid))
                    })
                },
            )
        }
    }
}
