package de.justjanne.voctotv.tv.route.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.util.fastJoinToString
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
fun HeroCarouselLecture(
    item: FeaturedItem.Lecture,
    navigate: (NavKey) -> Unit,
    isSaved: Boolean,
    onLongClick: () -> Unit,
) {
    Card(
        onClick = { navigate(Routes.PlayerVod(item.lecture.guid)) },
        onLongClick = onLongClick,
        colors =
            CardDefaults.colors(
                containerColor = HeroCarouselDefaults.BackgroundColor,
                focusedContainerColor = HeroCarouselDefaults.BackgroundColor,
                pressedContainerColor = HeroCarouselDefaults.BackgroundColor,
            ),
    ) {
        val background = rememberAsyncImagePainter(item.lecture.posterUrl)
        Box {
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
            if (isSaved) {
                Icon(
                    painter = painterResource(R.drawable.ic_star),
                    contentDescription = stringResource(R.string.action_watch_later),
                    tint = Color(0xFFFFD54F),
                    modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(24.dp),
                )
            }
        }
    }
}
