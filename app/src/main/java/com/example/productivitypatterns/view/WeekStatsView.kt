package com.example.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.components.charts.BarChart
import com.example.productivitypatterns.components.charts.LineChart
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.viewmodel.StatsViewModel

@Composable
fun WeekStatsView(statsViewModel: StatsViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BoxWithConstraints {
            var constr = this
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    //.background(Color.White)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
                        .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
                        .background(Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Poductivity on the last 7 days",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        ) {
                            RadialCircleChart(
                                statsViewModel.getAverageProductivityInTheLast7Days(),
                                BlueChartsCode,
                                BlueChartsCode,
                                "Average"
                            )
                            RadialCircleChart(
                                statsViewModel.getProductivityInTheLastSession(),
                                BlueChartsCode,
                                BlueChartsCode,
                                "Last"
                            )
                        }
                        val prodData = statsViewModel.getProductivityOfEachSessionInTheLast7Days()
                        LineChart(
                            listOf(
                                "Productivity" to prodData.second,
                            ),
                            prodData.first,
                            listOf(BlueChartsCode)
                        )
                    }

                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
                        .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
                        .background(Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Music & productivity",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }
                        val prodData = statsViewModel.getMusicProductivityInTheLastSession()
                            BarChart(
                                categories = prodData.keys.toList(),
                                values = prodData.values.toList(),
                                barColor = BlueChartsCode,
                            )


                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
                        .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
                        .background(Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Sleep & Stress",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }
                        val prodData = statsViewModel.getStressAndSleepOfEachSessionInTheLast7Days()
                        LineChart(
                            listOf(
                                "Productivity" to prodData.second[0],
                                "Stress" to prodData.second[2],
                            ),
                            prodData.first,
                            listOf(BlueChartsCode, GreenChartsCode)
                        )
                        LineChart(
                            listOf(
                                "Productivity" to prodData.second[0],
                                "Sleep" to prodData.second[1],
                            ),
                            prodData.first,
                            listOf(BlueChartsCode, GreenChartsCode)
                        )


                    }
                }

                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
                        .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
                        .background(Color.White)
                ) {

                    var prodData = statsViewModel.getCaffeineAndTrainingOfEachSessionInTheLast7Days()
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Training",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        ) {
                            RadialCircleChart(
                                prodData[0],
                                BlueChartsCode,
                                BlueChartsCode,
                                "Yes"
                            )
                            RadialCircleChart(
                                prodData[1],
                                BlueChartsCode,
                                BlueChartsCode,
                                "No"
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Caffeine",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 5.dp, end = 5.dp, top = 5.dp)
                        ) {
                            RadialCircleChart(
                                prodData[2],
                                BlueChartsCode,
                                BlueChartsCode,
                                "Yes"
                            )
                            RadialCircleChart(
                                prodData[3],
                                BlueChartsCode,
                                BlueChartsCode,
                                "No"
                            )
                        }

                    }

                }


            }
        }
    }
}