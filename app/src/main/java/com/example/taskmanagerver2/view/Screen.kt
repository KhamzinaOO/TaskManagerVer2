package com.example.taskmanagerver2.view

sealed class Screen (val route: String){
    data object ContentScreen:Screen("contentscreen")
    data object TaskListScreen:Screen("tasklistscreen")
}
