/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.previews

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil3.Image
import coil3.request.ImageRequest
import okhttp3.HttpUrl

@Composable
fun rememberPreviewThumbnail(url: State<HttpUrl?>): State<ImageRequest?> {
    val context = LocalContext.current
    val previousThumbnail = remember { mutableStateOf<Image?>(null) }
    return remember(context) {
        derivedStateOf {
            url.value?.let { url ->
                try {
                    ImageRequest
                        .Builder(context)
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
