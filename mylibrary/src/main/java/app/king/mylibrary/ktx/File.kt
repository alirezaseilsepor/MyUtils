/*
 * *
 *  * Created by Alireza Seilsepor on 4/5/20 4:20 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/5/20 4:20 AM
 *
 */
@file:Suppress("unused")
package app.king.mylibrary.ktx

import android.content.Context
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import java.net.URLEncoder


/**
 * converts a file to multipart request
 */
fun File.toMultipart(name: String): MultipartBody.Part? {
    val encoded: String?
    try {
        encoded = URLEncoder.encode(path, "UTF-8").replace("+", "%20")
    } catch (e: UnsupportedEncodingException) {
        return null
    }
    var type: String? = null
    val extension = MimeTypeMap.getFileExtensionFromUrl(encoded)
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    }
    val requestFile =
        asRequestBody("$type".toMediaTypeOrNull()!!)
    return MultipartBody.Part.createFormData(name, this.name, requestFile)
}
fun File.writeToFile(input: InputStream) {
    try {
        val out: OutputStream = FileOutputStream(this)
        val buf = ByteArray(1024)
        var len: Int
        while (input.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        out.close()
        input.close()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}