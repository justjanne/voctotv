package de.justjanne.voctotv.mobile.route.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.WatchNowButton
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarouselContent


@Composable
fun HeroCarouselLecture(
    item: FeaturedItem.Lecture,
    navigate: (NavKey) -> Unit
) {
    val painter = rememberAsyncImagePainter(item.lecture.posterUrl)

    HeroCarouselContent(
        background = painter,
        title = {
            Text(
                item.lecture.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        },
        description = {
            Text(
                item.lecture.persons.fastJoinToString(" · "),
                maxLines = 1,
            )
        },
        action = {
            WatchNowButton(onClick = {
                navigate(Routes.PlayerVod(item.lecture.guid))
            })
        },
    )
}
