package com.example.productivitypatterns.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.productivitypatterns.viewmodel.AuthViewModel

@Composable
fun HomeView(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    Text("HErte you can start an activity!")


}