package de.justjanne.voctotv.tv.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.viewmodel.ConferenceKind
import de.justjanne.voctotv.common.viewmodel.HomeViewModel
import de.justjanne.voctotv.tv.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val conferences by viewModel.conferences.collectAsState()
    val recent by viewModel.recent.collectAsState()
    val featuredLectures by viewModel.featuredLectures.collectAsState()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        item("header") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 20.dp),
            ) {
                Image(
                    painterResource(R.drawable.ic_mediacccde),
                    contentDescription = null,
                    modifier = Modifier.height(32.dp),
                    colorFilter = ColorFilter.tint(LocalContentColor.current),
                )
            }
        }

        item("featured") {
            FeaturedCarousel(featuredLectures, navigate, Modifier.focusRequester(focusRequester))
        }

        item("recent-lectures") {
            Text("Recent", modifier = Modifier.padding(horizontal = 20.dp))

            val focusRequester = remember { FocusRequester() }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.focusRestorer(focusRequester),
            ) {
                itemsIndexed(recent, key = { _, lecture -> lecture.guid }) { index, lecture ->
                    LectureCard(lecture, navigate, if (index == 0) Modifier.focusRequester(focusRequester) else Modifier)
                }
            }
        }

        items(conferences, key = { it.key }) {
            val title =
                remember(it) {
                    when (it.key) {
                        ConferenceKind.CONGRESS -> "Congress"
                        ConferenceKind.GPN -> "GPN"
                        ConferenceKind.CONFERENCE -> "Conferences"
                        ConferenceKind.DOCUMENTATIONS -> "Documentaries"
                        ConferenceKind.ERFA -> "Erfas"
                        ConferenceKind.OTHER -> "Other"
                    }
                }
            ConferenceRow(title, it.value, navigate)
        }
    }
}
