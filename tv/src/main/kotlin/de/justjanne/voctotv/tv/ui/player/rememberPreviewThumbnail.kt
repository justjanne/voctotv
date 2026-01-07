package de.justjanne.voctotv.tv.ui.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil3.Image
import coil3.request.ImageRequest
import de.justjanne.voctotv.mobile.util.spritesheetUrl
import okhttp3.HttpUrl

@Composable
fun rememberPreviewThumbnail(url: State<HttpUrl?>): State<ImageRequest?> {
    val context = LocalContext.current
    val previousThumbnail = remember { mutableStateOf<Image?>(null) }
    return remember(context) {
        derivedStateOf {
            url.value?.let { url ->
                try {
                    ImageRequest.Builder(context)
                        .spritesheetUrl(url)
                        .placeholder(previousThumbnail.value)
                        .listener(onSuccess = { _, result ->
                            previousThumbnail.value = result.image
                        })
                        .build()
                } catch (e: Throwable) {
                    e.printStackTrace()
                    null
                }
            }
        }
    }
}
