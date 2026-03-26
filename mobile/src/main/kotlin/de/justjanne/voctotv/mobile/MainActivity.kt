/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import de.justjanne.voctotv.mobile.route.AppRouter
import de.justjanne.voctotv.mobile.ui.theme.VoctoTvTheme
import de.justjanne.voctotv.voctoweb.deeplink.DeepLink

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )
        super.onCreate(savedInstanceState)

        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = false
        }

        val deepLink = handleLink(intent.data)
        Log.i("MainActivity", "Started with deep link: $deepLink")
        val startRoute =
            when (deepLink) {
                is DeepLink.Events.Show -> Routes.PlayerVod(deepLink.slug)
                is DeepLink.Conferences.Show -> Routes.Conference(deepLink.acronym)
                else -> null
            }
        Log.i("MainActivity", "Navigating to: $startRoute")

        setContent {
            VoctoTvTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppRouter(startRoute)
                }
            }
        }
    }

    fun handleLink(uri: Uri?): DeepLink? {
        if (uri == null) return null
        return DeepLink.match(
            uri.host ?: return null,
            uri.encodedPath ?: return null,
        )
    }
}
