package com.example.productivitypatterns.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.productivitypatterns.domain.Question
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.components.Buttons.SmallButton
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.viewmodel.PersonalViewModel

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.ui.theme.Background
import com.example.productivitypatterns.ui.theme.InterFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayQuestion(
    question: Question, onReply: (String) -> Unit, constr: BoxWithConstraintsScope, personalViewModel: PersonalViewModel
) {
    Box(
        modifier = Modifier
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .background(colorScheme.surface, shape = RoundedCornerShape(16.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .width(constr.maxWidth * 0.83f)
                .padding(20.dp)
        ) {
            Text(
                question.question,
                fontSize = 20.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if (question is Question.YesNoQuestion) {
                Row {
                    SmallButton(constr, text = "Yes", onClick = { onReply("yes") }, colorScheme = colorScheme)
                    SmallButton(constr, text = "No", onClick = { onReply("no") },colorScheme = colorScheme)
                }
            } else if (question is Question.MultipleChoiceQuestion) {
                if (personalViewModel.info.customAnswers.containsKey(question.id)) {
                    personalViewModel.info.customAnswers[question.id]!!.forEach { answer ->
                        if (!question.options.contains(answer)) {
                            question.options.add(answer)
                        }
                    }
                }

                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    // Usamos un estado mutable para almacenar las opciones
                    var options by remember { mutableStateOf(question.options) }
                    var showDialog by remember { mutableStateOf(false) }
                    var newOption by remember { mutableStateOf("") }

                    options.forEachIndexed { index, option ->
                        if (personalViewModel.optionIsCustom(question.id, option)) {
                            Row {
                                IconButton(onClick = {
                                    // Eliminar la opción tanto en el ViewModel como en la lista local de opciones
                                    personalViewModel.deleteCustomOption(question.id, option)
                                    options =
                                        options.filter { it != option }.toMutableList()  // Actualizar la lista de opciones en tiempo real
                                    question.options.remove(option)
                                }) { Icon(Icons.Outlined.Delete, contentDescription = "") }

                                MediumButton(
                                    constr,
                                    buttonText = option,
                                    onClick = { onReply(option) },
                                    width = constr.maxWidth * 0.6f,
                                    colorScheme = colorScheme,
                                    isInBackground =  false
                                )
                            }
                        } else {
                            MediumButton(
                                constr,
                                buttonText = option,
                                onClick = { onReply(option) },
                                colorScheme = colorScheme,
                                isInBackground =  false
                            )
                        }
                    }

                    MediumButton(
                        constr,
                        buttonText = "Other",
                        onClick = {
                            showDialog = true
                        },
                        colorScheme = colorScheme,
                        isInBackground =  false
                    )

                    if (showDialog) {
                        AlertDialog(
                            containerColor = colorScheme.surface,
                            onDismissRequest = { showDialog = false },
                            title = {
                                Text("Add a new option")
                            },
                            text = {
                                TextField(
                                    value = newOption ?: "",
                                    onValueChange = { newText -> newOption = newText },
                                )
                            },
                            confirmButton = {
                                Button(onClick = {
                                    showDialog = false
                                    personalViewModel.addOptionToQuestion(question.id, newOption)
                                    options =
                                        (options + newOption).toMutableList() // Agregar la nueva opción a la lista local de opciones
                                }) {
                                    Text("Add")
                                }
                            },
                            dismissButton = {
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancel")
                                }
                            }
                        )
                    }
                }

            } else if (question is Question.RatingQuestion) {
                for (i in 1..10 step 3) {
                    Row(modifier = Modifier.padding(top = 10.dp)) {
                        SmallButton(constr, text = i.toString(), onClick = { onReply(i.toString()) },colorScheme = colorScheme)
                        if (i + 1 <= 10) {
                            SmallButton(constr, text = (i + 1).toString(), onClick = { onReply((i + 1).toString()) },colorScheme = colorScheme)
                        }
                        if (i + 2 <= 10) {
                            SmallButton(constr, text = (i + 2).toString(), onClick = {
                                onReply((i + 2).toString())
                            },colorScheme = colorScheme)
                        }
                    }
                }
            }
        }
        //}
    }
}