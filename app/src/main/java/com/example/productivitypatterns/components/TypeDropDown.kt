package com.example.productivitypatterns.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.runtime.Composable
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

    Column {
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            TextField(value = selectedOption,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select session type") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(text = { Text(option) }, onClick = {
                        selectedOption = option
                        onChangeType(option)
                        expanded = false
                    })
                }

                DropdownMenuItem(text = { Text("Add type") }, onClick = {
                    showDialog = true
                })

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