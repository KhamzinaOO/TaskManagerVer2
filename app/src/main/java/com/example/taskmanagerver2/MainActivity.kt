package com.example.taskmanagerver2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.taskmanagerver2.ui.theme.TaskManagerVer2Theme
import com.example.taskmanagerver2.view.TaskManagerApp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            TaskManagerVer2Theme {
                HideSystemUI()
                Surface(

                    modifier = Modifier.fillMaxSize(),
//                        .padding(top=48.dp),
                    color = MaterialTheme.colorScheme.background
                ) {

                    TaskManagerApp(application = application)
                    //KanbanBoard(application = application, navigateToDetail = {})
                }
            }
        }
    }

}
@Composable
fun HideSystemUI() {
    val systemUiController = rememberSystemUiController()
    systemUiController.isStatusBarVisible = false
    systemUiController.isNavigationBarVisible = false // Optional, to hide the navigation bar as well
}