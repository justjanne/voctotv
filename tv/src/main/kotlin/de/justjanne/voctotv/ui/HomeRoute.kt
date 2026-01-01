package de.justjanne.voctotv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Text
import de.justjanne.voctotv.ui.FeaturedRow
import de.justjanne.voctotv.ui.LectureCard
import de.justjanne.voctotv.viewmodel.HomeViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = viewModel(),
    openConference: (String) -> Unit,
    openLecture: (String) -> Unit,
    openPlayer: (String) -> Unit,
) {
    val featured by homeViewModel.featured.collectAsState()
    val recent by homeViewModel.recent.collectAsState()

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        FeaturedRow(featured, openConference)

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