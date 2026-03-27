package de.justjanne.voctotv.mobile.route.player

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import de.justjanne.voctotv.mobile.R

@Composable
fun PlayerNavigationIcon(back: () -> Unit) {
    IconButton(
        onClick = { back() },
    ) {
        Icon(
            painterResource(R.drawable.ic_arrow_back),
            contentDescription = stringResource(R.string.action_back),
        )
    }
}
