package com.productivity.productivitypatterns

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.productivity.productivitypatterns.view.Home
import com.productivity.productivitypatterns.view.LoginView
import com.productivity.productivitypatterns.view.SignupView
import com.productivity.productivitypatterns.viewmodel.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel, activity: Activity) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home", builder = {
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