package app.king.mylibrary.date


import androidx.annotation.Keep
import net.time4j.PlainDate
import net.time4j.Weekday
import net.time4j.calendar.HijriCalendar
import net.time4j.calendar.PersianCalendar
import net.time4j.engine.CalendarDays
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
class GeneralCalendar private constructor() {

    private val appDateFormatter = AppDateFormatter()

    @Keep
    enum class CalendarType(val type: Int) {
        GEORGIAN(0),
        HIJRI(1),
        PERSIAN(2),
    }

    private lateinit var grgDate: Date
    private lateinit var persianDate: PersianCalendar
    private lateinit var hijriDate: HijriCalendar


    constructor(grgDate: Date) : this() {
        this.grgDate = grgDate
        this.persianDate = grgDate.toPersianCalendar()
        this.hijriDate = grgDate.toHijriCalendar()
    }

    constructor(persianDate: PersianCalendar) : this() {
        this.grgDate = persianDate.toGregorian()
        this.persianDate = persianDate
        this.hijriDate = persianDate.toHijriCalendar()
    }

    constructor(hijriDate: HijriCalendar) : this() {
        this.grgDate = hijriDate.toGregorian()
        this.persianDate = hijriDate.toPersianCalendar()
        this.hijriDate = hijriDate
    }

    constructor(dateString: String) : this() {
        val gc = appDateFormatter.parse(dateString)!!
        this.grgDate = gc.grgDate
        this.persianDate = gc.persianDate
        this.hijriDate = gc.hijriDate
    }

    constructor(
        day: Int,
        month: Int,
        year: Int,
        type: CalendarType = GeneralCalendarType,
    ) : this() {
        when (type) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DAY_OF_MONTH, day)
                calendar.set(Calendar.MONTH, month - 1)
                calendar.set(Calendar.YEAR, year)
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                val calendar = HijriCalendar.of(HijriCalendar.VARIANT_UMALQURA, year, month, day)
                this.grgDate = calendar.toGregorian()
                this.persianDate = calendar.toPersianCalendar()
                this.hijriDate = calendar
            }
            CalendarType.PERSIAN -> {
                val calendar = PersianCalendar.of(year, month, day)
                this.grgDate = calendar.toGregorian()
                this.persianDate = calendar
                this.hijriDate = calendar.toHijriCalendar()
            }
        }
        /* this.grgDate = hijriDate.toGregorian()
         this.persianDate = hijriDate.toPersianCalendar()
         this.hijriDate = hijriDate*/
    }

    fun getYear(): Int {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.get(Calendar.YEAR)
            }
            CalendarType.HIJRI -> {
                hijriDate.year
            }
            CalendarType.PERSIAN -> {
                persianDate.year
            }
        }
    }

    fun getGeorgianYear(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = grgDate
        return calendar.get(Calendar.YEAR)
    }

    fun getMonth(): Int {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                getGeorgianMonth()
            }
            CalendarType.HIJRI -> {
                hijriDate.month.value
            }
            CalendarType.PERSIAN -> {
                persianDate.month.value
            }
        }
    }

    fun getGeorgianMonth(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = grgDate
        return calendar.get(Calendar.MONTH) + 1
    }

    fun getDay(): Int {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.get(Calendar.DAY_OF_MONTH)
            }
            CalendarType.HIJRI -> {
                hijriDate.dayOfMonth
            }
            CalendarType.PERSIAN -> {
                persianDate.dayOfMonth
            }
        }
    }

    fun getDayName(): String {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                persianDate.dayOfWeek.getDisplayName(Locale.ENGLISH)
            }
            CalendarType.HIJRI -> {
                hijriDate.dayOfWeek.getDisplayName(Locale("ar"))
            }
            CalendarType.PERSIAN -> {
                persianDate.dayOfWeek.getDisplayName(Locale("fa"))
            }
        }
    }

    fun getGeorgianDay(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = grgDate
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getHijriDay(): Int {
        return hijriDate.dayOfMonth
    }

    fun getHijriMonth(): Int {
        return hijriDate.month.value
    }

    fun getHijriYear(): Int {
        return hijriDate.year
    }

    fun getLengthsOfMonths(): Int {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val plainDate = PlainDate.of(year, month + 1, 1)
                plainDate.lengthOfMonth()
            }
            CalendarType.HIJRI -> {
                hijriDate.lengthOfMonth()
            }
            CalendarType.PERSIAN -> {
                persianDate.lengthOfMonth()
            }
        }
    }

    fun getDayOfWeek(): Int {

        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN, CalendarType.HIJRI -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                when (calendar.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> {
                        1
                    }
                    Calendar.TUESDAY -> {
                        2
                    }
                    Calendar.WEDNESDAY -> {
                        3
                    }
                    Calendar.THURSDAY -> {
                        4
                    }
                    Calendar.FRIDAY -> {
                        5
                    }
                    Calendar.SATURDAY -> {
                        6
                    }
                    Calendar.SUNDAY -> {
                        7
                    }
                    else -> {
                        0
                    }
                }
            }
            CalendarType.PERSIAN -> {
                when (persianDate.dayOfWeek) {
                    Weekday.MONDAY -> {
                        3
                    }
                    Weekday.TUESDAY -> {
                        4
                    }
                    Weekday.WEDNESDAY -> {
                        5
                    }
                    Weekday.THURSDAY -> {
                        6
                    }
                    Weekday.FRIDAY -> {
                        7
                    }
                    Weekday.SATURDAY -> {
                        1
                    }
                    Weekday.SUNDAY -> {
                        2
                    }
                    else -> {
                        0
                    }
                }
            }
        }


        /* return when (GeneralCalendarType) {
             CalendarType.GEORGIAN -> {
                 val calendar = Calendar.getInstance()
                 calendar.time = grgDate
                 calendar.get(Calendar.DAY_OF_WEEK)
             }
             CalendarType.HIJRI -> {
                 hijriDate.dayOfWeek.value
             }
             CalendarType.PERSIAN -> {
                 persianDate.dayOfWeek.value
             }
         }*/
    }

    fun goFirstDateOfMonth(): GeneralCalendar {
        when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                this.hijriDate = HijriCalendar.of(
                    HijriCalendar.VARIANT_UMALQURA,
                    hijriDate.year,
                    hijriDate.month.value,
                    1
                )
                this.grgDate = hijriDate.toGregorian()
                this.persianDate = hijriDate.toPersianCalendar()

            }
            CalendarType.PERSIAN -> {
                this.persianDate = PersianCalendar.of(persianDate.year, persianDate.month.value, 1)
                this.grgDate = persianDate.toGregorian()
                this.hijriDate = persianDate.toHijriCalendar()
            }
        }
        return this
    }

    fun goEndDateOfMonth(): GeneralCalendar {
        when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.set(Calendar.DAY_OF_MONTH, getLengthsOfMonths())
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                this.hijriDate = HijriCalendar.of(
                    HijriCalendar.VARIANT_UMALQURA,
                    hijriDate.year,
                    hijriDate.month.value,
                    getLengthsOfMonths()
                )
                this.grgDate = hijriDate.toGregorian()
                this.persianDate = hijriDate.toPersianCalendar()

            }
            CalendarType.PERSIAN -> {
                this.persianDate = PersianCalendar.of(
                    persianDate.year,
                    persianDate.month.value,
                    getLengthsOfMonths()
                )
                this.grgDate = persianDate.toGregorian()
                this.hijriDate = persianDate.toHijriCalendar()
            }
        }
        return this
    }

    fun addHours(hour: Int): GeneralCalendar {
        val calendar = Calendar.getInstance()
        calendar.time = grgDate
        calendar.add(Calendar.HOUR_OF_DAY, hour)
        this.grgDate = calendar.time
        this.persianDate = grgDate.toPersianCalendar()
        this.hijriDate = grgDate.toHijriCalendar()
        return this
    }

    fun addDays(day: Int): GeneralCalendar {
        when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.add(Calendar.DATE, day)
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                this.hijriDate = this.hijriDate.plus(day, HijriCalendar.Unit.DAYS)
                this.grgDate = hijriDate.toGregorian()
                this.persianDate = hijriDate.toPersianCalendar()

            }
            CalendarType.PERSIAN -> {
                this.persianDate = this.persianDate.plus(day.toLong(), PersianCalendar.Unit.DAYS)
                this.grgDate = persianDate.toGregorian()
                this.hijriDate = persianDate.toHijriCalendar()
            }
        }
        return this
    }

    fun addMonths(month: Int): GeneralCalendar {
        when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.add(Calendar.MONTH, month)
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                this.hijriDate = this.hijriDate.plus(month, HijriCalendar.Unit.MONTHS)
                this.grgDate = hijriDate.toGregorian()
                this.persianDate = hijriDate.toPersianCalendar()

            }
            CalendarType.PERSIAN -> {
                this.persianDate =
                    this.persianDate.plus(month.toLong(), PersianCalendar.Unit.MONTHS)
                this.grgDate = persianDate.toGregorian()
                this.hijriDate = persianDate.toHijriCalendar()
            }
        }
        return this
    }

    fun addYears(year: Int): GeneralCalendar {
        when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.add(Calendar.YEAR, year)
                this.grgDate = calendar.time
                this.persianDate = grgDate.toPersianCalendar()
                this.hijriDate = grgDate.toHijriCalendar()
            }
            CalendarType.HIJRI -> {
                this.hijriDate = this.hijriDate.plus(year, HijriCalendar.Unit.YEARS)
                this.grgDate = hijriDate.toGregorian()
                this.persianDate = hijriDate.toPersianCalendar()

            }
            CalendarType.PERSIAN -> {
                this.persianDate = this.persianDate.plus(year.toLong(), PersianCalendar.Unit.YEARS)
                this.grgDate = persianDate.toGregorian()
                this.hijriDate = persianDate.toHijriCalendar()
            }
        }

        return this
    }

    fun getMonthName(): String {
        return when (GeneralCalendarType) {
            CalendarType.GEORGIAN -> {
                val calendar = Calendar.getInstance()
                calendar.time = grgDate
                calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) ?: ""
            }
            CalendarType.HIJRI -> {
                hijriDate.month.getDisplayName(Locale.ENGLISH)
            }
            CalendarType.PERSIAN -> {
                persianDate.month.getDisplayName(Locale("fa"))
            }
        }
    }

    fun getDateForServer(): String {
        return appDateFormatter.getDateForServer(this)
    }

    fun getListMonths(): Array<String> {
        return when (GeneralCalendarType) {
            CalendarType.PERSIAN -> arrayOf(
                "فروردین",
                "اردیبهشت",
                "خرداد",
                "تیر",
                "مرداد",
                "شهریور",
                "مهر",
                "ابان",
                "اذر",
                "دی",
                "بهمن",
                "اسفند"
            )
            CalendarType.HIJRI -> arrayOf(
                "محرم",
                "صفر",
                "ربیع الاول",
                "ربیع الثانی",
                "جمادی الاول",
                "جمادی الثانی",
                "رجب",
                "شعبان",
                "رمضان",
                "شوال",
                "ذی القعده",
                "ذی الحجه"
            )
            CalendarType.GEORGIAN -> arrayOf(
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December",
            )
        }
    }

    fun untilDays(generalCalendar: GeneralCalendar): Long {
        return CalendarDays.between(this.persianDate, generalCalendar.persianDate).amount
    }

    //bigger
    fun isAfter(gc: GeneralCalendar): Boolean {
        return grgDate.after(gc.grgDate)
    }

    //smaller
    fun isBefore(gc: GeneralCalendar): Boolean {
        return grgDate.before(gc.grgDate)
    }

    fun getId(): Int {
        return (getYear().toString() + getMonth().toString() + getDay().toString()).toInt()
    }

    fun copy(): GeneralCalendar {
        return GeneralCalendar(this.grgDate)
    }

    fun getPersianCalendar(): PersianCalendar {
        return copy().persianDate
    }

    fun getHijriCalendar(): HijriCalendar {
        return copy().hijriDate
    }

    fun getGeorgianDate(): Date {
        return this.grgDate
    }

    fun format(type: DateFormat = DateFormat.DEFAULT): String {
        return appDateFormatter.format(this, type)
    }

    fun getTimeMillis(): Long {
        return this.getGeorgianDate().time
    }

    companion object {
        fun getInstance(): GeneralCalendar {
            return GeneralCalendar(Calendar.getInstance().time)
        }

        var GeneralCalendarType = CalendarType.PERSIAN
    }

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is GeneralCalendar -> {
                this.getDay() == other.getDay() &&
                        this.getMonth() == other.getMonth() &&
                        this.getYear() == other.getYear()
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = getYear()
        result = 31 * result + getMonth()
        result = 31 * result + getDay()
        result = 31 * result + GeneralCalendarType.hashCode()
        return result
    }


    override fun toString(): String {
        return appDateFormatter.formatPersianDate(this) +
                "  ,  " +
                appDateFormatter.getDateForServer(this)
    }
}