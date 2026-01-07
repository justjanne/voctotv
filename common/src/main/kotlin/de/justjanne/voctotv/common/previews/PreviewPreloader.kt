package de.justjanne.voctotv.common.previews

import android.content.Context
import coil3.imageLoader
import coil3.request.ImageRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreviewPreloader @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    fun preload(urls: List<TimedCue>) {
        val rawUrls = urls.map { it.data.newBuilder().removeAllQueryParameters("xywh").toString() }.distinct()
        for (url in rawUrls) {
            context.imageLoader.enqueue(ImageRequest.Builder(context).data(url).build())
        }
    }
}
