package com.example.productivitypatterns.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productivitypatterns.components.DisplayQuestion
import com.example.productivitypatterns.components.SmallButton
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.Session
import com.example.productivitypatterns.domain.YesNoQuestion
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.util.formatTime
import com.example.productivitypatterns.util.listQuestions
import com.example.productivitypatterns.viewmodel.AuthState
import com.example.productivitypatterns.viewmodel.AuthViewModel
import com.example.productivitypatterns.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*


@Composable
fun StartActivityView() {
    var started: Boolean by remember { mutableStateOf(false) }
    val timerState = remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()
    var activityFinished: Boolean by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableList<Pair<UUID,Int>> = mutableListOf()

    var buttonText = "Start"

    var aboveButtonText = "Lorem ipsum dolor sit amet."

    if (started) {
        buttonText = "Stop"
        aboveButtonText = "Concentrate"
    }

    Surface(color = Background, modifier = Modifier.fillMaxSize()) {
        BoxWithConstraints {
            var constr = this
            if (!activityFinished) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        SmallButton(constr, Icons.Filled.Add)
                    }

                    Text(text = formatTime(timerState.value))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {

                        Text(
                            text = aboveButtonText,
                            fontSize = 16.sp,
                            fontFamily = InterFontFamily,
                            minLines = 2,
                        )

                        ElevatedButton(
                            elevation = ButtonDefaults.elevatedButtonElevation(
                                defaultElevation = 10.dp, pressedElevation = 10.dp, disabledElevation = 4.dp
                            ),
                            onClick = {
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
                            shape = RoundedCornerShape(16),
                            modifier = Modifier
                                .padding(bottom = constr.maxWidth * 0.1f)
                                .width(constr.maxWidth * 0.7f)
                                .height(constr.maxHeight * 0.1f),

                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = Color.White // Color de fondo del botón
                            ),
                        ) {
                            Text(text = buttonText, fontSize = 20.sp, fontFamily = InterFontFamily)
                        }
                    }

                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Text("Progress Bar Question Number: " + questionIndex)
                    DisplayQuestion(
                        listQuestions[questionIndex],
                        onCancel = {
                            activityFinished = false
                        },
                        onReply = {
                            answerList.add(Pair(listQuestions[questionIndex].id, it))
                            questionIndex++
                            if(questionIndex >= listQuestions.size) {
                                var session = Session(duration = timerState.value, responses = answerList, type="TODO")
                                activityFinished = false
                            }

                        },
                        constr = constr  
                    )
                }
            }


        }
    }

}