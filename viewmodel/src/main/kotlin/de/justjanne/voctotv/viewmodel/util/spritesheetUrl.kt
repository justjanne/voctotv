package de.justjanne.voctotv.mobile.util

import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.SizeResolver
import de.justjanne.voctotv.viewmodel.util.SpritesheetTransformation
import okhttp3.HttpUrl

fun ImageRequest.Builder.spritesheetUrl(url: HttpUrl): ImageRequest.Builder {
    val fragment = url.queryParameter("xywh")

    if (fragment == null) {
        return this
            .data(url.toString())
            .size(SizeResolver.ORIGINAL)
    } else {
        val (x, y, w, h) = fragment.split(',')
        val cleanUrl = url.newBuilder().removeAllQueryParameters("xywh").toString()
        return this
            .data(cleanUrl)
            .size(SizeResolver.ORIGINAL)
            .transformations(SpritesheetTransformation(w.toInt(), h.toInt(), x.toInt(), y.toInt()))
    }
}
