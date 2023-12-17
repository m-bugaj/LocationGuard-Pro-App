package com.example.locationguardpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import androidx.viewpager.widget.ViewPager
import android.widget.CalendarView


class ReportsScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports_screen)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.worm_dots_indicator)
        dotsIndicator.attachTo(viewPager)


    }
}