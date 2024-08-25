package com.example.productivitypatterns.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productivitypatterns.viewmodel.AuthState
import com.example.productivitypatterns.viewmodel.AuthViewModel
import com.example.productivitypatterns.viewmodel.CarListViewModel

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    carViewModel: CarListViewModel = viewModel()
) {
    val authState = authViewModel.authState.observeAsState()

    val cars by carViewModel.carList.collectAsStateWithLifecycle()

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }



    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Page", fontSize = 32.sp)

        for (car in cars) {
            Text(car.brand + " " + car.id)
        }
        



        TextButton(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
    }

}