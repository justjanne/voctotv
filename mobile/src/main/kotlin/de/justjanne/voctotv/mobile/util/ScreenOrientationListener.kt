package de.justjanne.voctotv.mobile.util

import android.content.Context
import android.content.res.Configuration
import android.view.OrientationEventListener
import android.view.Surface

abstract class ScreenOrientationListener(
    context: Context,
) : OrientationEventListener(context) {
    var orientation = -1

    abstract fun onScreenOrientationChanged(orientation: Int)

    override fun onOrientationChanged(orientation: Int) {
        val proposedOrientation =
            when (orientation) {
                !in 31..<330 -> Surface.ROTATION_0
                in 60..120 -> Surface.ROTATION_90
                in 150..210 -> Surface.ROTATION_180
                in 240..300 -> Surface.ROTATION_270
                else -> -1
            }

        if (proposedOrientation != this.orientation) {
            this.orientation = proposedOrientation
            onScreenOrientationChanged(proposedOrientation)
        }
    }
}
