package de.justjanne.voctotv.mobile.util

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun CustomRotationController() {
    val context = LocalActivity.current ?: return

    val scope = rememberCoroutineScope()
    DisposableEffect(context, LocalConfiguration.current) {
        val listener = CustomRotationListener(context, scope)
        listener.enable()

        onDispose {
            listener.disable()
        }
    }
}
