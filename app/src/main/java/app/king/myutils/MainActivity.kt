package app.king.myutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.king.mylibrary.date.GeneralCalendar
import net.time4j.android.ApplicationStarter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ApplicationStarter.initialize(this, false)
        val c=GeneralCalendar("2023-03-11")
        val e1=c.getDateForServer(isEnableTime = true)
        val d=e1
    }
}