package com.example.taskmanagerver2.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    object DashboardScreen : Screen("dashboard", Icons.Default.Home, "Dashboard")
    object TaskListScreen : Screen("tasklist", Icons.Default.List, "Tasks")
    object KanbanScreen : Screen("kanban", Icons.Default.DateRange, "Kanban")
    object ContentScreen : Screen("content", Icons.Default.List, "Content") // Assuming there's no bottom nav item for ContentScreen
}