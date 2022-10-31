@file:Suppress("unused")
/*
 * *
 *  * Created by Alireza Seilsepor on 4/5/20 4:58 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/5/20 4:58 AM
 *
 */

package app.king.mylibrary.ktx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.net.toFile
import java.io.*
import java.util.*


fun createImageFile(): File {
    val storageDir = File(Environment.getDownloadCacheDirectory(), "Pictures")
    if (!storageDir.exists())
        storageDir.mkdirs()
    return File.createTempFile(
        "JPEG_${UUID.randomUUID()}_",
        ".jpg",
        storageDir
    ).apply {
        deleteOnExit()
    }
}


fun Uri.copyFile(context: Context?, file: File): File? {
    try {
        return toFile()
    } catch (e: Exception) {
        try {
            file.outputStream().use {
                context?.contentResolver?.openInputStream(this)?.copyTo(it)
                it.flush()
                it.close()
            }

            /* val inputStream = context?.contentResolver?.openInputStream(this)
             val outputStream = FileOutputStream(file)
             val buf = ByteArray(1024)
             var len: Int
             if (inputStream == null) return null
             while (run {
                     len = inputStream.read(buf)
                     len
                 } != -1) {
                 outputStream.write(buf, 0, len)
             }
             outputStream.flush()
             outputStream.close()
             inputStream.close()*/
            return file
        } catch (e: Exception) {
            return null
        }
    }
}


fun Drawable.toBitmap(): Bitmap? {
    if (this is BitmapDrawable) {
        val bitmapDrawable = this
        if (bitmapDrawable.bitmap != null) {
            return bitmapDrawable.bitmap.copy(Bitmap.Config.ARGB_8888, true)
        }
    }
    val bitmap = if (this.intrinsicWidth <= 0 || this.intrinsicHeight <= 0) {
        Bitmap.createBitmap(
            1,
            1,
            Bitmap.Config.ARGB_8888
        ) // Single color bitmap will be created of 1x1 pixel
    } else {
        Bitmap.createBitmap(
            this.intrinsicWidth,
            this.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
    }
    val canvas = Canvas(bitmap)
    this.setBounds(0, 0, canvas.width, canvas.height)
    this.draw(canvas)
    return bitmap.copy(Bitmap.Config.ARGB_8888, true)
}

fun Bitmap.toFile(
    application: Application,
    quality: Int = 100,
    name: String = System.nanoTime().toString(),
): File {
    val folder = File(application.getExternalFilesDir(null), "/IMAGE")
    if (folder.exists().not()) {
        folder.mkdir()
    }

    val filePath = "${folder.path}/Cover_${name}.jpg"
    val imageFile = File(filePath)
    val os: OutputStream
    try {
        os = FileOutputStream(imageFile)
        compress(Bitmap.CompressFormat.JPEG, quality, os)
        os.flush()
        os.close()
    } catch (e: java.lang.Exception) {

    }
    return imageFile
}


fun Bitmap.changeScale(newWidth: Int): Bitmap {
    var cache =copy(Bitmap.Config.ARGB_8888, true)
    val width = cache.width
    val height = cache.height
    val finalWidth= if (width < newWidth) width else newWidth
    val finalHeight = ((finalWidth.toFloat() / width) * height).toInt()
    cache= Bitmap.createScaledBitmap(cache,  newWidth, finalHeight, true)
    return cache.copy(Bitmap.Config.ARGB_8888, true)
}