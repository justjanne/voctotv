package de.justjanne.voctotv.mobile.ui.util

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat

data class WindowState(
    private val context: Activity,
) {
    fun showSystemUi() {
        WindowCompat
            .getInsetsController(context.window, context.window.decorView)
            .show(WindowInsetsCompat.Type.systemBars())
    }
    fun hideSystemUi() {
        WindowCompat
            .getInsetsController(context.window, context.window.decorView)
            .hide(WindowInsetsCompat.Type.systemBars())
    }

    fun disableSleep() {
        context.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    fun enableSleep() {
        context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
fun rememberWindowState(): WindowState? {
    val context = LocalActivity.current
    return remember(context) { context?.let(::WindowState) }
}
