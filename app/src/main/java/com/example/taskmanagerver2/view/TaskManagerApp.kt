package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.taskmanagerver2.model.database.TasksDbEntity

@Composable
fun TaskManagerApp(application: Application, navController: NavHostController){

    NavHost(navController = navController, startDestination = Screen.TaskListScreen.route) {
        composable(route=Screen.TaskListScreen.route){
            TaskListScreen(application, navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("task", it)
                navController.navigate(Screen.ContentScreen.route)
            })
        }
        composable(route=Screen.ContentScreen.route){
            val task = navController.previousBackStackEntry?.savedStateHandle?.get<TasksDbEntity>("task")
                ?: TasksDbEntity(0,"", "", "", "")
            ContentScreen(application, item=task)
        }
    }

}
