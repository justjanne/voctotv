package de.justjanne.voctotv.mobile.ui.dropdown

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun DropdownMenuRadioItem(
    text: @Composable () -> Unit,
    selected: Boolean,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onClick: () -> Unit = {},
) {
    DropdownMenuItem(
        text = text,
        trailingIcon = {
            RadioButton(
                selected,
                interactionSource = interactionSource,
                onClick = onClick,
            )
        },
        interactionSource = interactionSource,
        onClick = onClick,
    )
}
