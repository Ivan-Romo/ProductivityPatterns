package com.example.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.ui.theme.Background
import com.example.productivitypatterns.ui.theme.InterFontFamily
import com.example.productivitypatterns.viewmodel.PersonalViewModel

@Composable
fun PersonalView(viewModel: PersonalViewModel) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        BoxWithConstraints {
            var constr = this

            // Estado para controlar la visibilidad del diálogo
            var showDialog by remember { mutableStateOf(false) }
            var enabledQuestionsData by remember { mutableStateOf(viewModel.getListQuestionsAndEnabled()) }
            LaunchedEffect(viewModel.info) {
                enabledQuestionsData = viewModel.getListQuestionsAndEnabled()
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .width(constr.maxWidth * 0.9f)
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            "Enabled questions",
                            textAlign = TextAlign.Left,
                            fontWeight = FontWeight.Bold,
                            fontFamily = InterFontFamily
                        )

                        enabledQuestionsData.forEach { question ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                var isChecked by remember { mutableStateOf(question.second) }
                                Text(
                                    question.first.question,
                                    textAlign = TextAlign.Left,
                                    textDecoration = if (!isChecked) TextDecoration.LineThrough else null,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 10.dp)
                                )
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = {
                                        isChecked = it
                                    }
                                )
                            }
                        }
                    }
                }
                MediumButton(
                    constr,
                    buttonText = "Add question",
                    width = constr.maxWidth * 0.9f,
                    onClick = { showDialog = true }  // Muestra el diálogo al hacer clic
                )
            }

            // Diálogo que se muestra cuando showDialog es true
            if (showDialog) {

                var questionEntered by remember { mutableStateOf(false) }
                var type: String? by remember { mutableStateOf(null) }
                var question: String? by remember { mutableStateOf(null) }
                var textFields by remember { mutableStateOf(listOf("")) }

                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text("Add New Question")
                    },
                    text = {
                        BoxWithConstraints {
                            var constr2 = this
                            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.SpaceBetween) {
                                if(type==null) {
                                    Text("Question type")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    MediumButton(constr2, width = constr.maxWidth * 0.9f, buttonText = "Yes or No", onClick = { type = "yesno" })
                                    MediumButton(constr2, width = constr.maxWidth * 0.9f, buttonText = "Multiple Choice", onClick = { type = "multiple" })
                                    MediumButton(constr2, width = constr.maxWidth * 0.9f, buttonText = "1 to 10", onClick = { type = "rating" })
                                }
                                else if(!questionEntered) {
                                    Text("Question text")
                                    TextField(
                                        value = question ?: "",
                                        onValueChange = { newText -> question = newText },
                                        label = { Text("Enter your custom question") }
                                    )
                                }
                                else if(type=="multiple") {
                                    textFields.forEachIndexed { index, text ->
                                        TextField(
                                            value = text,
                                            onValueChange = { newText ->
                                                // Actualizar el estado del TextField en el índice correspondiente
                                                textFields = textFields.toMutableList().apply { this[index] = newText }
                                            },
                                            label = { Text("Campo de texto ${index + 1}") },
                                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                                        )
                                    }

                                    MediumButton(constr2, width = constr.maxWidth * 0.9f, buttonText = "Add more option",onClick = {
                                        // Agregar un nuevo campo de texto a la lista
                                        textFields = textFields + ""
                                    },)

                                }
                            }
                        }
                    },
                    confirmButton = {
                        if(type==null) {
                        }
                        else if((!questionEntered && type!="multiple") || questionEntered) {
                            Button(
                                onClick = {
                                    showDialog = false

                                    var quest: Question? = null

                                    if(type=="yesno"){
                                        quest = Question.YesNoQuestion(question= question ?:"")
                                    }
                                    else if(type=="rating"){
                                        quest = Question.RatingQuestion(question = question ?: "")
                                    }else if(type=="multiple"){
                                        quest = Question.MultipleChoiceQuestion(question = question ?: "", options = textFields)
                                    }

                                    if(quest != null){
                                        viewModel.addCustomQuestion(quest)
                                    }

                                }
                            ) {
                                Text("Add")
                            }
                        }
                        else{
                            Button(
                                onClick = {
                                    questionEntered = true

                                }
                            ) {
                                Text("Continue")
                            }
                        }

                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}