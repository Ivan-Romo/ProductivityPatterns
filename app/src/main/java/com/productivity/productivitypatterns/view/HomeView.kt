package com.productivity.productivitypatterns.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.productivity.productivitypatterns.BottomNavigationItem
import com.productivity.productivitypatterns.ui.theme.InterFontFamily
import com.productivity.productivitypatterns.viewmodel.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier,
    auxNavController: NavController,
    authViewModel: AuthViewModel,
    activity: Activity
) {
    var context = LocalContext.current
    var sessionViewModel =  LocalSessionViewModel(context)
    var adManager = AdManager(context)
    var personalViewModel = PersonalViewModel(context)
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "Session", icon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home, route = "StartActivity"
        ),
        BottomNavigationItem(
            title = "Week Stats",
            icon = Icons.Filled.BarChart,
            unselectedIcon = Icons.Outlined.BarChart,
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
        modifier = Modifier.fillMaxSize(), color = colorScheme.background
    ) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.surface
                ), title = {

                    Text(
                        text = items[selectedItemIndex].title, style = androidx.compose.ui.text.TextStyle(
                            fontFamily = InterFontFamily,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    )

                }, modifier = Modifier
                    .shadow(4.dp) // Añade una sombra de 4.dp
                    .background(colorScheme.background)
            )
        }, bottomBar = {
            NavigationBar(
                contentColor = Color.Red,
                containerColor = colorScheme.tertiaryContainer,
                modifier = Modifier

                    .clip(
                        RoundedCornerShape(
                            topStart = 30.dp, topEnd = 30.dp
                        )
                    )
                    .background(colorScheme.tertiaryContainer)  // Fondo del NavigationBar para que la sombra sea visible
                    .graphicsLayer {
                        clip = true
                        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                    },
                tonalElevation = 10.dp,
            ) {

                items.forEachIndexed { index, item ->
                    NavigationBarItem(selected = selectedItemIndex == index, onClick = {
                        selectedItemIndex = index
                        navController.navigate(item.route) {
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
                                tint = if (index == selectedItemIndex) {
                                    colorScheme.background
                                } else colorScheme.onSurface
                            )
                        }
                    })
                }
            }

        }) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "StartActivity",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("StartActivity") {
                    StartActivityView(sessionViewModel, personalViewModel, activity)
                }
                composable("Develop") {
                    DevelopView(modifier, auxNavController, authViewModel, personalViewModel = personalViewModel)
                }
                composable("WeekStats") {
                    WeekStatsView(StatsViewModel(sessionViewModel.sessionList.value), personalViewModel, sessionViewModel)
                }
                composable("Personal") {
                    PersonalView(personalViewModel, authViewModel)
                }
            }
        }
    }
}

