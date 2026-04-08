package de.justjanne.voctotv.tv.route.watchlater

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import de.justjanne.voctotv.common.watchlater.toLectureModel
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.tv.R
import de.justjanne.voctotv.tv.ui.LectureCard
import de.justjanne.voctotv.tv.ui.theme.GridGutter
import de.justjanne.voctotv.tv.ui.theme.GridPadding
import de.justjanne.voctotv.tv.ui.theme.textShadow

@Composable
fun WatchLaterRoute(
    viewModel: WatchLaterViewModel,
    navigate: (NavKey) -> Unit,
) {
    val items by viewModel.items.collectAsState()
    val consumeNextCenterKeyUp = remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        item("header") {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = GridGutter, end = GridGutter, top = 32.dp, bottom = GridPadding),
            ) {
                Text(
                    text = stringResource(R.string.action_watch_later),
                    style =
                        MaterialTheme.typography.titleLarge.copy(
                            shadow = MaterialTheme.colorScheme.textShadow,
                        ),
                    maxLines = 2,
                )
            }
        }

        if (items.isEmpty()) {
            item("empty") {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(R.string.watch_later_empty),
                        modifier = Modifier.align(Alignment.Center).padding(GridGutter),
                    )
                }
            }
        } else {
            item("saved") {
                val focusRequester = remember("watch-later") { FocusRequester() }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(GridPadding),
                    contentPadding = PaddingValues(vertical = GridPadding, horizontal = GridGutter),
                    modifier =
                        Modifier
                            .focusRestorer(focusRequester)
                            .onPreviewKeyEvent { event ->
                                val isCenterKey =
                                    event.key == Key.DirectionCenter ||
                                        event.key == Key.Enter ||
                                        event.key == Key.NumPadEnter
                                if (consumeNextCenterKeyUp.value && isCenterKey && event.type == KeyEventType.KeyUp) {
                                    consumeNextCenterKeyUp.value = false
                                    true
                                } else {
                                    false
                                }
                            },
                ) {
                    itemsIndexed(items, key = { _, lecture -> lecture.guid }) { index, lecture ->
                        LectureCard(
                            lecture = lecture.toLectureModel(),
                            navigate = navigate,
                            modifier = if (index == 0) Modifier.focusRequester(focusRequester) else Modifier,
                            isSaved = true,
                            onLongClick = {
                                consumeNextCenterKeyUp.value = true
                                viewModel.remove(lecture.guid)
                            },
                        )
                    }
                }
            }
        }
    }
}
