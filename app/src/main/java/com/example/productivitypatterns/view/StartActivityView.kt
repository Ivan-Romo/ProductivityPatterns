package com.example.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitypatterns.components.Buttons.BigButton
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.components.DisplayQuestion
import com.example.productivitypatterns.components.Buttons.SmallButton
import com.example.productivitypatterns.domain.Session
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.util.formatTime
import com.example.productivitypatterns.util.listQuestions
import com.example.productivitypatterns.viewmodel.PersonalViewModel
import com.example.productivitypatterns.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


//example@gmail.com
//ola1234
//qRpm5LMD4vgxfFzOtzleXuBq1AU2


//dateWeek@gmail.com
//ola1234
//87NsxfU8VrPqQg4eeDxnaWuHsXy2


@Composable
fun StartActivityView(viewModel: SessionViewModel, personalViewModel: PersonalViewModel) {
    var started: Boolean by remember { mutableStateOf(false) }
    val timerState = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
    var activityFinished: Boolean by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableMap<String, Int> = mutableMapOf()
    var addActivity: Boolean by remember { mutableStateOf(false) }

    var buttonText = "Start session"

    var aboveButtonText = "Lorem ipsum dolor sit amet."

    if (started) {
        buttonText = "Stop"
        aboveButtonText = "Concentrate"
    }

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            var constr = this
            if (addActivity) {
                AddActivity(constr, onCancel = { addActivity = false }, viewModel)
            } else {
                if (!activityFinished) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                            SmallButton(constr, Icons.Filled.Add, onClick = { addActivity = true })
                        }

                        Box(
                            modifier = Modifier
                                .size(300.dp)
                                .shadow(8.dp, CircleShape, clip = false) // Aplica la sombra sin afectar el recorte
                                .clip(CircleShape) // Recorta en forma de círculo
                                .background(Color.White)
                                .clickable {

                                    started = !started
                                    if (started) {
                                        coroutineScope.launch {
                                            while (started) {
                                                delay(1000L)
                                                timerState.value += 1
                                            }
                                        }
                                    } else if (!activityFinished) {
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
                                Box(Modifier.padding(top = 50.dp)) {
                                    Text(
                                        text = buttonText,
                                        fontSize = 20.sp, // Tamaño más pequeño para el texto superior
                                        fontFamily = InterFontFamily,
                                    )
                                }
                                Text(
                                    text = formatTime(timerState.value),
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

                            BigButton(
                                constr, onClick = {
                                    started = !started
                                    if (started) {
                                        coroutineScope.launch {
                                            while (started) {
                                                delay(1000L)
                                                timerState.value += 1
                                            }
                                        }
                                    } else if (!activityFinished) {
                                        activityFinished = true
                                    }
                                },
                                buttonText = buttonText
                            )
                        }


                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text("Progress Bar Question Number: " + questionIndex)
                        var questionList = personalViewModel.getListEnabledQuestions()
                        DisplayQuestion(
                            questionList[questionIndex],
                            onReply = {
                                answerList[questionList[questionIndex].id]=  it
                                questionIndex++
                                if (questionIndex >= questionList.size) {
                                    var session =
                                        Session(duration = timerState.value, responses = answerList, type = "TODO")
                                    viewModel.createSession(session)

                                    started = false
                                    timerState.value = 0
                                    questionIndex = 0
                                    activityFinished = false
                                }

                            },
                            constr = constr
                        )

                        MediumButton(constr, onClick = { activityFinished = false }, buttonText = "Cancel")
                    }
                }
            }
        }
    }
}
