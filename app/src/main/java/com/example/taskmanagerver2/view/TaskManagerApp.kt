package com.example.taskmanagerver2.view

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagerver2.KanbanScreen
import com.example.taskmanagerver2.model.database.TasksDbEntity

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskManagerApp(application: Application) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.DashboardScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.TaskListScreen.route) {
                val status = navController.previousBackStackEntry?.savedStateHandle?.get<String>("status") ?: ""
                TaskListScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                }, statusFromDashboard = status)
            }
            composable(route = Screen.DashboardScreen.route) {
                DashboardScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                },
                    navigateToStatus = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("status", it)
                        navController.navigate(Screen.TaskListScreen.route)
                    })
            }
            composable(route = Screen.ContentScreen.route) {
                val task = navController.previousBackStackEntry?.savedStateHandle?.get<TasksDbEntity>("task")
                    ?: TasksDbEntity(0, "", "", "", "")
                ContentScreen(application, item = task, navigateToList = {
                    navController.navigate(Screen.TaskListScreen.route)
                })
            }
            composable(route = Screen.KanbanScreen.route) {
                val task = navController.previousBackStackEntry?.savedStateHandle?.get<TasksDbEntity>("task")
                    ?: TasksDbEntity(0, "", "", "", "")
                KanbanScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                })
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.DashboardScreen,
        Screen.TaskListScreen,
        Screen.KanbanScreen
    )
    BottomNavigation(
        modifier = Modifier.background(Color(0xff364958))
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(screen.label,
                    color = Color.White) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to avoid building up a large stack of destinations
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}
