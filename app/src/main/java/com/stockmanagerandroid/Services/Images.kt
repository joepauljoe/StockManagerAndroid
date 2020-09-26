package com.stockmanagerandroid.Services

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import java.io.ByteArrayInputStream

object Images {

    fun rotateBitmap(data: ByteArray?, bitmap: Bitmap): Bitmap {
        // Find out if the picture needs rotating by looking at its Exif data
        val exifInterface =
            androidx.exifinterface.media.ExifInterface(ByteArrayInputStream(data))
        val orientation: Int = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1)
        var rotationDegrees = 0
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotationDegrees = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotationDegrees = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotationDegrees = 270
        }
        // Create and rotate the bitmap by rotationDegrees

        if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            return bitmap
        }
    }
}