package de.justjanne.voctotv.tv.ui.dropdown

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ProvideTextStyle

@Composable
fun DropdownMenuSubheader(text: @Composable () -> Unit) {
    Row(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProvideTextStyle(
            MaterialTheme.typography.titleMedium,
            content = text,
        )
    }
}
