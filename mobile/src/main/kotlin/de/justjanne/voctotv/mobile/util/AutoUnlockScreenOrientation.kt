package de.justjanne.voctotv.mobile.util

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

private class AutoUnlockScreenOrientationListener(
    private val context: Activity,
    private val scope: CoroutineScope,
) : ScreenOrientationListener(context) {
    private var job: Job? = null

    override fun onScreenOrientationChanged(orientation: Int) {
        val lockedOrientation = when (context.requestedOrientation) {
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE -> Configuration.ORIENTATION_LANDSCAPE
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT -> Configuration.ORIENTATION_PORTRAIT
            else -> null
        }

        job?.cancel()
        job = scope.launch {
            delay(500.milliseconds)
            if (orientation != Configuration.ORIENTATION_UNDEFINED && orientation != lockedOrientation) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }

    override fun disable() {
        job?.cancel()
        super.disable()
    }
}

@Composable
fun AutoUnlockScreenOrientation() {
    val context = LocalActivity.current ?: return

    val scope = rememberCoroutineScope()
    DisposableEffect(context) {
        val listener = AutoUnlockScreenOrientationListener(context, scope)
        listener.enable()

        onDispose {
            listener.disable()
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}
