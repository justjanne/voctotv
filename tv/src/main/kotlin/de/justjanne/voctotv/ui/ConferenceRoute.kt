package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Text
import de.justjanne.voctotv.viewmodel.ConferenceViewModel

@Composable
fun ConferenceRoute(
    viewModel: ConferenceViewModel,
    openLecture: (String) -> Unit,
    openPlayer: (String) -> Unit,
) {
    val conference by viewModel.conference.collectAsState()
    val featured by viewModel.featured.collectAsState()
    val itemsByTrack by viewModel.itemsByTrack.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        conference?.let { conference ->
            item("header") {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(start = 32.dp, end = 32.dp, top = 32.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = conference.title,
                        style = MaterialTheme.typography.titleLarge.copy(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                offset = Offset(x = 2f, y = 4f),
                                blurRadius = 2f
                            )
                        ),
                        maxLines = 2,
                    )
                }
            }
        }

        item("featured") {
            FeaturedCarousel(featured, openPlayer, showConference = false)
        }

        items(itemsByTrack.entries.sortedBy { it.key }.toList(), key = { it.key }) { (track, items) ->
            Text(track, modifier = Modifier.padding(horizontal = 20.dp))

            val focusRequester = remember(track) { FocusRequester() }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.focusRestorer(focusRequester)
            ) {
                itemsIndexed(items, key = { _, lecture -> lecture.guid }) { index, lecture ->
                    LectureCard(lecture, openPlayer, if (index == 0) Modifier.focusRequester(focusRequester) else Modifier)
                }
            }
        }
    }
}
