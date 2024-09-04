package com.productivity.productivitypatterns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.productivity.productivitypatterns.ui.theme.ProductivityPatternsTheme
import com.productivity.productivitypatterns.viewmodel.AuthViewModel
import com.google.android.gms.ads.MobileAds


data class BottomNavigationItem(
    val title: String,
    val icon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: String,
    val hasNews: Boolean = false,
    val badgeCount: Int? = null
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        setContent {
            MobileAds.initialize(this)

            ProductivityPatternsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(modifier = Modifier.padding(innerPadding), authViewModel = authViewModel, this)

                }
            }
        }
    }

}
