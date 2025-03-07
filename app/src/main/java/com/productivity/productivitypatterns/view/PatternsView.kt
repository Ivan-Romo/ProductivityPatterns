package com.productivity.productivitypatterns.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.productivity.productivitypatterns.viewmodel.*
import org.json.JSONObject
import org.json.JSONArray

@Composable
fun PatternsView(
    statsViewModel: StatsViewModel,
    personalViewModel: PersonalViewModel,
    sessionViewModel: LocalSessionViewModel,
    patternsViewModel: PatternsViewModel
) {
    var responseState by remember { mutableStateOf<ApiResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun fetchData() {
        isLoading = true
        patternsViewModel.callDeepSeekAPI { response ->
            responseState = response
            isLoading = false
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { fetchData() }) {
            Text("Cargar Datos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            responseState?.let { response ->
                response.strongestPatterns.forEach { pattern ->
                    Text("Factores: ${pattern.factors.joinToString(", ")}")
                    Text("Correlación: ${pattern.correlation}")
                }
                response.recommendations.forEach { recommendation ->
                    Text("Recomendación: $recommendation")
                }
            } ?: Text("Presiona el botón para cargar los datos.")
        }
    }
}