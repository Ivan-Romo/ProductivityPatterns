package com.example.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.components.TypeDropdown
import com.example.productivitypatterns.components.charts.BarChart
import com.example.productivitypatterns.components.charts.LineChart
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.util.listQuestions
import com.example.productivitypatterns.viewmodel.PersonalViewModel
import com.example.productivitypatterns.viewmodel.StatsViewModel

@Composable
fun WeekStatsView(statsViewModel: StatsViewModel, personalViewModel: PersonalViewModel) {
    var type by remember { mutableStateOf(statsViewModel.sessionData.last().type) }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BoxWithConstraints {
            var constr = this
            Column(
                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                TypeDropdown(personalViewModel, onChangeType = { selectedType ->
                    type = selectedType
                })
                key(type) {
                    StatsContent(type = type, statsViewModel, personalViewModel, constr)
                }

            }
        }
    }
}

@Composable
fun StatsContent(type: String, statsViewModel: StatsViewModel, personalViewModel: PersonalViewModel, constr:  BoxWithConstraintsScope)
{
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
                    statsViewModel.getAverageProductivityInTheLast7Days(type),
                    BlueChartsCode,
                    BlueChartsCode,
                    "Average"
                )
                RadialCircleChart(
                    statsViewModel.getProductivityInTheLastSession(type), BlueChartsCode, BlueChartsCode, "Last"
                )
            }
            val prodData = statsViewModel.getProductivityOfEachSessionInTheLast7Days(type)
            LineChart(
                listOf(
                    "Productivity" to prodData.second,
                ), prodData.first, listOf(BlueChartsCode)
            )
        }

    }


    personalViewModel.info.enabledQuestions.forEach { question ->
        if (question.value && question.key != "prod") {
            val questFromList = listQuestions.find { quest -> quest.id == question.key }
            val questFromCustom =
                personalViewModel.info.customQuestions.find { quest -> quest.id == question.key }
            if (questFromList != null || questFromCustom != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        if ((questFromList is Question.YesNoQuestion) || (questFromCustom is Question.YesNoQuestion)) {
                            var data = statsViewModel.getYesNoStats(question.key, type)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    questFromList?.question ?: questFromCustom!!.id,
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
                                    data.second, BlueChartsCode, BlueChartsCode, "Yes"
                                )
                                RadialCircleChart(
                                    data.second, BlueChartsCode, BlueChartsCode, "No"
                                )

                            }

                        } else if ((questFromList is Question.RatingQuestion) || (questFromCustom is Question.RatingQuestion)) {

                            var data = statsViewModel.getRatingStats(question.key, type)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    questFromList?.question ?: questFromCustom!!.id,
                                    textAlign = TextAlign.Left,
                                    fontFamily = InterFontFamily
                                )
                            }

                            LineChart(
                                listOf(
                                    "Productivity" to data.second.first,
                                    "Answer" to data.second.second,
                                ), data.first, listOf(BlueChartsCode, GreenChartsCode)
                            )
                        } else if ((questFromList is Question.MultipleChoiceQuestion) || (questFromCustom is Question.MultipleChoiceQuestion)) {
                            var data = statsViewModel.getMultipleStats(question.key, type)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    questFromList?.question ?: questFromCustom!!.id,
                                    textAlign = TextAlign.Left,
                                    fontFamily = InterFontFamily
                                )
                            }
                            BarChart(
                                categories = data.keys.toList(),
                                values = data.values.toList(),
                                barColor = BlueChartsCode,
                            )
                        }
                    }
                }
            }
        }
    }
}