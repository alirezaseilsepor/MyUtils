package app.king.mylibrary.date

import net.time4j.PlainDate
import net.time4j.TemporalType
import net.time4j.calendar.HijriCalendar
import net.time4j.calendar.HijriCalendar.VARIANT_UMALQURA
import net.time4j.calendar.PersianCalendar
import net.time4j.tz.TZID
import net.time4j.tz.olson.ASIA
import java.util.*

fun PersianCalendar.toGregorian(): Date {
    val tzid: TZID = ASIA.TEHRAN
    val pd = transform(PlainDate::class.java)
    return TemporalType.JAVA_UTIL_DATE.from(pd.atStartOfDay().inTimezone(tzid))
}

fun HijriCalendar.toGregorian(): Date {
    val pd = transform(PlainDate::class.java)
    return TemporalType.JAVA_UTIL_DATE.from(pd.atStartOfDay().inStdTimezone())
}

fun Date.toHijriCalendar(): HijriCalendar {
    val m = TemporalType.MILLIS_SINCE_UNIX.translate(time)
    val greg = m.toLocalTimestamp().toDate()
    return greg.transform(HijriCalendar::class.java, VARIANT_UMALQURA)
}

fun Date.toPersianCalendar(): PersianCalendar {
    val m = TemporalType.MILLIS_SINCE_UNIX.translate(time)
    val greg = m.toZonalTimestamp(ASIA.TEHRAN).toDate()
    return greg.transform(PersianCalendar::class.java)
}

fun PersianCalendar.toHijriCalendar(): HijriCalendar {
    return toGregorian().toHijriCalendar()
}

fun HijriCalendar.toPersianCalendar(): PersianCalendar {
    return toGregorian().toPersianCalendar()
}

fun getTimeLocal(action: (Int, Int, Int) -> Unit) {
    val calendar = Calendar.getInstance()
    action(calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE], calendar[Calendar.SECOND])
}




