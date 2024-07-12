package com.example.taskmanagerver2.view.Components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskmanagerver2.model.database.TasksDbEntity
import java.text.SimpleDateFormat

@Composable
fun TaskListItem(
    modifier: Modifier,
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
                        fontSize = 16.sp
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

            LazyGridContainer(
                Modifier,
                elements = tagsList,
                columns = StaggeredGridCells.Fixed(2),
                tagAndColorList = tagColorList) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Последнее изменение:")
            Text(
                text = item.dateOfCreation?.let { SimpleDateFormat("dd/MM/yyyy HH:mm").format(it) } ?: "None",
                fontSize = 12.sp
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text("Дедлайн:")
            Text(
                text = item.deadline?.let { SimpleDateFormat("dd/MM/yyyy HH:mm").format(it) } ?: "Не установлен",
                fontSize = 12.sp
            )

        }
    }
}