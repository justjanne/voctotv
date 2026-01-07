package de.justjanne.voctotv.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun VoctoTvTheme(
    isInDarkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        if (isInDarkTheme) {
            darkColorScheme(
                primary = Purple80,
                secondary = PurpleGrey80,
                tertiary = Pink80,
            )
        } else {
            lightColorScheme(
                primary = Purple40,
                secondary = PurpleGrey40,
                tertiary = Pink40,
            )
        }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
