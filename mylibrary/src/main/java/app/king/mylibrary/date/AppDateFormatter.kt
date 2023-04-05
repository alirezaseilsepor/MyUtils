package app.king.mylibrary.date

import android.util.Log
import androidx.annotation.Keep
import app.king.mylibrary.ktx.withZeroNumber
import net.time4j.Moment
import net.time4j.PlainDate
import net.time4j.TemporalType
import net.time4j.calendar.PersianCalendar
import net.time4j.format.expert.ChronoFormatter
import net.time4j.format.expert.ParseLog
import net.time4j.format.expert.PatternType
import net.time4j.tz.Timezone
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
class AppDateFormatter {

    companion object {
        private const val LOG = "AppDateFormatter"
        const val YEAR = 'y'
        const val MONTH = 'm'
        const val DAY = 'd'
        const val DAY_N = 'p'
        const val DAY_NONE_ZERO = 'j'
        const val MONTH_N = 'G'
        const val HOUR = 'h'
        const val MINUTES = 'M'
        const val SECOND = 's'
        const val MILLI = 'S'
        const val SERVER_GEORGIAN_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSS"
        const val SERVER_GEORGIAN_DATE_WITHOUT_TIME = "yyyy-MM-dd"
        private const val PATTERN_COMPLETE_DATE = "uuuu-M-d'T'HH[:mm][:ss][.SSS]"
        private const val PATTERN_GEORGIAN_DATE = "uuuu/M/d|uuuu.M.d"
        private const val PATTERN_PERSIAN_DATE = "yyyy/M/d|yyyy.M.d"
        private const val PATTERN_HIJRI_DATE = "yyyy/M/d|yyyy.M.d"
    }

    /**
    parse by CalendarType
     */
    fun parse(
        dateString: String,
        isEnableUtc: Boolean = false,
        generalCalendarType: GeneralCalendar.CalendarType = GeneralCalendar.GeneralCalendarType,
    ): Date? {
        val result = runCatching {
            if (dateString.length > 10)
                parsCompleteDate(dateString, isEnableUtc)
            else {
                when (generalCalendarType) {
                    GeneralCalendar.CalendarType.GEORGIAN -> {
                        return parsGeorgianDate(dateString)
                    }
                    GeneralCalendar.CalendarType.PERSIAN -> {
                        return parsPersianDate(dateString)
                    }
                     /*GeneralCalendar.CalendarType.HIJRI -> {
                         return parsHijriDate(dateString)
                     }*/
                }
            }
        }.onFailure {
            Log.e(LOG, "ERROR MESSAGE= convert $dateString ${it.message}")
            return null
        }
        if (result.isSuccess)
            Log.e(LOG, "Success MESSAGE= convert $dateString")

        return result.getOrNull()
    }


    /**
    Example= 2022/2/17 or 2022/02/17 or 2022-02-17 or 2022.2.17
     */
    @Throws(ParseException::class)
    fun parsGeorgianDate(dateString: String): Date? {
        var cacheDateString = dateString.replace("-", "/")
        cacheDateString = cacheDateString.replace(",", "/")
        cacheDateString = cacheDateString.replace(" ", "/")
        val formatter = ChronoFormatter.setUp(PlainDate.axis(), Locale.ENGLISH)
            .addPattern(PATTERN_GEORGIAN_DATE,
                PatternType.CLDR)
            .addIgnorableWhitespace()
            .build()

        val pLog = ParseLog()

        val plainDate = formatter.parse(cacheDateString, pLog)

        return if (plainDate == null || pLog.isError) {
            Log.e(LOG, "ERROR MESSAGE= convert $dateString ${pLog.errorMessage}")
            null
        } else {
            val date = TemporalType.JAVA_UTIL_DATE.from(plainDate.atFirstMoment("UTC"))
            val calendar = Calendar.getInstance()
            calendar.time = GeneralCalendar.getInstance().getGeorgianDate()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.time
        }
    }


    /**
    Example= 1401/2/17 or 1401/02/17 or 1401-02-17 or 1401.2.17
     */
    @Throws(ParseException::class)
    fun parsPersianDate(dateString: String): Date? {
        val cacheDateString = dateString.replace("-", "/")
        val formatter = ChronoFormatter.setUp(PersianCalendar.axis(), Locale.ENGLISH)
            .addPattern(PATTERN_PERSIAN_DATE,
                PatternType.CLDR)
            .addIgnorableWhitespace()
            .build()
            .withTimezone(Timezone.of("UTC").id)
        val pLog = ParseLog()

        val date = formatter.parse(cacheDateString, pLog)

        return if (date == null || pLog.isError) {
            Log.e(LOG, "ERROR MESSAGE= convert PersianDate $dateString ${pLog.errorMessage}")
            null
        } else {
            date.toGregorian()
        }
    }


    /**
    Example= 1444/2/17 or 1444/02/17 or 1444-02-17 or 1444.2.17
     */
    /*@Throws(ParseException::class)
    fun parsHijriDate(dateString: String): GeneralCalendar? {
        val cacheDateString = dateString.replace("-", "/")
        val formatter = ChronoFormatter.setUp(HijriCalendar::class.java, Locale.ENGLISH)
            .addPattern(PATTERN_HIJRI_DATE,
                PatternType.CLDR)
            .addIgnorableWhitespace()
            .build()
            .withCalendarVariant(HijriCalendar.VARIANT_UMALQURA)

        val pLog = ParseLog()

        val date = formatter.parse(cacheDateString, pLog)

        return if (date == null || pLog.isError) {
            Log.e(LOG, "ERROR MESSAGE= convert HijriDate $dateString ${pLog.errorMessage}")
            null
        } else {
            GeneralCalendar(date)
        }
    }*/

    /**
    just parse Georgian Date
    Example=
    2022-09-13T10:13:46.786 or
    2012-07-01T01:59:22.123Z or
    2014-06-12T23:59:22 or
    2012-07-01T05:29:21.123GMT+5:30
     */
    @Throws(ParseException::class)
    fun parsCompleteDate(dateString: String, isEnableUtc: Boolean = false): Date? {
        if (isEnableUtc) {
            var standardDate = dateString
            if (!dateString.contains(".")) {
                standardDate = "$standardDate.000"
            }
            standardDate = standardDate.lowercase(Locale.ENGLISH).replace("z", "")

            val formatter =
                ChronoFormatter.setUp(Moment.axis(TemporalType.JAVA_UTIL_DATE), Locale.ENGLISH)
                    .addPattern(PATTERN_COMPLETE_DATE,
                        PatternType.CLDR)
                    .addIgnorableWhitespace()
                    .build()

            val pLog = ParseLog()
            val date = formatter.parse(standardDate, pLog)
            return if (date == null || pLog.isError) {
                Log.e(LOG, "ERROR MESSAGE= convert CompleteDate $standardDate ${pLog.errorMessage}")
                null
            } else {
               date
            }
        } else {
            val dateFormat = SimpleDateFormat(SERVER_GEORGIAN_DATE, Locale.ENGLISH)
            return dateFormat.parse(dateString)!!
        }
    }


    /**
    format global generalCalendar to String
     */
    @Throws(ParseException::class)
    fun format(
        generalCalendar: GeneralCalendar,
        pattern: DateFormat = DateFormat.DEFAULT,
        type: GeneralCalendar.CalendarType = GeneralCalendar.GeneralCalendarType,
    ): String {
        return when (type) {
            GeneralCalendar.CalendarType.GEORGIAN -> {
                formatGeorgianDate(generalCalendar, pattern)
            }
            GeneralCalendar.CalendarType.PERSIAN -> {
                formatPersianDate(generalCalendar, pattern)
            }
            /*   GeneralCalendar.CalendarType.HIJRI -> {
                   formatHijriDate(generalCalendar, pattern)
               }*/
        }
    }


    /**
    format PersianCalendar to String
     */
    @Throws(ParseException::class)
    fun formatGeorgianDate(
        generalCalendar: GeneralCalendar,
        pattern: DateFormat = DateFormat.DEFAULT,
    ): String {
        val calendar = Calendar.getInstance()
        calendar.time = generalCalendar.getGeorgianDate()
        var result = ""

        pattern.format.forEach { ch ->
            val data: String
            when (ch) {
                YEAR -> {
                    data = generalCalendar.getGeorgianYear().toString()
                }
                MONTH -> {
                    data = generalCalendar.getGeorgianMonth().withZeroNumber()
                }
                MONTH_N -> {
                    data = generalCalendar.getGeorgianMonthName()
                }
                DAY -> {
                    data = generalCalendar.getGeorgianDay().withZeroNumber()
                }
                DAY_N -> {
                    data = generalCalendar.getDayName(GeneralCalendar.CalendarType.GEORGIAN)
                }
                DAY_NONE_ZERO -> {
                    data = generalCalendar.getGeorgianDay().toString()
                }
                HOUR -> {
                    data = calendar.get(Calendar.HOUR_OF_DAY).withZeroNumber()
                }
                MINUTES -> {
                    data = calendar.get(Calendar.MINUTE).withZeroNumber()
                }
                SECOND -> {
                    data = calendar.get(Calendar.SECOND).withZeroNumber()
                }
                MILLI -> {
                    data = calendar.get(Calendar.MILLISECOND).withZeroNumber()
                }
                else -> {
                    data = ch.toString()
                }
            }
            result += data
        }
        return result
    }


    /**
    format PersianCalendar to String
     */
    @Throws(ParseException::class)
    fun formatPersianDate(
        generalCalendar: GeneralCalendar,
        pattern: DateFormat = DateFormat.DEFAULT,
    ): String {
        val calendar = Calendar.getInstance()
        calendar.time = generalCalendar.getGeorgianDate()
        var result = ""
        val persianCalendar = generalCalendar.getPersianCalendar()

        pattern.format.forEach { ch ->
            val data: String
            when (ch) {
                YEAR -> {
                    data = persianCalendar.year.toString()
                }
                MONTH -> {
                    data = persianCalendar.month.value.withZeroNumber()
                }
                MONTH_N -> {
                    data = persianCalendar.month.getDisplayName(Locale("fa"))
                }
                DAY -> {
                    data = persianCalendar.dayOfMonth.withZeroNumber()
                }
                DAY_N -> {
                    data = generalCalendar.getDayName(GeneralCalendar.CalendarType.PERSIAN)
                }
                DAY_NONE_ZERO -> {
                    data = persianCalendar.dayOfMonth.toString()
                }
                HOUR -> {
                    data = calendar.get(Calendar.HOUR_OF_DAY).withZeroNumber()
                }
                MINUTES -> {
                    data = calendar.get(Calendar.MINUTE).withZeroNumber()
                }
                SECOND -> {
                    data = calendar.get(Calendar.SECOND).withZeroNumber()
                }
                MILLI -> {
                    data = calendar.get(Calendar.MILLISECOND).withZeroNumber()
                }
                else -> {
                    data = ch.toString()
                }
            }
            result += data
        }
        return result
    }


    /**
    format HijriCalendar to String
     */
    /*  @Throws(ParseException::class)
      fun formatHijriDate(
          generalCalendar: GeneralCalendar,
          pattern: DateFormat = DateFormat.DEFAULT,
      ): String {
          val calendar = Calendar.getInstance()
          calendar.time = generalCalendar.getGeorgianDate()
          var result = ""
          val hijriCalendar = generalCalendar.getHijriCalendar()

          pattern.format.forEach { ch ->
              val data: String
              when (ch) {
                  YEAR -> {
                      data = hijriCalendar.year.toString()
                  }
                  MONTH -> {
                      data = hijriCalendar.month.value.withZeroNumber()
                  }
                  MONTH_N -> {
                      data = hijriCalendar.month.getDisplayName(Locale("fa"))
                  }
                  DAY -> {
                      data = hijriCalendar.dayOfMonth.withZeroNumber()
                  }
                  DAY_N -> {
                      data = generalCalendar.getDayName(GeneralCalendar.CalendarType.HIJRI)
                  }
                  DAY_NONE_ZERO -> {
                      data = hijriCalendar.dayOfMonth.toString()
                  }
                  HOUR -> {
                      data = calendar.get(Calendar.HOUR_OF_DAY).withZeroNumber()
                  }
                  MINUTES -> {
                      data = calendar.get(Calendar.MINUTE).withZeroNumber()
                  }
                  SECOND -> {
                      data = calendar.get(Calendar.SECOND).withZeroNumber()
                  }
                  MILLI -> {
                      data = calendar.get(Calendar.MILLISECOND).withZeroNumber()
                  }
                  else -> {
                      data = ch.toString()
                  }
              }
              result += data
          }
          return result
      }
  */

    fun getDateForServer(
        generalCalendar: GeneralCalendar,
        isEnableUtc: Boolean = false,
        isEnableTime: Boolean = true,
    ): String {
        val format = if (isEnableTime) SERVER_GEORGIAN_DATE else SERVER_GEORGIAN_DATE_WITHOUT_TIME
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        if (isEnableUtc)
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(generalCalendar.getGeorgianDate())
    }
}

@Suppress("unused")
@Keep
enum class DateFormat(val format: String) {
    DEFAULT("${AppDateFormatter.YEAR}/${AppDateFormatter.MONTH}/${AppDateFormatter.DAY}"),
    DEFAULT_CLOCK("${AppDateFormatter.YEAR}/${AppDateFormatter.MONTH}/${AppDateFormatter.DAY} ${AppDateFormatter.HOUR}:${AppDateFormatter.MINUTES}"),
    LINE("${AppDateFormatter.YEAR}-${AppDateFormatter.MONTH}-${AppDateFormatter.DAY}"),
    MONTH_NAME_FULL("${AppDateFormatter.DAY_NONE_ZERO} ${AppDateFormatter.MONTH_N} ${AppDateFormatter.YEAR}"),
    MONTH_NAME_FULL_REVERSE("${AppDateFormatter.YEAR} ${AppDateFormatter.MONTH_N} ${AppDateFormatter.DAY_NONE_ZERO}"),
    MONTH_NAME_YEAR("${AppDateFormatter.YEAR} ${AppDateFormatter.MONTH_N}"),
    MONTH_NAME_DAY("${AppDateFormatter.DAY_NONE_ZERO} ${AppDateFormatter.MONTH_N}"),
    CLOCK("${AppDateFormatter.YEAR}-${AppDateFormatter.MONTH}-${AppDateFormatter.DAY}:${AppDateFormatter.HOUR}:${AppDateFormatter.MINUTES}"),
    CLOCK_SECOND("${AppDateFormatter.YEAR}-${AppDateFormatter.MONTH}-${AppDateFormatter.DAY}:${AppDateFormatter.HOUR}:${AppDateFormatter.MINUTES}:${AppDateFormatter.SECOND}"),
    CLOCK_MILLI("${AppDateFormatter.YEAR}-${AppDateFormatter.MONTH}-${AppDateFormatter.DAY}:${AppDateFormatter.HOUR}:${AppDateFormatter.MINUTES}:${AppDateFormatter.SECOND}.${AppDateFormatter.MILLI}"),
    DAY_NAME("${AppDateFormatter.DAY_N} ${AppDateFormatter.DAY} ${AppDateFormatter.MONTH_N} ${AppDateFormatter.YEAR}"),
    DAY_NAME_NONE_ZERO("${AppDateFormatter.DAY_N} ${AppDateFormatter.DAY_NONE_ZERO} ${AppDateFormatter.MONTH_N} ${AppDateFormatter.YEAR}"),
    DAY_NAME_NONE_YEAR("${AppDateFormatter.DAY_N} ${AppDateFormatter.DAY_NONE_ZERO} ${AppDateFormatter.MONTH_N}"),
}


/*  val input = arrayOf(
         "2015-03-29T03:30+02:00",
         "2019-05-11T03:30+02:00",
         "2012-07-01T00:00:34",
         "2012-07-01",
         "2012-06-30T23:59:33,123000000Z",
         "2014-06-12T23:59:22",
         "2014-06-12",
         "2014/06/12",
         "1400-06-02",
         "2014-06-12T",
         "2012-07-01T05:29:21.123GMT+5:30",
         "2012-06-30T23:59:48.123",
         "2014-2-7T23:59:48.123",
         "2012-07-01T01:59:22.123Z",
         GeneralCalendar.getInstance().getDateForServer())

     val inputF = arrayOf(
         "1400-06-02",
         "1400-01-07",
         "1400-1-7",
         "1400/06/02",
         "1400/2/4",
         "1400.06.02",
         "1400.1.8",
         GeneralCalendar.getInstance().toString())


     val gc=GeneralCalendar.getInstance()

     val inputH = arrayOf("${gc.getHijriYear()}/${gc.getHijriMonth()}/${gc.getHijriDay()}")

     val inputG= arrayOf(
         "${gc.getGeorgianYear()}/${gc.getGeorgianMonth()}/${gc.getGeorgianDay()}")*/