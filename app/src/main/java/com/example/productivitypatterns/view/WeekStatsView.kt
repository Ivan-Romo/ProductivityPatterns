package com.example.productivitypatterns.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.productivitypatterns.components.charts.BarChart
import com.example.productivitypatterns.components.charts.LineChart
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.ui.theme.BlueChartsCode

@Composable
fun WeekStatsView() {
    Column(modifier = Modifier
        //.background(Color.White)
        .fillMaxSize()) {
        Text("HErte you can start an activity!")

        RadialCircleChart(63, BlueChartsCode, BlueChartsCode, "ola")

        BarChart(listOf("aa","bbb","ccc","ddd","eee","fff","ggg"), listOf(10,20,80,90,34,3,73), BlueChartsCode )
        LineChart()
    }
}