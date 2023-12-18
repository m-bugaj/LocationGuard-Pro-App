package com.example.locationguardpro


import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.locationguardpro.components.MyBarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.random.Random


class StatisticsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_screen)

        var barChart = findViewById<BarChart>(R.id.bar_chart)
        var hoursWorkedText = findViewById<TextView>(R.id.hours_worked)
        var calendar = findViewById<CalendarView>(R.id.calendarView)

        var barEntries = ArrayList<BarEntry>();
        var hoursWorked = 0
        val backButton = findViewById<ImageButton>(R.id.back_button2)
        val settingsButton = findViewById<ImageButton>(R.id.settings_button2)
        val min = 0.0f
        val max = 10.0f



        for(i in 1..7){
            var value = min + Random.nextFloat() * (max - min)
            val barEntry = BarEntry(i * 1.0f, value)
            barEntries.add(barEntry)
            hoursWorked += i;
        }

        var barDataSet = MyBarDataSet(barEntries, "Hours worked")
        barDataSet.colors =
            intArrayOf(
                Color.Red.hashCode(),
                Color.Yellow.hashCode(),
                Color.Green.hashCode()
            ).toList()
        barDataSet.setDrawValues(false)
        barChart.data = BarData(barDataSet)
        barChart.animateY(5000)
        barChart.description.text = "Hours worked chart"
        barChart.description.textColor = Color.Blue.hashCode()

        val str = SpannableString("Hours of work this week: $hoursWorked/40")
        if(hoursWorked < 40)
            str.setSpan(ForegroundColorSpan(Color.Yellow.hashCode()), 25, 30, 0)
        else
            str.setSpan(ForegroundColorSpan(Color.Green.hashCode()), 25, 30, 0)
        hoursWorkedText.text = str

        val scalingFactor = 0.5f // scale down to half the size

        calendar.scaleX = scalingFactor
        calendar.scaleY = scalingFactor

        backButton.setOnClickListener{
            val intent = Intent(this, HomeScreenActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        settingsButton.setOnClickListener{
            // Tworzymy Intencję, aby przenieść się na ekran TrackingScreenActivity
            val intent = Intent(this, LoginScreenActivity::class.java)

            // Uruchamiamy aktywność
            startActivity(intent)

            // Ustawiamy animację wejścia i wyjścia
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }


    }
}