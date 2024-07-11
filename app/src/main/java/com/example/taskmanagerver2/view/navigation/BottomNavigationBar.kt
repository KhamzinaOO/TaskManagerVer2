package com.example.taskmanagerver2.view.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        Screen.DashboardScreen,
        Screen.TaskListScreen,
        Screen.KanbanScreen
    )
    BottomNavigation(
        backgroundColor = Color(0xff364958),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(screen.label, color = Color.White) },
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route){
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
