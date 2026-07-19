package de.justjanne.voctotv.mobile.route.watchlater

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import de.justjanne.voctotv.common.viewmodel.WatchLaterViewModel
import de.justjanne.voctotv.common.watchlater.toLectureModel
import de.justjanne.voctotv.mobile.R
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.LectureItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchLaterRoute(
    viewModel: WatchLaterViewModel,
    navigate: (NavKey) -> Unit,
    back: () -> Unit,
) {
    val items = viewModel.items.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.action_watch_later)) },
                navigationIcon = {
                    IconButton(onClick = back) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.action_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(R.string.watch_later_empty),
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        } else {
            LazyColumn(contentPadding = contentPadding, modifier = Modifier.fillMaxSize()) {
                items(items, key = { it.guid }) { item ->
                    LectureItem(
                        item = item.toLectureModel(),
                        isSaved = true,
                        onClick = { navigate(Routes.PlayerVod(item.guid)) },
                        onLongClick = { viewModel.remove(item.guid) },
                    )
                }
            }
        }
    }
}
