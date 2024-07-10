package com.example.taskmanagerver2

import android.app.Application
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.taskmanagerver2.model.Constants.statusColorList
import com.example.taskmanagerver2.model.Constants.tagColorList
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory


@Composable
fun MainScreen(application: Application) {
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))
    val viewModel: KanbanScreenViewModel = viewModel()
    val listState = rememberLazyListState()

    for (board in viewModel.state.boards) {
        val status = board.title
        val taskList = tasksViewModel.getTasksByStatus(status).observeAsState(emptyList())
        board.items = taskList.value
    }

    var expandedStatus by remember { mutableStateOf<String?>(null) }

    LongPressDraggable {
        Scaffold { padding ->
            val columnWidth = 200.dp  // Specify the desired width for each column
            LazyRow(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp)
            ) {
                items(viewModel.state.boards, key = { it.id }) { board ->
                    DropTarget<TasksDbEntity>(modifier = Modifier.width(columnWidth)) { isInBound, dropItem ->
                        dropItem?.let {
                            if (isInBound) {
                                viewModel.onDragAndDropFinished(dropItem, board)
                                tasksViewModel.updateTask(
                                    TasksDbEntity(
                                        dropItem.taskId,
                                        dropItem.title,
                                        dropItem.content,
                                        dropItem.tag,
                                        board.title
                                    )
                                )
                            }
                        }
                        val bgColor = if (isInBound) {
                            Color.LightGray
                        } else {
                            Color.Unspecified
                        }
                        Column(
                            Modifier
                                .border(1.dp, Color.Black)
                                .background(bgColor)
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = board.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                            )
                            LazyColumn(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                items(board.items, key = { it.taskId }) { boardItem ->
                                    BoardContentItem(
                                        modifier = Modifier.padding(4.dp),
                                        boardItem = boardItem,
                                        isExpanded = false,
                                        listState = listState
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}