package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanagerver2.model.database.TasksDbEntity

@Composable
fun TaskManagerApp(application: Application, navController: NavHostController){

    NavHost(navController = navController, startDestination = Screen.DashboardScreen.route) {
        composable(route=Screen.TaskListScreen.route){
            val status = navController.previousBackStackEntry?.savedStateHandle?.get<String>("status") ?: ""
            TaskListScreen(application, navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                navController.navigate(Screen.ContentScreen.route)
            }, statusFromDashboard = status)
        }
        composable(route=Screen.DashboardScreen.route){
            DashboardScreen(application, navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                navController.navigate(Screen.ContentScreen.route)
            },
                navigateToStatus = {
                    navController.currentBackStackEntry?.savedStateHandle?.set("status", it)
                    navController.navigate(Screen.TaskListScreen.route)
                })
        }
        composable(route=Screen.ContentScreen.route){
            val task = navController.previousBackStackEntry?.savedStateHandle?.get<TasksDbEntity>("task")
                ?: TasksDbEntity(0,"", "", "", "")
            ContentScreen(application, item=task, navigateToList = {
                navController.navigate(Screen.TaskListScreen.route)
            })
        }
    }

}
