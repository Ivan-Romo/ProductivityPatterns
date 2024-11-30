package com.productivity.productivitypatterns.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.components.Buttons.MediumButton
import com.productivity.productivitypatterns.components.DisplayQuestion
import com.productivity.productivitypatterns.components.Buttons.SmallButton
import com.productivity.productivitypatterns.components.TypeDropdown
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.ui.theme.*
import com.productivity.productivitypatterns.util.formatTime
import com.productivity.productivitypatterns.viewmodel.AdManager
import com.productivity.productivitypatterns.viewmodel.LocalSessionViewModel
import com.productivity.productivitypatterns.viewmodel.PersonalViewModel
import com.productivity.productivitypatterns.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//example@gmail.com
//ola1234
//qRpm5LMD4vgxfFzOtzleXuBq1AU2


//dateWeek@gmail.com
//ola1234
//87NsxfU8VrPqQg4eeDxnaWuHsXy2


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartActivityView(viewModel: LocalSessionViewModel, personalViewModel: PersonalViewModel, activity: Activity) {

    val adManager = AdManager(LocalContext.current)

    var activityFinished: Boolean by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableMap<String, String> = mutableMapOf()
    var addActivity: Boolean by remember { mutableStateOf(false) }
    var type: String by remember { mutableStateOf(viewModel.getLastSessionType()) }

    Surface(color = colorScheme.background, modifier = Modifier.fillMaxSize()) {
        var adStatus by remember { mutableStateOf(false) }

        BoxWithConstraints {
            var constr = this
            if (addActivity) {
                AddActivity(constr, onCancel = {
                    addActivity = false
                    questionIndex = 0
                    activityFinished = false
                }, viewModel, personalViewModel)
            } else {
                if (!activityFinished) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            SmallButton(
                                constr,
                                Icons.Filled.Add,
                                onClick = { addActivity = true },
                                colorScheme = colorScheme,
                                isMainScreen = true
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .shadow(8.dp, CircleShape, clip = false) // Aplica la sombra sin afectar el recorte
                                .clip(CircleShape) // Recorta en forma de círculo
                                .background(colorScheme.surface)
                                .clickable {
                                    activityFinished = true
                                },
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Add a \n\nsession",
                                    fontSize = 40.sp,
                                    fontFamily = InterFontFamily,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Be productive and watch the analytics",
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily,
                                minLines = 2,
                            )
                            TypeDropdown(personalViewModel, onChangeType = { type = it }, sessionViewModel = viewModel)
                        }
                        Box(Modifier.fillMaxWidth().height(50.dp)) {
                            adManager.loadBannerAd(modifier = Modifier.fillMaxSize())
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize(),
                    ) {

                        if (!adStatus) {
                            val questionList = personalViewModel.getListEnabledQuestions()
                            LinearProgressIndicator(
                                progress = questionIndex / questionList.size.toFloat(),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                            )
                            DisplayQuestion(
                                questionList[questionIndex],
                                onReply = {
                                    answerList[questionList[questionIndex].id] = it
                                    questionIndex++
                                    if (questionIndex >= questionList.size) {
                                        val session = Session(
                                            duration = 60L, // Duración fija en segundos
                                            responses = answerList,
                                            type = type
                                        )
                                        viewModel.createSession(session)
                                        adStatus = true
                                        questionIndex = 0
                                    }
                                },
                                constr = constr,
                                personalViewModel = personalViewModel,
                            )

                            MediumButton(
                                constr, onClick = {
                                    activityFinished = false
                                    addActivity = false
                                    questionIndex = 0
                                }, buttonText = "Cancel",
                                colorScheme = colorScheme
                            )
                        } else {
                            adManager.showInterstitialAd(activity, onAdClosed = {
                                adStatus = false
                                activityFinished = false
                            })
                        }
                    }
                }
            }
        }
    }
}

