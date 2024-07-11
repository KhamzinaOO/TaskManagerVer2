package com.example.taskmanagerver2.view.navigation

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagerver2.KanbanScreen
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.view.ContentScreen
import com.example.taskmanagerver2.view.DashboardScreen
import com.example.taskmanagerver2.view.TaskListScreen

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
            composable(route = Screen.DashboardScreen.route) {
                DashboardScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                }, navigateToStatus = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("status", it)
                    navController.navigate(Screen.TaskListScreen.route)
                })
            }
            composable(route = Screen.TaskListScreen.route) {
                val status = navController.previousBackStackEntry?.savedStateHandle?.get<String>("status") ?: ""
                TaskListScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                }, statusFromDashboard = status)
            }
            composable(route = Screen.ContentScreen.route) {
                val task = navController.previousBackStackEntry?.savedStateHandle?.get<TasksDbEntity>("task")
                    ?: TasksDbEntity(0, "", "", "", "")
                ContentScreen(application, item = task, navigateToList = {
                    navController.navigate(Screen.TaskListScreen.route)
                })
            }
            composable(route = Screen.KanbanScreen.route) {
                KanbanScreen(application, navigateToDetail = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                    navController.navigate(Screen.ContentScreen.route)
                })
            }
        }
    }
}