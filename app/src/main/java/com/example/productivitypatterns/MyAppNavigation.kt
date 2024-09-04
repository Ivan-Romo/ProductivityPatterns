package com.example.productivitypatterns

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitypatterns.view.Home
import com.example.productivitypatterns.view.LoginView
import com.example.productivitypatterns.view.SignupView
import com.example.productivitypatterns.viewmodel.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, activity: Activity) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login"){
            LoginView(modifier, navController, authViewModel)
        }
        composable("signup"){
            SignupView(modifier, navController, authViewModel)
        }
        composable("home"){
            Home(modifier, navController, authViewModel, activity = activity)
        }
    })
}