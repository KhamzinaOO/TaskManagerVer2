package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.TagAndColor
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory

@Composable
fun DashboardScreen(application: Application,
                    navigateToDetail: (TasksDbEntity) -> Unit,
                    navigateToStatus :(String)->Unit,
                    ){

    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))
    val importantTasks by tasksViewModel.getTasksByTag("срочно").observeAsState(emptyList())

    val statusColorList = Constants.statusColorList + TagAndColor("Все",Color.White)
    val tagColorList = Constants.tagColorList

    val itemHeight = 80.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, top = 56.dp, 16.dp)
            .fillMaxSize()
    ){
        Text("Дашборд",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text("Статистика задач",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp))
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 140.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(statusColorList.size) { index ->
                    val item = statusColorList[index]
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .height(itemHeight)
                            .border(
                                1.dp,
                                Color.DarkGray,
                                RoundedCornerShape(15.dp)
                            )
                            .background(
                                item.color,
                                RoundedCornerShape(15.dp)
                            )
                            .clickable { navigateToStatus(if (item.name == "Все") "" else item.name) }
                    )
                    {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, 8.dp)
                        ) {
                            Text(
                                fontSize = 24.sp,
                                text = if(item.name=="Все") tasksViewModel.getCount().observeAsState(0).value.toString()
                                else tasksViewModel.getCountByStatus(item.name).observeAsState(0).value.toString()
                            )
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = item.name,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
        Text("Срочные задачи",
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp))
        LazyRow(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
        ){
            items(importantTasks){
                    item ->
                TaskListItem(
                    item,
                    onTagClick = {},
                    onDeleteClick = {tasksViewModel.deleteTask(item) },
                    tagColorList = tagColorList,
                    statusColorList = statusColorList,
                    navigateToDetail = navigateToDetail
                )
            }
        }
    }
}

