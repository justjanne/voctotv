package de.justjanne.voctotv.tv.route.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.Text
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.Routes
import de.justjanne.voctotv.tv.ui.WatchNowButton
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselContent
import de.justjanne.voctotv.tv.ui.carousel.HeroCarouselDefaults

@Composable
fun HeroCarouselStream(
    item: FeaturedItem.Stream,
    navigate: (NavKey) -> Unit,
) {
    Card(
        onClick = { navigate(Routes.LiveConference(item.conference.slug)) },
        colors =
            CardDefaults.colors(
                containerColor = HeroCarouselDefaults.BackgroundColor,
                focusedContainerColor = HeroCarouselDefaults.BackgroundColor,
                pressedContainerColor = HeroCarouselDefaults.BackgroundColor,
            ),
    ) {
        val background = rememberAsyncImagePainter(item.rooms.first().poster)
        HeroCarouselContent(
            background = background,
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.ic_live),
                        contentDescription = null,
                        tint = Color.Red,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("LIVE")
                }
            },
            title = {
                Text(
                    item.conference.conference,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
            },
            description = {
                Text(
                    item.conference.description.replace(Regex("\n\n+"), "\n"),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            action = {
                WatchNowButton(onClick = {
                    navigate(Routes.LiveConference(item.conference.slug))
                })
            },
        )
    }
}
