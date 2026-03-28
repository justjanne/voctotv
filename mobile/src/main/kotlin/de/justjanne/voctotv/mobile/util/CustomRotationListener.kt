package de.justjanne.voctotv.mobile.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.provider.Settings
import android.view.Surface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

internal class CustomRotationListener(
    private val context: Activity,
    private val scope: CoroutineScope,
) : ScreenOrientationListener(context) {
    private var job: Job? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onScreenOrientationChanged(orientation: Int) {
        job?.cancel()
        job = scope.launch {
            delay(500.milliseconds)
            autoUnlockRotation()
        }
    }

    override fun disable() {
        autoUnlockRotation()
        job?.cancel()
        super.disable()
    }

    private fun autoUnlockRotation() {
        val rotationLock =
            Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 0

        val userRotation =
            Settings.System.getInt(context.contentResolver, Settings.System.USER_ROTATION, -1)

        val lockedOrientation =
            when (context.requestedOrientation) {
                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> Surface.ROTATION_90
                ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT -> Surface.ROTATION_0
                else -> null
            }
        if (rotationLock) {
            if (lockedOrientation == userRotation || (lockedOrientation == Surface.ROTATION_90 && userRotation == Surface.ROTATION_270)) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        } else {
            if (lockedOrientation == orientation || (lockedOrientation == Surface.ROTATION_90 && orientation == Surface.ROTATION_270)) {
                context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
        }
    }
}
