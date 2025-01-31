package com.productivity.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.productivity.productivitypatterns.components.TypeDropdown
import com.productivity.productivitypatterns.components.charts.BarChart
import com.productivity.productivitypatterns.components.charts.LineChart
import com.productivity.productivitypatterns.components.charts.RadialCircleChart
import com.productivity.productivitypatterns.domain.Question
import com.productivity.productivitypatterns.ui.theme.*
import com.productivity.productivitypatterns.util.listQuestions
import com.productivity.productivitypatterns.viewmodel.*
import kotlinx.coroutines.delay


@Composable
fun WeekStatsView(
    statsViewModel: StatsViewModel,
    personalViewModel: PersonalViewModel,
    sessionViewModel: LocalSessionViewModel
) {
    var type by remember { mutableStateOf(statsViewModel.sessionData.last().type) }
    var isLoading by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        isLoading  = false
        isLoading = true
        delay(1000)
        isLoading = false
    }
    if (isLoading) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {

        Surface(
            color = colorScheme.background, modifier = Modifier.fillMaxSize()
        ) {
            BoxWithConstraints {
                var constr = this
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(Modifier.fillMaxWidth().height(50.dp)) {
                        AdManager(LocalContext.current).loadBannerAd(modifier = Modifier.fillMaxSize())
                    }
                    Box(Modifier.height(16.dp)) {}

                    TypeDropdown(personalViewModel, onChangeType = { selectedType ->
                        type = selectedType
                    }, sessionViewModel = sessionViewModel)
                    key(type) {
                        StatsContent(type = type, statsViewModel, personalViewModel, constr)
                    }

                    Box(Modifier.fillMaxWidth().height(50.dp).padding(top = 20.dp)) {
                        AdManager(LocalContext.current).loadBannerAd(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Composable
fun StatsContent(
    type: String,
    statsViewModel: StatsViewModel,
    personalViewModel: PersonalViewModel,
    constr: BoxWithConstraintsScope
) {
//    var bgColor = if (isSystemInDarkTheme()) "#1E1E1E" else "#FFFBFE"
//    var chartColor = if (isSystemInDarkTheme()) "#08a4a7" else BlueChartsCode
//    var chartColor2 = if (isSystemInDarkTheme()) "#c65102" else GreenChartsCode
//    var textColor = if (isSystemInDarkTheme()) "#FFF" else "#000000"
    var bgColor = "#FFFBFE"
    var chartColor = BlueChartsCode
    var chartColor2 = GreenChartsCode
    var textColor = "#000000"

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .width(constr.maxWidth * 0.9f)
            .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
            .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
            .background(colorScheme.surface)
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
                    chartColor,
                    textColor,
                    "Average",
                    backgroundColor = bgColor,
                )
                RadialCircleChart(
                    statsViewModel.getProductivityInTheLastSession(type), chartColor, textColor, "Last",
                    backgroundColor = bgColor,
                )
            }
            val prodData = statsViewModel.getProductivityOfEachSessionInTheLast7Days(type)
            LineChart(
                listOf(
                    "Productivity" to prodData.second,
                ), prodData.first, listOf(chartColor)
            )
        }

    }

    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .width(constr.maxWidth * 0.9f)
            .shadow(8.dp, RoundedCornerShape(16.dp)) // Elevación con bordes redondeados
            .clip(RoundedCornerShape(16.dp)) // Recorte para asegurar los bordes redondeados
            .background(colorScheme.surface)
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
                    "Productivity by hour",
                    textAlign = TextAlign.Left,
                    fontFamily = InterFontFamily
                )
            }
            val prodData = statsViewModel.getProductivityByHourInTheLast7Days(type)
            LineChart(
                listOf(
                    "Productivity" to prodData.second,
                ), prodData.first, listOf(chartColor)
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
                        .background(colorScheme.surface)
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
                                    data.first, chartColor, textColor, "Yes",
                                    backgroundColor = bgColor,
                                )
                                RadialCircleChart(
                                    data.second, chartColor, textColor, "No",
                                    backgroundColor = bgColor,
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
                                ), data.first, listOf(chartColor, chartColor2)
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
                                barColor = chartColor,
                            )
                        }
                    }
                }
            }
        }
    }
}