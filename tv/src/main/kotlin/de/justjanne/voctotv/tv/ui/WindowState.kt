package de.justjanne.voctotv.tv.ui

import android.app.Activity
import android.view.WindowManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

data class WindowState(
    private val context: Activity,
) {
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
