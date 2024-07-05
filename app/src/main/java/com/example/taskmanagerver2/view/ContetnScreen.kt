package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory
import java.util.Date

@Composable
fun ContentScreen(application: Application, item : TasksDbEntity){

    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))

    var statusFromButton by remember { mutableStateOf("") }
    var tagsFromButton by remember { mutableStateOf("") }

    val taskList by tasksViewModel.allTasks.observeAsState(emptyList())
    val tasksByStatus by tasksViewModel.getTasksByStatus(statusFromButton).observeAsState(emptyList())
    val tasksByTag by tasksViewModel.getTasksByTag(tagsFromButton).observeAsState(emptyList())


    var itemName by remember { mutableStateOf("") }
    var itemContent by remember { mutableStateOf("") }
    var deadline: Date? by remember { mutableStateOf(null) }
    var status by remember { mutableStateOf("В процессе") }
    var tagsList = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    var tagExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statuses = listOf("Завершено", "В процессе", "Отложено")
    val tagsColors = listOf(Color(0xffffe5d9), Color(0xffd8e2dc), Color(0xffffcad4), Color.Gray)
    val tags = listOf("работа", "встреча", "срочно", "none")


    Column(
         modifier = Modifier
        .padding(16.dp, 48.dp, 16.dp, 8.dp),
    verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            singleLine = true,
            placeholder = { Text("Задача") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)

        )
        OutlinedTextField(
            value = itemContent,
            onValueChange = { itemContent = it },
            singleLine = false,
            minLines = 4,
            placeholder = { Text("Описание..") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .width(500.dp)
        )
        Box {
            Row(
                Modifier
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { tagExpanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Тэги: ",
                    fontSize = 16.sp)
                Row(){
                    for (item in tagsList){
                        OutlinedButton(onClick = { tagsList.remove(item)},
                            modifier = Modifier
                                .size(50.dp, 20.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = tagsColors[tags.indexOf(item)])
                        ) {
                            Text(text = item,
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black)
                        }
                    }
                }
                IconButton(onClick = { tagExpanded = true }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")
                }
            }

            DropdownMenu(
                expanded = tagExpanded,
                onDismissRequest = { tagExpanded = false },
                offset = DpOffset(x = 150.dp, y = 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(tags[0], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = {
                        if(!tagsList.contains(tags[0])){
                            tagsList.add(tags[0])
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text(tags[1], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = {
                        if(!tagsList.contains(tags[1])){
                            tagsList.add(tags[1])
                        }
                    }
                )
                DropdownMenuItem(
                    text = { Text(tags[2], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = {
                        if(!tagsList.contains(tags[2])){
                            tagsList.add(tags[2])
                        }
                    }
                )
            }
        }
        Box {
            Row(
                Modifier
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Gray,
                        shape = RoundedCornerShape(3.dp)
                    )
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { statusExpanded = true },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Статус: ",
                    fontSize = 16.sp)
                Text(
                    text = status,
                    fontSize = 16.sp
                )
                IconButton(onClick = { statusExpanded = true }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")
                }
            }
            DropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false },
                offset = DpOffset(x = 130.dp, y = 0.dp)
            ) {
                DropdownMenuItem(
                    text = { Text(statuses[0], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = { status = statuses[0] }
                )
                DropdownMenuItem(
                    text = { Text(statuses[1], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = { status = statuses[1] }
                )
                DropdownMenuItem(
                    text = { Text(statuses[2], fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                    onClick = { status = statuses[2] }
                )
            }
        }
        Button(onClick = { tasksViewModel.insertTask(TasksDbEntity(
            title = itemName,
            content = itemContent,
            tag = if(tagsList.isNotEmpty()) tagsList.joinToString(separator = ",") else "none",
            status = status
            )) }) {
                Text(text = "Добавить")
        }
    }
}