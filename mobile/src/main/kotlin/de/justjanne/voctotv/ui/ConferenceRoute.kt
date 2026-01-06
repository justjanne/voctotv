package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.R
import de.justjanne.voctotv.Routes
import de.justjanne.voctotv.mediacccde.model.LectureModel
import de.justjanne.voctotv.viewmodel.ConferenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConferenceRoute(
    viewModel: ConferenceViewModel,
    navigate: (NavKey) -> Unit,
    back: () -> Unit,
) {
    val conference = viewModel.conference.collectAsState().value
    val allItems = viewModel.allItems.collectAsState()
    val itemsByTrack = viewModel.itemsByTrack.collectAsState()
    val currentFilter = remember { mutableStateOf<String?>(null) }
    val filters = remember {
        derivedStateOf {
            itemsByTrack.value.map { it.key }.sorted()
        }
    }
    val filteredItems: State<List<LectureModel>> = remember {
        derivedStateOf {
            currentFilter.value?.let { filter -> itemsByTrack.value[filter] }
                ?: allItems.value.orEmpty()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(conference?.title ?: "Conference") },
                navigationIcon = {
                    IconButton(onClick = { back() } ) {
                        Icon(
                            painterResource(R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                        )
                    }
                }
            )
        }
    ){ contentPadding ->
        if (conference == null) {
            Box(Modifier.fillMaxSize()) {
                Column(Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxSize(),
            ) {
                item("filters") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        item(null) {
                            FilterChip(
                                selected = currentFilter.value == null,
                                onClick = { currentFilter.value = null },
                                label = { Text("Everything") },
                            )
                        }
                        items(filters.value) { filter ->
                            FilterChip(
                                selected = currentFilter.value == filter,
                                onClick = { currentFilter.value = filter },
                                label = { Text(filter) },
                            )
                        }
                    }
                }

                items(filteredItems.value, key = { it.guid }) { item ->
                    LectureItem(
                        item = item,
                        onClick = {
                            navigate(Routes.Player(item.guid))
                        }
                    )
                }
            }
        }
    }
}