package com.example.productivitypatterns.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.Personalization
import com.example.productivitypatterns.viewmodel.AuthState
import com.example.productivitypatterns.viewmodel.AuthViewModel
import com.example.productivitypatterns.viewmodel.PersonalViewModel
import com.example.productivitypatterns.viewmodel.SessionViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


@Composable
fun DevelopView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    sessionViewModel: SessionViewModel = viewModel(),
    personalViewModel: PersonalViewModel
) {
    var context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()

    val sessions by sessionViewModel.sessionList.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }
    var data : Personalization? by remember { mutableStateOf(null) }

    val userAux = Personalization.default()

    BoxWithConstraints {
        var constr = this


        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Home Page", fontSize = 32.sp)

            MediumButton(constr, buttonText = "Reset", onClick = {
               personalViewModel.resetData()
            })
            if(data!=null){
                Text(data!!.customQuestions.toString())
            }








            TextButton(onClick = {
                authViewModel.signout()
            }) {
                Text(text = "Sign out")
            }
        }
    }

}