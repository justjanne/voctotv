package de.justjanne.voctotv.mobile.ui.util

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

data class SystemBarState(
    private val context: Activity,
) {
    fun show() {
        WindowCompat
            .getInsetsController(context.window, context.window.decorView)
            .show(WindowInsetsCompat.Type.systemBars())
    }
    fun hide() {
        WindowCompat
            .getInsetsController(context.window, context.window.decorView)
            .hide(WindowInsetsCompat.Type.systemBars())
    }
}

@Composable
fun rememberSystemBarState(): SystemBarState? {
    val context = LocalActivity.current
    return remember(context) { context?.let(::SystemBarState) }
}
