package com.example.locationguardpro

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import androidx.viewpager.widget.ViewPager
import android.widget.CalendarView
import android.widget.ImageButton
import com.github.mikephil.charting.charts.BarChart


class ReportsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports_screen)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        val backButton = findViewById<ImageButton>(R.id.back_arrow)
        val settingsButton = findViewById<ImageButton>(R.id.settings_button)
        viewPager.adapter = adapter

        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        dotsIndicator.attachTo(viewPager)

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