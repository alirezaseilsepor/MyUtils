package app.king.mylibrary.date

import net.time4j.PlainDate
import net.time4j.TemporalType
import net.time4j.calendar.PersianCalendar
import java.text.SimpleDateFormat
import java.util.*

fun PersianCalendar.toGregorian(): Date {
    val pd = transform(PlainDate::class.java)
    val dateFormatter = SimpleDateFormat("y-M-d", Locale.ENGLISH)
    val date = dateFormatter.parse(pd.toString())!!
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    return calendar.time
}

fun Date.toPersianCalendar(): PersianCalendar {
    val m = TemporalType.JAVA_UTIL_DATE.translate(this)
    val greg = m.toZonalTimestamp("UTC").toDate()
    return greg.transform(PersianCalendar::class.java)
}




