package de.justjanne.voctotv.mobile.util

import android.content.Context
import android.content.res.Configuration
import android.view.OrientationEventListener

abstract class ScreenOrientationListener(
    context: Context,
) : OrientationEventListener(context) {
    private var previousValue = -1

    abstract fun onScreenOrientationChanged(orientation: Int)

    override fun onOrientationChanged(orientation: Int) {
        val proposedOrientation =
            when (orientation) {
                !in 31..<330 -> Configuration.ORIENTATION_PORTRAIT
                in 60..120 -> Configuration.ORIENTATION_LANDSCAPE
                in 150..210 -> Configuration.ORIENTATION_PORTRAIT
                in 240..300 -> Configuration.ORIENTATION_LANDSCAPE
                else -> Configuration.ORIENTATION_UNDEFINED
            }

        if (proposedOrientation != previousValue) {
            previousValue = proposedOrientation
            onScreenOrientationChanged(proposedOrientation)
        }
    }
}
