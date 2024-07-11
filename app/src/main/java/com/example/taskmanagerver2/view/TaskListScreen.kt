package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.view.Components.TaskDialog
import com.example.taskmanagerver2.view.Components.TaskListItem
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(application: Application,
                   navigateToDetail: (TasksDbEntity) -> Unit,
                   statusFromDashboard : String) {
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))

    var statusFromButton by remember { mutableStateOf(statusFromDashboard) }
    var tagsFromButton by remember { mutableStateOf("") }

    val taskList by tasksViewModel.allTasks.observeAsState(emptyList())
    val tasksByStatus by tasksViewModel.getTasksByStatus(statusFromButton).observeAsState(emptyList())
    val tasksByTag by tasksViewModel.getTasksByTag(tagsFromButton).observeAsState(emptyList())

    var itemName by remember { mutableStateOf("") }
    var itemContent by remember { mutableStateOf("") }
    var dateOfCreation: Date? by remember { mutableStateOf(null) }
    var deadline: Date? by remember { mutableStateOf(null) }
    var status by remember { mutableStateOf("В работе") }
    val tagsList = remember { mutableStateListOf<String>() }
    var showDialog by remember { mutableStateOf(false) }
    var tagExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusColorList = Constants.statusColorList
    val tagColorList = Constants.tagColorList

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp, 16.dp, 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff364958))
        ) {
            Text("Добавить задачу")
        }

        var selectedStatus by remember { mutableStateOf(statusFromDashboard) }
        LazyRow(
            modifier = Modifier
                .padding(8.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(statusColorList) { item ->
                FilterChip(
                    modifier = Modifier.padding(3.dp),
                    onClick = {
                        selectedStatus = if (selectedStatus == item.name) "" else item.name
                        statusFromButton = if (selectedStatus == item.name) item.name else ""
                    },
                    label = {
                        Text(item.name)
                    },
                    selected = selectedStatus == item.name,
                    leadingIcon = if (selectedStatus == item.name) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = item.color)
                )
            }
        }

        var isSelectedTag by remember { mutableStateOf("") }
        LazyRow(
            modifier = Modifier
                .padding(8.dp, 0.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            items(tagColorList) { item ->
                FilterChip(
                    modifier = Modifier.padding(3.dp),
                    onClick = {
                        isSelectedTag = if (isSelectedTag == item.name) "" else item.name
                        tagsFromButton = if (isSelectedTag == item.name) item.name else ""
                    },
                    label = {
                        Text(item.name)
                    },
                    selected = isSelectedTag == item.name,
                    leadingIcon = if (isSelectedTag == item.name) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = item.color)
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(if (statusFromButton != "" && tagsFromButton != "") tasksByStatus.intersect(tasksByTag).toList()
            else if (statusFromButton != "") tasksByStatus else if (tagsFromButton != "") tasksByTag
            else taskList) { item ->
                TaskListItem(
                    item = item,
                    onDeleteClick = { tasksViewModel.deleteTask(item) },
                    onTagClick = {
                            tag -> tagsFromButton = tag
                        isSelectedTag = tag
                    },
                    tagColorList,
                    statusColorList,
                    navigateToDetail
                )
            }
        }

        if (showDialog) {
            TaskDialog(
                itemName = itemName,
                itemContent = itemContent,
                status = status,
                tagsList = tagsList,
                deadline = deadline,
                onItemNameChange = { itemName = it },
                onItemContentChange = { itemContent = it },
                onStatusChange = { status = it },
                onTagAdd = { tag -> tagsList.add(tag) },
                onTagRemove = { tag -> tagsList.remove(tag) },
                onDismiss = { showDialog = false },
                onConfirm = {
                    dateOfCreation = Date()
                    tasksViewModel.insertTask(
                        TasksDbEntity(
                            title = itemName,
                            content = itemContent,
                            status = status,
                            tag = if (tagsList.isNotEmpty()) tagsList.joinToString(separator = ",") else "none",
                            dateOfCreation = dateOfCreation,
                            deadline = deadline
                        )
                    )
                    showDialog = false
                    itemName = ""
                    itemContent = ""
                    status = "В работе"
                    tagsList.clear()
                },
                tagColorList = tagColorList,
                statusColorList = statusColorList
            )
        }
    }
}



