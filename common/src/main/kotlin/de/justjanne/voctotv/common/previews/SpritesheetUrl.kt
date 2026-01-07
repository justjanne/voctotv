/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.previews

import coil3.request.ImageRequest
import coil3.request.transformations
import coil3.size.SizeResolver
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
