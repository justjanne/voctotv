package de.justjanne.voctotv.mobile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.viewmodel.ConferenceKind
import de.justjanne.voctotv.viewmodel.HomeViewModel
import de.justjanne.voctotv.viewmodel.kind

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeRoute(
    viewModel: HomeViewModel,
    navigate: (NavKey) -> Unit,
) {
    val allConferences = viewModel.allConferences.collectAsState()
    val conferences = viewModel.conferences.collectAsState()
    val currentFilter = remember { mutableStateOf<ConferenceKind?>(null) }
    val filteredItems = remember {
        derivedStateOf {
            currentFilter.value?.let { filter -> conferences.value.firstOrNull { it.key == filter } }?.value
                ?: allConferences.value
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painterResource(R.drawable.ic_mediacccde),
                        contentDescription = "media.ccc.de",
                        modifier = Modifier.height(with (LocalDensity.current) { LocalTextStyle.current.lineHeight.toDp() })
                    )
                }
            )
        }
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
        ) {
            item("filter") {
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
                    items(ConferenceKind.entries) { filter ->
                        FilterChip(
                            selected = currentFilter.value == filter,
                            onClick = { currentFilter.value = filter },
                            label = {
                                Text(
                                    when (filter) {
                                        ConferenceKind.CONGRESS -> "Congress"
                                        ConferenceKind.GPN -> "GPN"
                                        ConferenceKind.CONFERENCE -> "Conferences"
                                        ConferenceKind.DOCUMENTATIONS -> "Documentaries"
                                        ConferenceKind.ERFA -> "Erfas"
                                        ConferenceKind.OTHER -> "Other"
                                    }
                                )
                            },
                        )
                    }
                }
            }

            items(filteredItems.value, key = { it.acronym }) { item ->
                ConferenceItem(
                    item = item,
                    showYear = item.kind() != ConferenceKind.ERFA,
                    onClick = {
                        navigate(Routes.Conference(item.acronym))
                    }
                )
            }
        }
    }
}