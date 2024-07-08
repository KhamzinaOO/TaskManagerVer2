package com.example.taskmanagerver2.view

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.ForeignKey
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.TagAndColor
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Date

@Composable
fun ContentScreen(
    application: Application,
    item: TasksDbEntity,
    navigateToList: () -> Unit
) {
    val navController = rememberNavController()
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))

    var statusFromButton by remember { mutableStateOf("") }
    var tagsFromButton by remember { mutableStateOf("") }

    val taskList by tasksViewModel.allTasks.observeAsState(emptyList())
    val tasksByStatus by tasksViewModel.getTasksByStatus(statusFromButton).observeAsState(emptyList())
    val tasksByTag by tasksViewModel.getTasksByTag(tagsFromButton).observeAsState(emptyList())

    var itemName by remember { mutableStateOf(item.title) }
    var itemContent by remember { mutableStateOf(item.content) }
    var deadline: Date? by remember { mutableStateOf(null) }
    var status by remember { mutableStateOf(item.status) }
    val tagsList = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    var tagExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(item.tag) {
        val tags = item.tag.split(",").map { it.trim() }
        tagsList.addAll(tags)
    }

    val statusColorList = Constants.statusColorList
    val tagColorList = Constants.tagColorList


    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val jsonString = remember {
        Gson().toJson(item)
    }

    var isSuccess by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(isSuccess) {
        isSuccess?.let {
            Toast.makeText(
                context,
                if (it) "File saved successfully" else "File save failed",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        modifier = Modifier.padding(8.dp, top = 80.dp, bottom = 8.dp)
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
            ) {
                Text(
                    "Тэги: ",
                    fontSize = 16.sp
                )
                val columns = 3

                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(1.dp),
                    verticalArrangement = Arrangement.spacedBy(1.dp),
                    horizontalArrangement = Arrangement.spacedBy(1.dp),
                    modifier = Modifier.width(220.dp)
                ) {
                    items(tagsList) { item ->
                        tagColorList.find { it.name == item }?.color?.let { color ->
                            OutlinedButton(
                                onClick = { tagsList.remove(item) },
                                modifier = Modifier
                                    .padding(1.dp)
                                    .height(20.dp)
                                    .width(55.dp),
                                contentPadding = PaddingValues(0.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = color
                                )
                            ) {
                                Text(
                                    text = item,
                                    fontSize = 12.sp,
                                    color = Color.Black,
                                    modifier = Modifier.padding(1.dp)
                                )
                            }
                        }
                    }
                }
                IconButton(onClick = { tagExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "arrowdown"
                    )
                }
            }

            DropdownMenu(
                expanded = tagExpanded,
                onDismissRequest = { tagExpanded = false },
                offset = DpOffset(x = 195.dp, y = (-80).dp)
            ) {
                for (item in tagColorList.dropLast(1)) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                item.name,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        },
                        onClick = {
                            if (!tagsList.contains(item.name)) {
                                tagsList.add(item.name)
                            }
                        }
                    )
                }
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
            ) {
                Text(
                    "Статус: ",
                    fontSize = 16.sp
                )
                Text(
                    text = status,
                    fontSize = 16.sp
                )
                IconButton(onClick = { statusExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "arrowdown"
                    )
                }
            }
            DropdownMenu(
                expanded = statusExpanded,
                onDismissRequest = { statusExpanded = false },
                offset = DpOffset(x = 185.dp, y = (-80).dp)
            ) {
                for (item in statusColorList) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                item.name,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        },
                        onClick = { status = item.name }
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                tasksViewModel.insertTask(
                    TasksDbEntity(
                        taskId = item.taskId,
                        title = itemName,
                        content = itemContent,
                        status = status,
                        tag = if (tagsList.isNotEmpty()) tagsList.joinToString(separator = ",") else "none"
                    )
                )
                Toast.makeText(application, "задача изменена", Toast.LENGTH_LONG).show()
            }) {
                Text(text = "Изменить")
            }
            Button(onClick = {
                tasksViewModel.deleteTask(item)
                navigateToList()
            }) {
                Text(text = "Удалить")
            }
            Button(onClick = {
                coroutineScope.launch {
                    isSuccess = saveTextToFile(context, "${itemName}.txt", jsonString)
                }
            }) {
                Text("Save to File")
            }
        }
    }
}

suspend fun saveTextToFile(context: Context, filename: String, content: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            val file = File(context.filesDir, filename)
            FileOutputStream(file).use {
                it.write(content.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}