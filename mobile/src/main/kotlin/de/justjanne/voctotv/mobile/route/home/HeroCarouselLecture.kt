package de.justjanne.voctotv.mobile.route.home

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import androidx.navigation3.runtime.NavKey
import coil3.compose.rememberAsyncImagePainter
import de.justjanne.voctotv.common.viewmodel.FeaturedItem
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.WatchNowButton
import de.justjanne.voctotv.mobile.ui.carousel.HeroCarouselContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarouselLecture(
    item: FeaturedItem.Lecture,
    navigate: (NavKey) -> Unit,
    isSaved: Boolean,
    onLongClick: () -> Unit,
) {
    val painter = rememberAsyncImagePainter(item.lecture.posterUrl)

    Box(
        modifier =
            Modifier.combinedClickable(
                onClick = { navigate(Routes.PlayerVod(item.lecture.guid)) },
                onLongClick = onLongClick,
            ),
    ) {
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
        if (isSaved) {
            Icon(
                painter = painterResource(R.drawable.ic_star),
                contentDescription = stringResource(R.string.action_watch_later),
                tint = Color(0xFFFFD54F),
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp).size(20.dp),
            )
        }
    }
}
