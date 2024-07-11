package com.example.taskmanagerver2.view.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanagerver2.model.TagAndColor
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TaskDialog(
    itemName: String,
    itemContent: String,
    status: String,
    tagsList: SnapshotStateList<String>,
    deadline: Date?,
    onItemNameChange: (String) -> Unit,
    onItemContentChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
    onTagAdd: (String) -> Unit,
    onTagRemove: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    tagColorList: List<TagAndColor>,
    statusColorList: List<TagAndColor>
) {
    var tagExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }
    var deadlineDialogShown by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<Date?>(null) }

    if (deadlineDialogShown) {
        DateTimePickerDialog(
            initialDate = selectedDate ?: Date(),
            onDismissRequest = { deadlineDialogShown = false },
            onDateTimeSelected = { date ->
                selectedDate = date
                deadlineDialogShown = false
            }
        )
    }

    AlertDialog(
        onDismissRequest = {
            onDismiss()
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
                            onConfirm()
                        }
                    }) {
                    Text("Добавить")
                }
                Button(onClick = {
                    onDismiss()
                    tagsList.clear()
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
                    onValueChange = onItemNameChange,
                    singleLine = true,
                    placeholder = { Text("Задача") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = itemContent,
                    onValueChange = onItemContentChange,
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
                                            .clickable { onTagRemove(item) },
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
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")
                        }
                    }

                    DropdownMenu(
                        expanded = tagExpanded,
                        onDismissRequest = { tagExpanded = false },
                        offset = DpOffset(x = 150.dp, y = 0.dp)
                    ) {
                        for (item in tagColorList.dropLast(1)) {
                            DropdownMenuItem(
                                text = { Text(item.name, fontSize = 18.sp, modifier = Modifier.padding(10.dp)) },
                                onClick = {
                                    if (!tagsList.contains(item.name)) {
                                        onTagAdd(item.name)
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
                        for (item in statusColorList) {
                            DropdownMenuItem(
                                text = { Text(item.name, fontSize = 18.sp, modifier = Modifier.padding(10.dp)) },
                                onClick = { onStatusChange(item.name) }
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
                            .clickable { deadlineDialogShown = true },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Дедлайн: ",
                            fontSize = 16.sp)
                        Text(
                            text = selectedDate?.let { SimpleDateFormat("dd/MM/yyyy HH:mm").format(it) } ?: "Не установлен",
                            fontSize = 16.sp
                        )
                        IconButton(onClick = { deadlineDialogShown = true }) {
                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "arrowdown")
                        }
                    }
                }
            }
        }
    )
}