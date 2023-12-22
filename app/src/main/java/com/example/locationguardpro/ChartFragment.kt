package com.example.locationguardpro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import com.example.locationguardpro.components.MyBarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import kotlin.random.Random




class ChartFragment: Fragment(R.layout.fragment_chart) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val barChart: BarChart = view.findViewById(R.id.bar_chart_fragment)
        var barEntries = ArrayList<BarEntry>();
        var hoursWorked = 0
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

        // Ustawienie koloru tekstu legendy na biały
        val legend = barChart.legend
        legend.textColor = Color.White.hashCode()

        // Ustawienie koloru etykiet osi X na biały
        val xAxis: XAxis = barChart.xAxis
        xAxis.textColor = Color.White.hashCode()

        // Ustawienie koloru etykiet osi Y na biały
        val leftYAxis: YAxis = barChart.axisLeft
        leftYAxis.textColor = Color.White.hashCode()

        val rightYAxis: YAxis = barChart.axisRight
        rightYAxis.textColor = Color.White.hashCode()
    }




}