/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.justjanne.voctotv.mobile.Routes
import de.justjanne.voctotv.mobile.ui.AppRouter
import de.justjanne.voctotv.mobile.ui.theme.VoctoTvTheme
import android.util.Log
import android.net.Uri
import android.content.Intent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val route = handleLinks()

        setContent {
            VoctoTvTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppRouter(startRoute = route)
                }
            }
        }
    }

    // FIXME: change return type to Routes but I don't know kotlin
    fun handleLinks(): Routes.Player? {
        // Get the intent that started this activity
        val intent = intent
        val action = intent.action
        val data: Uri? = intent.data

        // Check if the activity was started with a VIEW action and a valid URI
        if (Intent.ACTION_VIEW == action && data != null) {
            // Extract information from the URI
            val link = data.toString()
            val host = data.host
            val path = data.path

            // Log or process the extracted information as needed
            Log.d("LinkHandlerActivity", "Link: $link")
            Log.d("LinkHandlerActivity", "Host: $host")
            Log.d("LinkHandlerActivity", "Path: $path")

            // check if is video
            // TODO: implement other link types (events, spaces, etc.)
            val re = Regex("(http|https)://([\\w.-]+)/v/(.*)")
            val matchResult = re.find(link)
            if (matchResult == null) {
                Log.e("LinkHandlerActivity", "Not a valid video link")
                return null
            }
            val videoId = matchResult.groupValues[3]
            Log.d("LinkHandlerActivity", "Video ID: $videoId")

            return Routes.Player(videoId)
        } else {
            Log.e("LinkHandlerActivity", "Invalid intent or data")
        }

        return null
    }
}
