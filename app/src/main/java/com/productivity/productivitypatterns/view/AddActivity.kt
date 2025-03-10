package com.productivity.productivitypatterns.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.productivity.productivitypatterns.components.Buttons.MediumButton
import com.productivity.productivitypatterns.components.DisplayQuestion
import com.productivity.productivitypatterns.components.TypeDropdown
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.ui.theme.InterFontFamily
import com.productivity.productivitypatterns.util.listQuestions
import com.productivity.productivitypatterns.viewmodel.LocalSessionViewModel
import com.productivity.productivitypatterns.viewmodel.PersonalViewModel
import com.productivity.productivitypatterns.viewmodel.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun AddActivity(constr: BoxWithConstraintsScope, onCancel: () -> Unit, viewModel: LocalSessionViewModel, personalViewModel: PersonalViewModel) {

    var questionsMode by remember { mutableStateOf(false) }
    var questionIndex: Int by remember { mutableStateOf(0) }
    var answerList: MutableMap<String, String> by remember {  mutableStateOf(mutableMapOf())}
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
                    },
                    colorScheme = colorScheme
                )

                MediumButton(
                    constr, onClick = { timeStartPickerDialog.show() },
                    if (selectedStartTime == "") {
                        "Select start time"
                    } else {
                        "Start time: $selectedStartTime"
                    },
                    colorScheme = colorScheme
                )

                MediumButton(
                    constr, onClick = { timeEndPickerDialog.show() },
                    if (selectedEndTime == "") {
                        "Select end time"
                    } else {
                        "End time: $selectedEndTime"
                    },
                    colorScheme = colorScheme
                )

                MediumButton(
                    constr,
                    onClick = { questionsMode = true },
                    "Continue",
                    colorScheme = colorScheme
                )

            }
        } else {
            LinearProgressIndicator(
                progress = { (questionIndex ) / listQuestions.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )
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
                                datetime = LocalDateTime.parse("$selectedDate $selectedEndTime", DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")),
                                duration = java.time.Duration.between(startDateTime, endDateTime).toMinutes(),
                                responses = answerList,
                                type = viewModel.sessionList.value.last().type,
                            )
                        viewModel.createSession(session)
                        questionIndex = 0
                        onCancel()
                    }

                },
                constr = constr,
                personalViewModel = personalViewModel
            )
        }

        MediumButton(constr, onClick = { onCancel() }, buttonText = "Cancel",
            colorScheme = colorScheme)
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