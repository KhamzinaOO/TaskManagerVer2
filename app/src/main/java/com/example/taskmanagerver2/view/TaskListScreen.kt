package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.TagAndColor
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory
import java.util.Date

class Tag(
    tag:String,
    color: Color
)

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
    var deadline: Date? by remember { mutableStateOf(null) }
    var status by remember { mutableStateOf("В работе") }
    val tagsList = remember { mutableStateListOf<String>()}
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
                        selectedStatus = if(selectedStatus == item.name) "" else item.name
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
                        isSelectedTag = if(isSelectedTag == item.name) "" else item.name
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
            items(if(statusFromButton !="" && tagsFromButton !="") tasksByStatus.intersect(tasksByTag).toList()
            else if(statusFromButton !="") tasksByStatus else if (tagsFromButton !="") tasksByTag
            else taskList) { item ->
                TaskListItem(
                    item = item,
                    onDeleteClick = {tasksViewModel.deleteTask(item) },
                    onTagClick =  {
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
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                    status = "В работе"
                    tagsList.removeAll(tagsList)
                                   },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            onClick = {
                            if (itemName.isNotBlank() || itemContent.isNotBlank()) {
                                tasksViewModel.insertTask(
                                    TasksDbEntity(
                                        title = itemName,
                                        content = itemContent,
                                        status = status,
                                        tag = if(tagsList.isNotEmpty()) tagsList.joinToString(separator = ",") else "none"
                                    )
                                )
                                showDialog = false
                                itemName = ""
                                itemContent = ""
                                status = "В работе"
                                tagsList.removeAll(tagsList)
                            }
                        }) {
                            Text("Добавить")
                        }
                        Button(onClick = {
                            showDialog = false
                            tagsList.removeAll(tagsList)
                        }) {
                            Text("Отменить")
                        }
                    }
                },
                title = { Text("Добавить задачу:") },
                text = {
                    Column {
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

                                val columns = 2

                                LazyVerticalStaggeredGrid(
                                    columns = StaggeredGridCells.Fixed(columns),
                                    modifier = Modifier.width(155.dp),
                                ) {
                                    items(tagsList) { item ->
                                        tagColorList.find { it.name == item }?.color?.let { color ->
                                            Box(
                                                modifier = Modifier
                                                    .padding(1.dp)
                                                    .background(
                                                        color = color,
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .border(
                                                        border = BorderStroke(1.dp, Color.DarkGray),
                                                        shape = RoundedCornerShape(20.dp)
                                                    )
                                                    .padding(8.dp, 4.dp)
                                                    .wrapContentWidth()
                                                    .clickable { tagsList.remove(item) },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    item,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }
                                IconButton(onClick = { tagExpanded = true }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")}
                                }

                            DropdownMenu(
                                expanded = tagExpanded,
                                onDismissRequest = { tagExpanded = false },
                                offset = DpOffset(x = 150.dp, y = 0.dp)
                            ) {
                                for(item in tagColorList.dropLast(1)){
                                    DropdownMenuItem(
                                        text = { Text(item.name, fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                                        onClick = {
                                            if(!tagsList.contains(item.name)){
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
                            ){
                                Text("Статус: ",
                                    fontSize = 16.sp)
                                Text(
                                    text = status,
                                    fontSize = 16.sp
                                )
                                IconButton(onClick = { statusExpanded = true }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")}
                            }
                            DropdownMenu(
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false },
                                offset = DpOffset(x = 130.dp, y = 0.dp)
                            ) {
                                for(item in statusColorList){
                                    DropdownMenuItem(
                                        text = { Text(item.name, fontSize=18.sp, modifier = Modifier.padding(10.dp)) },
                                        onClick = { status = item.name }
                                    )
                                }
                            }
                        }

                    }
                }
            )
        }
    }
}

@Composable
fun TaskListItem(
    item: TasksDbEntity,
    onDeleteClick: () -> Unit,
    onTagClick: (String) -> Unit,
    tagColorList: List<TagAndColor>,
    statusColorList: List<TagAndColor>,
    navigateToDetail: (TasksDbEntity) -> Unit
) {
    var lines by remember { mutableIntStateOf(1) }
    var arrowIcon by remember { mutableStateOf(Icons.Default.KeyboardArrowDown) }
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(
                border = BorderStroke(1.dp, Color.DarkGray),
                shape = RoundedCornerShape(20.dp)
            )
            .fillMaxWidth()
            .clickable {
                navigateToDetail(item)
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                modifier = Modifier.padding(start = 16.dp)
            )
            statusColorList.find { it.name == item.status }?.color?.let {
                Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(
                        color = it,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp)
            }?.let {
                Row(
                    modifier = it,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.status,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tagsList = item.tag.split(",").map { it.trim() }
            val gridState = rememberLazyStaggeredGridState()

            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .width(180.dp)
                    .heightIn(30.dp, 100.dp)
            ) {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    modifier = Modifier.wrapContentWidth(),
                    state = gridState,
                    horizontalArrangement = Arrangement.Start,
                    verticalItemSpacing = 2.dp,
                ) {
                    items(tagsList) { tag ->
                        tagColorList.find { it.name == tag }?.let { tagColor ->
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .background(
                                        color = tagColor.color,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .border(
                                        border = BorderStroke(1.dp, Color.DarkGray),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(8.dp, 4.dp)
                                    .clickable { onTagClick(tag) },
                                    contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tag,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            Row {
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
                IconButton(onClick = {
                    if (lines == 1) {
                        lines = 50
                        arrowIcon = Icons.Default.KeyboardArrowUp
                    } else {
                        lines = 1
                        arrowIcon = Icons.Default.KeyboardArrowDown
                    }
                }) {
                    Icon(imageVector = arrowIcon, contentDescription = "")
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.content,
                textAlign = TextAlign.Left,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                maxLines = lines,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }
}