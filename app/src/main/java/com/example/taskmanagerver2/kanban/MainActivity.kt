package com.example.taskmanagerver2

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.viewmodel.TasksViewModel
import com.example.taskmanagerver2.viewmodel.TasksViewModelFactory


@Composable
fun MainScreen(application : Application) {
    val tasksViewModel: TasksViewModel = viewModel(factory = TasksViewModelFactory(application))
    val viewModel: KanbanScreenViewModel = viewModel()

    for(board in viewModel.state.boards){
        val status = board.title
        val taskList = tasksViewModel.getTasksByStatus(status).observeAsState(emptyList())
        board.items = taskList.value
    }

    LongPressDraggable {
        Scaffold { padding ->
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp)
            ) {
                items(viewModel.state.boards, key = {
                    it.id
                }) { board ->
                    DropTarget<TasksDbEntity>(modifier = Modifier.fillMaxSize()) { isInBound, dropItem ->
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
                            Color.Red
                        } else {
                            Color.Unspecified
                        }
                        Column(Modifier.background(bgColor)) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = board.title,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp,
                            )
                            LazyColumn {
                                items(board.items, key = {
                                    it.taskId
                                }) { boardItem ->
                                    BoardContentItem(boardItem = boardItem)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}