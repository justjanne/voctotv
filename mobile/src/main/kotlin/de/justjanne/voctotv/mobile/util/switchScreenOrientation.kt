package de.justjanne.voctotv.mobile.util

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration

fun Activity?.switchScreenOrientation(orientation: Int) {
    this?.requestedOrientation = when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT
        Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
}
