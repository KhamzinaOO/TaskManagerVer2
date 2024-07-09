package com.example.taskmanagerver2.view

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.Constants
import com.example.taskmanagerver2.model.Constants.statusColorList
import com.example.taskmanagerver2.model.Constants.tagColorList
import com.example.taskmanagerver2.model.TagAndColor
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory
import kotlin.math.roundToInt

@Composable
fun KanbanBoard(
    application: Application,
    navigateToDetail: (TasksDbEntity) -> Unit
) {
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))
    val taskList by tasksViewModel.allTasks.observeAsState(emptyList())
    val statuses = statusColorList

    var expandedStatus by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        for (status in statuses) {
            val weight = if (status.name == expandedStatus) 8f else 1f
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(weight)
                    .border(BorderStroke(0.2.dp, color = Color.DarkGray))
                    .background(Color(0xFFEEEEEE))
                    .clickable {
                        expandedStatus = if (expandedStatus == status.name) null else status.name
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .background(color = status.color)
                        .border(BorderStroke(0.3.dp, Color.DarkGray))
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Text(
                        if (expandedStatus == status.name || expandedStatus == null) status.name else "",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = if (expandedStatus == status.name) 11.sp else 7.sp,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                val tasksList = tasksViewModel.getTasksByStatus(status.name).observeAsState(
                    emptyList()
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    items(tasksList.value)  {
                            task ->
                        KanbanItem(
                            task = task,
                            navigateToDetail,
                            if (expandedStatus == status.name) true else false)
                    }
                }
            }
        }
    }
}

@Composable
fun KanbanItem(
    task: TasksDbEntity,
    navigateToDetail: (TasksDbEntity) -> Unit,
    isExpanded : Boolean
){
    val tagsList = task.tag.split(",").map { it.trim() }
    if(isExpanded){
        ExpandedItem(task, tagsList, navigateToDetail)
    }else{
        ItemHeading(task, tagsList, navigateToDetail)
    }

}

@Composable
fun ExpandedItem(
    task: TasksDbEntity,
    tagsList : List<String>,
    navigateToDetail: (TasksDbEntity) -> Unit
){
    var lines by remember { mutableIntStateOf(1) }
    var arrowIcon by remember { mutableStateOf(Icons.Default.KeyboardArrowDown) }
    Column (
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .border(BorderStroke(0.3.dp, Color.DarkGray), RoundedCornerShape(4.dp, 4.dp))
    ){
        ItemHeading(task = task, tagsList, navigateToDetail)
        Column(

        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = task.content,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.wrapContentWidth(),
                    maxLines = lines,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 6.sp
                )
                IconButton(
                    modifier = Modifier.size(10.dp),
                    onClick = {
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
            val gridState = rememberLazyStaggeredGridState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(30.dp, 100.dp),
                horizontalArrangement = Arrangement.Center
            )
            {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(3),
                    modifier = Modifier.wrapContentWidth(),
                    state = gridState,
                    horizontalArrangement = Arrangement.Start,
                    verticalItemSpacing = 1.dp,
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
                                        border = BorderStroke(0.2.dp, Color.DarkGray),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .padding(2.dp, 1.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    tag,
                                    fontSize = 5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemHeading(
    task: TasksDbEntity,
    tagsList : List<String>,
    navigateToDetail: (TasksDbEntity) -> Unit
){

    val color = tagColorList.find { it.name == tagsList[0] }?.color

    Row(
        Modifier
            .fillMaxWidth()
            .height(20.dp)
            .background(color ?: Color.White, RoundedCornerShape(4.dp, 4.dp))
            .border(BorderStroke(0.2.dp, Color.DarkGray), RoundedCornerShape(4.dp, 4.dp))
            .clickable {
                navigateToDetail
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(task.title,
            fontSize = 7.sp,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(4.dp,2.dp))
    }
}