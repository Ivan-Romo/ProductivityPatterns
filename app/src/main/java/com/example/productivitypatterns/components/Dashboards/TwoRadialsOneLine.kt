package com.example.productivitypatterns.components.Dashboards

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.ui.theme.BlueChartsCode

@Composable
fun TwoRadialsOneLine(rad1: @Composable () -> Unit, rad2: @Composable () -> Unit, line: @Composable () -> Unit) {
    Box {
        Column {

            Row {
                rad1()
                rad2()
            }
            line()
        }
    }
}