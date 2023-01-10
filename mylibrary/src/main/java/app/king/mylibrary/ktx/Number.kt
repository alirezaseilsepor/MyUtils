@file:Suppress("unused")

package app.king.mylibrary.ktx

import android.content.res.Resources
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.time.Duration.Companion.milliseconds

fun Float.toExactDouble(): Double =
    this.toString().toDouble()

fun Long.toDigitalClockTime(showHour: Boolean = true): String {
    val milliSeconds = this
    val hours = TimeUnit.MILLISECONDS.toHours(milliSeconds).toInt() % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliSeconds).toInt() % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliSeconds).toInt() % 60
    val prefixHour = if (showHour) "00:" else ""
    return when {
        hours > 0 -> String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format(Locale.US, "${prefixHour}%02d:%02d", minutes, seconds)
        seconds > 0 -> String.format(Locale.US, "${prefixHour}00:%02d", seconds)
        else -> {
            "${prefixHour}00:00"
        }
    }
}

fun Long?.getPrettyFormatForNumbers(): String {
    if (this == null)
        return "0"
    val x = abs(this.toDouble())
    var label = ""
    val value: Double
    val thousand = 1000.0
    val million = 1000000.0
    val billion = 1000000000.0
    when {
        x < thousand -> value = x
        x < million -> {
            value = x / thousand
            label = " K"
        }
        x < billion -> {
            value = x / million
            label = " M"
        }
        else -> {
            value = x / billion
            label = " B"
        }
    }
    var result = "$value$label"
    val split = "$value".split(".")
    if (split[1].trim().toInt() == 0) {
        result = result.replace(".0", "")
    }
    return result
}


/**
 * converts seconds to HH:MM:SS
 */

fun Number.toHumanTime(): String {
    this.toLong().milliseconds.toComponents { hours, minutes, seconds, _ ->
        return if (hours == 0L)
            "%02d:%02d".format(Locale.ENGLISH, minutes, seconds)
        else
            "%02d:%02d:%02d".format(Locale.ENGLISH, hours, minutes, seconds)
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

fun Number.withZeroNumber(): String = String.format(Locale.US, "%02d", this)


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