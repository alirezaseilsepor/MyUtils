package app.king.mylibrary.ktx

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

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