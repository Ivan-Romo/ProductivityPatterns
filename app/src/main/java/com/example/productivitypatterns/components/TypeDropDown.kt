package com.example.productivitypatterns.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.viewmodel.PersonalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(viewModel: PersonalViewModel, onChangeType: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf("undefined") }
    var expanded by remember { mutableStateOf(false) }
    var options by remember { mutableStateOf(viewModel.info.activityTypes) }
    var type by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.info.activityTypes) {
        options = viewModel.info.activityTypes
    }

    Column() {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.align(
            Alignment.CenterHorizontally)) {
            TextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select session type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom= 12.dp, start = 20.dp, end = 20.dp)
                    .menuAnchor()
                    .shadow(4.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false },modifier = Modifier.background(colorScheme.surface)) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        selectedOption = option
                        onChangeType(option)
                        expanded = false
                    })
                }

                DropdownMenuItem(text = { Text("Add type") }, onClick = {
                    showDialog = true
                },
                    modifier = Modifier.background(colorScheme.surface)
                )

            }
        }
    }

    if (showDialog) {
        AlertDialog(containerColor = Color.White, onDismissRequest = { showDialog = false }, title = {
            Text("Add New Question")
        }, text = {
            BoxWithConstraints {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Question text")
                    TextField(value = type ?: "",
                        onValueChange = { newText -> type = newText },
                        label = { Text("Enter activity type") })
                }
            }
        }, confirmButton = {
            Button(onClick = {
                if (type != "") {
                    selectedOption = type
                    onChangeType(type)
                    viewModel.addActivityType(type)
                }
                showDialog = false
            }) {
                Text("Add")
            }
        }, dismissButton = {
            Button(onClick = { showDialog = false }) {
                Text("Cancel")
            }
        })
    }
}