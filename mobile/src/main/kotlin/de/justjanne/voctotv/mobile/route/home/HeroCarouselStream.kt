package de.justjanne.voctotv.mobile.route.home

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation3.runtime.NavKey
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.WatchNowButton
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarouselContent


@Composable
fun HeroCarouselStream(
    item: FeaturedItem.Stream,
    navigate: (NavKey) -> Unit
) {
    val painter = rememberAsyncImagePainter(item.rooms.first().poster)

    HeroCarouselContent(
        background = painter,
        title = {
            Text(
                item.conference.conference,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
            )
        },
        description = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.ic_live),
                    contentDescription = null,
                    tint = Color.Red,
                )
                Text("LIVE")
            }
        },
        action = {
            WatchNowButton {
                navigate(Routes.LiveConference(item.conference.slug))
            }
        },
    )
}
