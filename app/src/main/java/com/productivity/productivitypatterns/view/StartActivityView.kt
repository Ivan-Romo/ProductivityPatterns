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
import androidx.compose.ui.text.style.TextAlign
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


    var started: Boolean by remember { mutableStateOf(false) }
    val startTime = remember { mutableStateOf<Long?>(null) }
    val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }

    val timerState = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
    var activityFinished: Boolean by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableMap<String, String> = mutableMapOf()
    var addActivity: Boolean by remember { mutableStateOf(false) }
    var type: String by remember { mutableStateOf(viewModel.getLastSessionType()) }

    var timer: Long? by remember { mutableStateOf(0L) }

    var buttonText = "Press to start \na session"

    var aboveButtonText = "Be productive and watch the analytics"

    if (started) {
        buttonText = "Stop"
        aboveButtonText = "Focus"
    }

    LaunchedEffect(started) {
        if (started) {
            startTime.value = System.currentTimeMillis()
        } else {
            startTime.value = null
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            if (started) {
                currentTime.value = System.currentTimeMillis()
            }
            delay(1000L)
        }
    }

    val elapsedTime: Long = startTime.value?.let { start ->
        (currentTime.value - start) / 1000 // Calcula el tiempo en segundos
    } ?: 0L

    Surface(color = colorScheme.background, modifier = Modifier.fillMaxSize()) {

        var adStatus by remember {
            mutableStateOf(false)
        }
        BoxWithConstraints {
            var constr = this
            if (addActivity) {
                AddActivity(constr, onCancel = {
                    addActivity = false
                    started = false
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
                                    started = !started
                                    if (!started && !activityFinished) {
                                        activityFinished = true
                                    }
                                },
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(Modifier.padding(top = 50.dp, start = 10.dp, end = 10.dp)) {
                                    Text(
                                        text = buttonText,
                                        fontSize = 20.sp, // Tamaño más pequeño para el texto superior
                                        fontFamily = InterFontFamily,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                timer = elapsedTime
                                if(elapsedTime < 0) {
                                    timer = 0L
                                }
                                Text(
                                    text = formatTime(timer!!),
                                    fontSize = 40.sp, // Tamaño más grande para el temporizador
                                    fontFamily = InterFontFamily
                                )
                                Box(Modifier.padding(bottom = 50.dp)) {}

                            }
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {

                            Text(
                                text = aboveButtonText,
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

                            var questionList = personalViewModel.getListEnabledQuestions()
                            LinearProgressIndicator(
                                progress = { (questionIndex) / questionList.size.toFloat() },
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
                                        var session =
                                            Session(duration = elapsedTime, responses = answerList, type = type)
                                        viewModel.createSession(session)
                                        adStatus = true
                                        started = false
                                        timerState.value = 0
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
                                    started = false
                                    questionIndex = 0
                                    activityFinished = false
                                }, buttonText = "Cancel",
                                colorScheme = colorScheme
                            )
                        } else {

                            adManager.showInterstitialAd(activity, onAdClosed =
                            {
                                adStatus = false
                                activityFinished = false
                            }
                            )

                        }
                    }
                }
            }
        }
    }
}

