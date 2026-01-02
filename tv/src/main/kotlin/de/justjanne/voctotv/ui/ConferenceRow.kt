package de.justjanne.voctotv.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Text
import coil3.compose.AsyncImage
import de.ccc.media.api.ConferenceModel
import de.justjanne.voctotv.ui.theme.VoctoTvTheme

@Composable
fun ConferenceRow(
    title: String,
    featured: List<ConferenceModel>,
    openConference: (String) -> Unit,
) {
    Text(title, modifier = Modifier.padding(horizontal = 20.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(20.dp),
    ) {
        items(featured) { conference ->
            Column {
                StandardCardContainer(
                    modifier = Modifier.width(196.dp),
                    imageCard = { interactionSource ->
                        VoctoTvTheme(isInDarkTheme = false) {
                            Card(
                                onClick = { openConference(conference.acronym) },
                                modifier = Modifier
                                    .aspectRatio(16f / 9),
                                interactionSource = interactionSource,
                            ) {
                                AsyncImage(
                                    model = conference.logoUrl,
                                    contentDescription = conference.title,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    },
                    title = {
                        Text(
                            text = conference.title,
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                            minLines = 2,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                )
            }
        }
    }
}
