package com.productivity.productivitypatterns.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.productivity.productivitypatterns.domain.Personalization
import com.productivity.productivitypatterns.viewmodel.AuthState
import com.productivity.productivitypatterns.viewmodel.AuthViewModel
import com.productivity.productivitypatterns.viewmodel.PersonalViewModel
import com.productivity.productivitypatterns.viewmodel.SessionViewModel


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

        }
    }

}