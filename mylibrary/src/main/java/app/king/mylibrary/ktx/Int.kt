@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.content.res.Resources
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.util.*

/**
 * converts seconds to HH:MM:SS
 */
fun Int.toHumanReadableHMSTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60
    val seconds = this % 60
    return if (hours != 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d", minutes, seconds)
    }
}

fun Int.toConvertMillisToString(): String? {
    val second = this / 1000 % 60
    val minute = this / (1000 * 60) % 60
    return java.lang.String.format(Locale.US, "%02d:%02d", minute, second)
}

fun humanReadableByteCountSI(bytes: Long): String {
    var byte = bytes
    if (-1000 < byte && byte < 1000) {
        return "$byte B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (byte <= -999950 || byte >= 999950) {
        byte /= 1000
        ci.next()
    }
    return java.lang.String.format(Locale.US, "%.1f %cB", byte / 1000.0, ci.current())
}

fun Int.withZeroNumber(): String = String.format(Locale.US, "%02d", this)


val Int.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Int.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Float.dp: Float
    get() = (this / Resources.getSystem().displayMetrics.density)
val Float.px: Float
    get() = (this * Resources.getSystem().displayMetrics.density)

val Double.dp: Float
    get() = (this.toFloat() / Resources.getSystem().displayMetrics.density)
val Double.px: Float
    get() = (this.toFloat() * Resources.getSystem().displayMetrics.density)

val Float.sp: Float get() = ((this / Resources.getSystem().displayMetrics.scaledDensity))
val Int.sp: Float get() = ((this / Resources.getSystem().displayMetrics.scaledDensity))

val Float.spToPx: Float get() = ((this * Resources.getSystem().displayMetrics.scaledDensity))
val Int.spToPx: Float get() = ((this * Resources.getSystem().displayMetrics.scaledDensity))