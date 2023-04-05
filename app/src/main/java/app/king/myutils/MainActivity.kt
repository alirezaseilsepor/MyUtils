package app.king.myutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.king.mylibrary.date.GeneralCalendar
import app.king.mylibrary.date.toGregorian
import app.king.mylibrary.date.toPersianCalendar
import net.time4j.android.ApplicationStarter
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApplicationStarter.initialize(this, false)


        test1()
        test2()
        test3()
        test4()
        test5()
        test6()
    }


    fun test1(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.GEORGIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val a=calendar.time.toPersianCalendar()

        Log.e("test1","${calendar.time}  ==  $a")
    }


    fun test2(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.GEORGIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val c=GeneralCalendar(2,4,2023)

        Log.e("test1","2023/4/2  ==  $c")
    }

    fun test3(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.GEORGIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val c=GeneralCalendar("2023/4/5")

        Log.e("test1","2023/4/5  ==  $c")
    }

    fun test4(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.PERSIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val a=calendar.time.toPersianCalendar().toGregorian()

        Log.e("test1","${calendar.time}  ==  $a")
    }


    fun test5(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.PERSIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val c=GeneralCalendar(15,1,1402)

        Log.e("test1","1402/1/15  ==  $c")
    }

    fun test6(){
        GeneralCalendar.GeneralCalendarType=GeneralCalendar.CalendarType.PERSIAN
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 20)
        val c=GeneralCalendar("1400/1/16")

        Log.e("test1","1400/1/16  ==  $c")
    }
}