@file:Suppress("unused")

package app.king.mylibrary.ktx

import androidx.core.text.isDigitsOnly
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.regex.Pattern


fun getAndroidVersion(sdk: Int): String {
    return when (sdk) {
        1 -> "android 1.0"
        2 -> "android 1.1"
        3 -> "android 1.5"
        4 -> "android 1.6"
        5 -> "android 2.0"
        6 -> "android 2.0.1"
        7 -> "android 2.1"
        8 -> "android 2.2"
        9 -> "android 2.3"
        10 -> "android 2.3.3"
        11 -> "android 3.0"
        12 -> "android 3.1"
        13 -> "android 3.2"
        14 -> "android 4.0"
        15 -> "android 4.0.3"
        16 -> "android 4.1"
        17 -> "android 4.2"
        18 -> "android 4.3"
        19 -> "android 4.4"
        20 -> "android 4.4"
        21 -> "android 5.0"
        22 -> "android 5.1"
        23 -> "android 6.0"
        24 -> "android 7.0"
        25 -> "android 7.1.1"
        26 -> "android 8.0"
        27 -> "android 8.1"
        28 -> "android 9.0"
        29 -> "android 10.0"
        30 -> "android 11.0"
        else -> "android Unknown"
    }
}

/**
 * converts a string to multipart request
 */
fun Number.toPrice(): String {
    return DecimalFormat("#,###.##", DecimalFormatSymbols(Locale.US)).format(
       this
    )
}
fun String.extractNumberFromString(): List<Int> {
    val p = Pattern.compile("-?\\d+")
    val m = p.matcher(this)
    val numbs: MutableList<Int> = ArrayList()
    while (m.find()) {
        numbs.add(m.group().toInt())
    }
    return numbs
}
fun String.toNumber(): String {
    val num = StringBuffer()
    for (i in 0 until this.length) {
        if (Character.isDigit(this[i])) {
            num.append(this[i])
        }
    }
    return num.toString().trim()
}

fun String.redCodeInSms(): String {
    val pattern = Pattern.compile("(\\d{5})")
    val matcher = pattern.matcher(this)
    return if (matcher.find()) {
        matcher.group(1)?.toString() ?: ""
    } else {
        ""
    }
}


fun String.persianToEnglish(): String {
    var result = ""
    var en: Char
    for (ch in this) {
        en = ch
        when (ch) {
            ',' -> en = ' '
            '٫' -> en = ' '
            '.' -> en = '.'
            '۰' -> en = '0'
            '۱' -> en = '1'
            '۲' -> en = '2'
            '۳' -> en = '3'
            '۴' -> en = '4'
            '۵' -> en = '5'
            '۶' -> en = '6'
            '۷' -> en = '7'
            '۸' -> en = '8'
            '۹' -> en = '9'
        }
        result = "${result}$en"
    }
    return result.removeWhitespaces().trim()
}

fun String.removeWhitespaces() = replace(" ", "")


fun String.toMultipart(): RequestBody {
    return toRequestBody("text/plain".toMediaTypeOrNull())
}

fun String.toMultiPart(name: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name,
        this
    )
}

fun String.isNumber() = isDigitsOnly() && isNotEmpty()



