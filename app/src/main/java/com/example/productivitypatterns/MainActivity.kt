package com.example.productivitypatterns

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitypatterns.components.charts.BarChart
import com.example.productivitypatterns.components.charts.LineChart
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.ui.theme.BlueChartsCode
import com.example.productivitypatterns.ui.theme.ProductivityPatternsTheme
import com.example.productivitypatterns.view.LoginView
import com.example.productivitypatterns.view.StartActivityView
import com.example.productivitypatterns.view.WeekStatsView

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
        setContent {
            val navController = rememberNavController()
            val items = listOf(
                BottomNavigationItem(
                    title = "Login",
                    icon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    route = "Login"
                ),
                BottomNavigationItem(
                    title = "Start Activity",
                    icon = Icons.Filled.Star,
                    unselectedIcon = Icons.Outlined.Star,
                    route = "StartActivity",
                    hasNews = true
                ),
                BottomNavigationItem(
                    title = "Stats",
                    icon = Icons.Filled.Person,
                    unselectedIcon = Icons.Outlined.Person,
                    route = "WeekStats",
                    hasNews = false,
                    badgeCount = 45
                ),
            )

            var selectedItemIndex by rememberSaveable {
                mutableStateOf(0)
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                        navController.navigate(item.route) {
                                            // Evitar duplicados en la pila de navegación
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        BadgedBox(badge = {}) {
                                            Icon(
                                                imageVector = if (index == selectedItemIndex) {
                                                    item.icon
                                                } else item.unselectedIcon,
                                                contentDescription = item.title,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    // Aquí es donde NavHost debe estar para que cambie el contenido de la pantalla
                    NavHost(
                        navController = navController,
                        startDestination = "StartActivity",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("Login") {
                            LoginView()
                        }
                        composable("StartActivity") {
                            StartActivityView()
                        }
                        composable("WeekStats") {
                            WeekStatsView()
                        }
                    }
                }
            }
        }
    }
}
