package com.example.productivitypatterns.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.components.DisplayQuestion
import com.example.productivitypatterns.domain.Session
import com.example.productivitypatterns.ui.theme.InterFontFamily
import com.example.productivitypatterns.util.listQuestions
import com.example.productivitypatterns.viewmodel.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AddActivity(constr: BoxWithConstraintsScope, onCancel: () -> Unit,viewModel: SessionViewModel) {

    var questionsMode by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableMap<String, Int> by remember {  mutableStateOf(mutableMapOf())}
    var dateTimeCustom: LocalDateTime


    var selectedDate by remember { mutableStateOf("") }
    var selectedStartTime by remember { mutableStateOf("") }
    var selectedEndTime by remember { mutableStateOf("") }

    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            updateDateTime(selectedDate, selectedStartTime) { updatedDateTime ->
                selectedDateTime = updatedDateTime
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timeStartPickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay, minute ->
            selectedStartTime = "%02d:%02d".format(hourOfDay, minute)
            updateDateTime(selectedDate, selectedStartTime) { updatedDateTime ->
                selectedDateTime = updatedDateTime
            }
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )
    val timeEndPickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay, minute ->
            selectedEndTime = "%02d:%02d".format(hourOfDay, minute)
            updateDateTime(selectedDate, selectedEndTime) { updatedDateTime ->
                selectedDateTime = updatedDateTime
            }
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
    ) {

        if (!questionsMode) {
            Text("Create a session manually", fontFamily = InterFontFamily)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                MediumButton(
                    constr, onClick = { datePickerDialog.show() }, if (selectedDate == "") {
                        "Select date"
                    } else {
                        "Date : $selectedDate"
                    }
                )

                MediumButton(
                    constr, onClick = { timeStartPickerDialog.show() },
                    if (selectedStartTime == "") {
                        "Select start time"
                    } else {
                        "Start time: $selectedStartTime"
                    }
                )

                MediumButton(
                    constr, onClick = { timeEndPickerDialog.show() },
                    if (selectedEndTime == "") {
                        "Select end time"
                    } else {
                        "End time: $selectedEndTime"
                    }
                )

                MediumButton(
                    constr,
                    onClick = { questionsMode = true },
                    "Continue"
                )

            }
        } else {
            Text("Progress Bar Question Number: " + questionIndex)
            DisplayQuestion(
                listQuestions[questionIndex],
                onReply = {
                    answerList[listQuestions[questionIndex].id] = it
                    questionIndex++
                    if (questionIndex >= listQuestions.size) {

                        val startDateTime = LocalDateTime.parse("$selectedDate $selectedStartTime", DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"))
                        val endDateTime = LocalDateTime.parse("$selectedDate $selectedEndTime", DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"))
                        var session =
                            Session(
                                datetime = LocalDateTime.parse("$selectedDate $selectedStartTime", DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")),
                                duration = java.time.Duration.between(startDateTime, endDateTime).toMinutes(),
                                responses = answerList,
                                type = "TODO"
                            )
                        viewModel.createSession(session)
                        questionIndex = 0
                        onCancel()

                    }

                },
                constr = constr
            )
        }

        MediumButton(constr, onClick = { onCancel() }, buttonText = "Cancel")
    }
}


fun updateDateTime(
    date: String,
    time: String,
    onDateTimeUpdated: (LocalDateTime?) -> Unit
) {
    if (date.isNotBlank() && time.isNotBlank()) {
        val formatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")
        val dateTime = LocalDateTime.parse("$date $time", formatter)
        onDateTimeUpdated(dateTime)
    }
}