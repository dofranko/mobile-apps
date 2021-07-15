package com.example.l3z1_4

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.github.sundeepk.compactcalendarview.CompactCalendarView
import com.github.sundeepk.compactcalendarview.domain.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class PopUpCalendarActivity : AppCompatActivity() {
    lateinit var compactCalendar : CompactCalendarView
    var selectedDate = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up_calendar)
        title = "Wybierz datę"
        actionBar?.setDisplayShowTitleEnabled(false)
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)

        val width = dm.widthPixels
        val height = dm.heightPixels

        window.setLayout((width*.7).toInt(), (height*.5).toInt())
        val params = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = 0
        params.flags = params.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0.6f
        window.attributes = params



        compactCalendar = findViewById(R.id.compactcalendar_view)
        compactCalendar.addEvent(Event(Color.RED, Date().time))
        selectedDate = SimpleDateFormat("yyyy/MM/dd").format(Date().time)
        compactCalendar.setUseThreeLetterAbbreviation(true);
        val vt = findViewById<TextView>(R.id.textViewCalendarMonth)
        vt.text =SimpleDateFormat("MMMM yyyy").format(Calendar.getInstance().time)
        val events  = intent.getSerializableExtra("events") as HashMap<String, Int>
        if (events != null) {
            for((key, value) in events){
                compactCalendar.addEvent(Event(value, SimpleDateFormat("yyyy/MM/dd").parse(key).time))
            }
        }

        compactCalendar.setListener(object : CompactCalendarView.CompactCalendarViewListener{
            override fun onDayClick(dateClicked: Date?) {
                if(dateClicked != null){
                    selectedDate = SimpleDateFormat("yyyy/MM/dd").format(dateClicked)
                }
            }

            override fun onMonthScroll(firstDayOfNewMonth: Date?) {
                vt.text = SimpleDateFormat("MMMM yyyy").format(firstDayOfNewMonth)
            }

        })
    }

    fun selectOnClick(view: View) {
        val myintent = Intent()
        myintent.putExtra("dateFilter", selectedDate)
        setResult(RESULT_OK, myintent)
        finish()
    }

    fun clearOnCLick(view: View) {
        val myintent = Intent()
        myintent.putExtra("dateFilter", "")
        setResult(RESULT_OK, myintent)
        finish()
    }
}