package com.productivity.productivitypatterns.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.components.Buttons.MediumButton
import com.productivity.productivitypatterns.ui.theme.InterFontFamily
import com.productivity.productivitypatterns.viewmodel.*
import org.json.JSONObject
import org.json.JSONArray

@Composable
fun PatternsView(
    statsViewModel: StatsViewModel,
    personalViewModel: PersonalViewModel,
    sessionViewModel: LocalSessionViewModel,
    patternsViewModel: PatternsViewModel,
    adManager: AdManager,
    activity: Activity
) {
    var responseState by remember { mutableStateOf<ApiResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(true) }
    var loadingMessageIndex by remember { mutableStateOf(0) }

    val loadingMessages = listOf(
        "Collecting data...",
        "Analyzing behavioral patterns...",
        "Correlating variables...",
        "Refining predictive models...",
        "Performing anomaly detection...",
        "Processing multivariate dependencies...",
        "Extracting key productivity insights..."
    )

    fun fetchData() {
        isLoading = true
        showButton = false
        patternsViewModel.callDeepSeekAPI { response ->
            responseState = response
            isLoading = false
        }
    }

    LaunchedEffect(isLoading) {
        while (isLoading) {
            loadingMessageIndex = (loadingMessageIndex + 1) % loadingMessages.size
            kotlinx.coroutines.delay(4000)
        }
    }

    BoxWithConstraints {
        var constr = this

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showButton) {

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
                                text = "Want to optimize your productivity?",
                                textAlign = TextAlign.Left,
                                fontFamily = InterFontFamily
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Discover insights based on your activity and habits. Analyze your data to identify what works best for you and receive personalized recommendations. Tap the button below to generate your report!",
                                textAlign = TextAlign.Left,
                                fontSize = 14.sp,
                                fontFamily = InterFontFamily,

                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .width(constr.maxWidth * 0.9f)
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = {
                                adManager.showRewardedAd(activity) { rewardAmount ->
                                    fetchData()
                                }
                            })
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color(13, 188, 171).copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShowChart,
                                    contentDescription = null,
                                    tint = Color(13, 188, 171)
                                )
                            }
                            Text(
                                text = "Generate my report",
                                fontFamily = InterFontFamily,
//                                        fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(13, 188, 171).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color(13, 188, 171)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text(loadingMessages[loadingMessageIndex], fontSize = 14.sp)
            } else {
                responseState?.let { response ->
                    response.strongestPatterns.forEach { pattern ->
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
                                        pattern.factors.joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } },
                                        textAlign = TextAlign.Left,
                                        fontFamily = InterFontFamily
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = pattern.explanation,
                                        textAlign = TextAlign.Left,
                                        fontSize = 14.sp,
                                        fontFamily = InterFontFamily
                                    )
                                }
                            }
                        }
                    }
                    response.recommendations.forEachIndexed { index, recommendation ->
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
                                        text = "Tip " + (index+1),
                                        textAlign = TextAlign.Left,
                                        fontFamily = InterFontFamily
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = recommendation.advice,
                                        textAlign = TextAlign.Left,
                                        fontSize = 14.sp,
                                        fontFamily = InterFontFamily
                                    )
                                }

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Based on: " + recommendation.basedOn.joinToString(", ") { it.replaceFirstChar { c -> c.uppercase() } },
                                        textAlign = TextAlign.Left,
                                        fontFamily = InterFontFamily
                                    )
                                }

                            }
                        }
                    }
                } ?: Text("")
            }
        }
    }
}