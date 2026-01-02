package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.justjanne.voctotv.viewmodel.ConferenceViewModel
import de.justjanne.voctotv.viewmodel.HomeViewModel

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
            FeaturedCarousel(featured, openPlayer)
        }

        items(itemsByTrack.entries.sortedBy { it.key }.toList()) { (track, items) ->
            Text(track, modifier = Modifier.padding(horizontal = 20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                items(items) { lecture ->
                    LectureCard(lecture, openPlayer)
                }
            }
        }
    }
}