package de.justjanne.voctotv.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.justjanne.voctotv.mobile.ui.AppRouter
import de.justjanne.voctotv.mobile.ui.theme.VoctoTvTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            VoctoTvTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppRouter()
                }
            }
        }
    }
}
