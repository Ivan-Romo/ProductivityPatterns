package com.example.productivitypatterns.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productivitypatterns.BottomNavigationItem
import com.example.productivitypatterns.viewmodel.AuthState
import com.example.productivitypatterns.viewmodel.AuthViewModel
import com.example.productivitypatterns.viewmodel.SessionViewModel


@Composable
fun HomeAux(modifier: Modifier = Modifier,
            auxNavController: NavController,
            authViewModel: AuthViewModel,
            sessionViewModel: SessionViewModel = viewModel()
) {
    val navController = rememberNavController()
    val items = listOf(
        BottomNavigationItem(
            title = "HomeView",
            icon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = "HomeView"
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
            NavHost(
                navController = navController,
                startDestination = "HomeView",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("HomeView") {
                    HomeView(modifier, auxNavController, authViewModel)
                }
                composable("WeekStats") {
                    WeekStatsView()
                }
            }
        }
    }
}



@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    sessionViewModel: SessionViewModel = viewModel()
) {
    val authState = authViewModel.authState.observeAsState()

    val sessions by sessionViewModel.sessionList.collectAsStateWithLifecycle()

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

        for (session in sessions) {
            Text(session.id.toString() + " " + session.type + session.datetime)
        }

        Button(onClick = {
            sessionViewModel.createSession()
        }, modifier = Modifier.fillMaxWidth()) {}




        TextButton(onClick = {
            authViewModel.signout()
        }) {
            Text(text = "Sign out")
        }
    }

}