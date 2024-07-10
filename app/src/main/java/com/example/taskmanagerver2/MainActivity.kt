package com.example.taskmanagerver2

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.database.AppDatabase
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.ui.theme.TaskManagerVer2Theme
import com.example.taskmanagerver2.view.DashboardScreen
import com.example.taskmanagerver2.view.KanbanBoard
import com.example.taskmanagerver2.view.TaskListScreen
import com.example.taskmanagerver2.view.TaskManagerApp
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TaskManagerVer2Theme {
                Surface(

                    modifier = Modifier.fillMaxSize()
                        .padding(top=48.dp),
                    color = MaterialTheme.colorScheme.background
                ) {

                    //TaskManagerApp(application = application, navController = navController)
                    //KanbanBoard(application = application, navigateToDetail = {})
                    MainScreen(application = application)
                }
            }
        }
    }

}
