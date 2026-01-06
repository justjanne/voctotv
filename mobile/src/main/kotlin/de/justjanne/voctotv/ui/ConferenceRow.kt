package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.justjanne.voctotv.mediacccde.model.ConferenceModel
import de.justjanne.voctotv.ui.theme.VoctoTvTheme

@Composable
fun ConferenceRow(
    title: String,
    featured: List<ConferenceModel>,
    openConference: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }

    Text(title, modifier = Modifier.padding(horizontal = 20.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp),
        modifier = Modifier.focusRestorer(focusRequester)
    ) {
        itemsIndexed(featured, key = { _, item -> item.acronym }) { index, conference ->
            val modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier

            Column(modifier.width(196.dp)) {
                VoctoTvTheme(isInDarkTheme = false) {
                    Card(
                        onClick = { openConference(conference.acronym) },
                        modifier = Modifier
                            .aspectRatio(16f / 9),
                    ) {
                        AsyncImage(
                            model = conference.logoUrl,
                            contentDescription = conference.title,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    Text(
                        text = conference.title,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
