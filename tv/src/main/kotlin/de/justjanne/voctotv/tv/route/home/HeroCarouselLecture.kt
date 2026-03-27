package de.justjanne.voctotv.tv.route.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.WatchNowButton
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselContent
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselDefaults

@Composable
fun HeroCarouselLecture(
    item: FeaturedItem.Lecture,
    navigate: (NavKey) -> Unit,
) {

    Card(
        onClick = { navigate(Routes.PlayerVod(item.lecture.guid)) },
        colors =
            CardDefaults.colors(
                containerColor = HeroCarouselDefaults.BackgroundColor,
                focusedContainerColor = HeroCarouselDefaults.BackgroundColor,
                pressedContainerColor = HeroCarouselDefaults.BackgroundColor,
            ),
    ) {
        val background = rememberAsyncImagePainter(item.lecture.posterUrl)
        HeroCarouselContent(
            background = background,
            label = {
                Text(
                    item.lecture.conferenceTitle,
                    maxLines = 1,
                )
            },
            title = {
                Text(
                    item.lecture.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            byline = {
                Text(item.lecture.persons.fastJoinToString(" · "))
            },
            description = {
                item.lecture.description?.let {
                    Text(
                        it.replace(Regex("\n\n+"), "\n"),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            },
            action = {
                WatchNowButton(onClick = {
                    navigate(Routes.PlayerVod(item.lecture.guid))
                })
            },
        )
    }
}
