package app.king.mylibrary.ktx

import java.util.*
import java.util.concurrent.TimeUnit

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