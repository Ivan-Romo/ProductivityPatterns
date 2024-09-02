package com.example.productivitypatterns.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitypatterns.BottomNavigationItem
import com.example.productivitypatterns.R
import com.example.productivitypatterns.ui.theme.InterFontFamily
import com.example.productivitypatterns.viewmodel.AuthViewModel
import com.example.productivitypatterns.viewmodel.PersonalViewModel
import com.example.productivitypatterns.viewmodel.SessionViewModel
import com.example.productivitypatterns.viewmodel.StatsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    auxNavController: NavController,
    authViewModel: AuthViewModel,
    sessionViewModel: SessionViewModel = viewModel()
) {

    var context = LocalContext.current
    var personalViewModel = PersonalViewModel(context)
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "Session",
            icon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "StartActivity"
        ),
        BottomNavigationItem(
            title = "Develop", icon = Icons.Filled.Code, unselectedIcon = Icons.Outlined.Code, route = "Develop"
        ),
        BottomNavigationItem(
            title = "Stats",
            icon = Icons.Filled.Analytics,
            unselectedIcon = Icons.Outlined.Analytics,
            route = "WeekStats",
            hasNews = false,
            badgeCount = 45
        ),
        BottomNavigationItem(
            title = "Personalization",
            icon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = "Personal",
            hasNews = false,
            badgeCount = 45
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                ),
                title = {
                    Text(
                        text = items[selectedItemIndex].title,
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )
                },
            )
        }, bottomBar = {
            NavigationBar(
                containerColor = Color.White,
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(selected = selectedItemIndex == index, onClick = {
                        selectedItemIndex = index
                        navController.navigate(item.route) {
                            // Evitar duplicados en la pila de navegación
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, icon = {
                        BadgedBox(badge = {}) {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.icon
                                } else item.unselectedIcon,
                                contentDescription = item.title,
                            )
                        }
                    })
                }
            }
        }) { paddingValues ->
            NavHost(
                navController = navController, startDestination = "StartActivity", modifier = Modifier.padding(paddingValues)
            ) {
                composable("StartActivity") {
                    StartActivityView(sessionViewModel, personalViewModel )
                }
                composable("Develop") {
                    DevelopView(modifier, auxNavController, authViewModel)
                }
                composable("WeekStats") {
                    WeekStatsView(StatsViewModel(sessionViewModel), personalViewModel)
                }
                composable("Personal") {
                   PersonalView(personalViewModel)
                }
            }
        }
    }
}