package com.example.locationguardpro

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.locationguardpro.components.MyBarDataSet
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
        var xLabelsList =  mutableListOf<String>()
        //xLabelsList.add("0")
        var barEntries = ArrayList<BarEntry>();
        var hoursWorked = 0
        val min = 0.0f
        val max = 10.0f
        val dateList = mutableListOf<String>()
        lifecycleScope.launch {

            val myApplication = requireActivity().application as MyApplication
            val appDatabase = myApplication.appDatabase
            val workHoursDao = appDatabase.workHoursDao()

            val sharedPreferences =
                requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getLong("USER_ID", -1)


            for (i in 0 until 6) {
                val date = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                    .format(Date(System.currentTimeMillis() - i * 24 * 60 * 60 * 1000))
                dateList.add(date)
            }
            var workHours = 0.0



            try {
                for (i in 0..6) {
                    workHours = workHoursDao.getTotalHoursForDate(userId, dateList[i])
                    var value = 0.0
                    if (workHours != null)
                        value = workHours
                    val barEntry = BarEntry(i * 1.0f, value.toFloat())
                    barEntries.add(barEntry)
                    xLabelsList.add(dateList[i])
                    hoursWorked += (i);
                }
            } catch (e: Exception) {
                println("Wystąpił błąd: ${e.message}")
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
            barChart.notifyDataSetChanged();

            // Ustawienie koloru tekstu legendy na biały
            val legend = barChart.legend
            legend.textColor = Color.White.hashCode()

            // Ustawienie koloru etykiet osi X na biały
            val xAxis: XAxis = barChart.xAxis
            xAxis.textColor = Color.White.hashCode()
            xLabelsList.reverse()
            for (l in xLabelsList)
                Log.d("LIST", l)
            xAxis.valueFormatter = IndexAxisValueFormatter(xLabelsList)
            xAxis.granularity = 1.0f
            xAxis.isGranularityEnabled = true

            // Ustawienie koloru etykiet osi Y na biały
            val leftYAxis: YAxis = barChart.axisLeft
            leftYAxis.textColor = Color.White.hashCode()
            leftYAxis.axisMinimum = 0F

            val rightYAxis: YAxis = barChart.axisRight
            rightYAxis.textColor = Color.White.hashCode()
        }
    }




}