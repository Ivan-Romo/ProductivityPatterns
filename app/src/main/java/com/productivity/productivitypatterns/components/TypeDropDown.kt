package com.productivity.productivitypatterns.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.ui.theme.InterFontFamily
import com.productivity.productivitypatterns.viewmodel.LocalSessionViewModel
import com.productivity.productivitypatterns.viewmodel.PersonalViewModel
import com.productivity.productivitypatterns.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeDropdown(viewModel: PersonalViewModel, onChangeType: (String) -> Unit, sessionViewModel: LocalSessionViewModel) {
    var selectedOption by remember { mutableStateOf(sessionViewModel.getLastSessionType()) }
    var expanded by remember { mutableStateOf(false) }
    var options by remember { mutableStateOf(viewModel.info.activityTypes) }
    var type by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.info.activityTypes) {
        options = viewModel.info.activityTypes
    }

    Column() {
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.align(
                Alignment.CenterHorizontally
            )
        ) {
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
                    .padding(bottom = 12.dp, start = 20.dp, end = 20.dp)
                    .menuAnchor()
                    .shadow(4.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(colorScheme.surface)
            ) {
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



        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .clickable(onClick = {showDialog = true })
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Add session Type",
                        fontFamily = InterFontFamily,
//                                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(13, 188, 171).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = Color(13, 188, 171)
                    )
                }
            }
        }

        if (showDialog) {
            AlertDialog(containerColor = colorScheme.surface, onDismissRequest = { showDialog = false }, title = {
                Text("Add New Question")
            }, text = {
                BoxWithConstraints {
                    var constr = this
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
}