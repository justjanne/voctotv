package de.justjanne.voctotv.tv.ui.dropdown

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.tv.material3.ListItem
import androidx.tv.material3.RadioButton

@Composable
fun DropdownMenuRadioItem(
    text: @Composable () -> Unit,
    selected: Boolean,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    focusRequester: FocusRequester = remember { FocusRequester() },
    onClick: () -> Unit = {},
) {
    ListItem(
        selected = selected,
        onClick = onClick,
        headlineContent = text,
        trailingContent = {
            RadioButton(
                selected,
                interactionSource = interactionSource,
                onClick = onClick,
            )
        },
        interactionSource = interactionSource,
        modifier = Modifier.focusRequester(focusRequester),
    )

    LaunchedEffect(focusRequester, selected) {
        if (selected) {
            focusRequester.requestFocus()
        }
    }
}
