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
