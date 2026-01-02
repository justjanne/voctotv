package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import de.justjanne.voctotv.viewmodel.HomeViewModel

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    openConference: (String) -> Unit,
    openLecture: (String) -> Unit,
    openPlayer: (String) -> Unit,
) {
    val conferences by viewModel.conferences.collectAsState()
    val erfas by viewModel.erfas.collectAsState()
    val recent by viewModel.recent.collectAsState()
    val featuredLectures by viewModel.featuredLectures.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize()
    ) {
        item("featured") {
            FeaturedCarousel(featuredLectures, openPlayer)
        }

        item("conferences") {
            ConferenceRow("Conferences", conferences, openConference)
        }

        item("erfas") {
            ConferenceRow("Erfas", erfas, openConference)
        }

        item("recent-lectures") {
            Text("Recent", modifier = Modifier.padding(horizontal = 20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(20.dp),
            ) {
                items(recent) { lecture ->
                    LectureCard(lecture, openPlayer)
                }
            }
        }
    }
}
