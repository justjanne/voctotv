/*
 * Copyright (c) 2026. Janne Mareike Koschinski
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package de.justjanne.voctotv.common.previews

import android.graphics.Bitmap
import coil3.size.Size
import coil3.transform.Transformation

class SpritesheetTransformation(
    private val w: Int,
    private val h: Int,
    private val x: Int,
    private val y: Int,
) : Transformation() {
    override val cacheKey: String = "spritesheet-$w-$h-$x-$y"

    override suspend fun transform(
        input: Bitmap,
        size: Size,
    ): Bitmap {
        val cropped = Bitmap.createBitmap(input, x, y, w, h)
        input.recycle()
        return cropped
    }
}
