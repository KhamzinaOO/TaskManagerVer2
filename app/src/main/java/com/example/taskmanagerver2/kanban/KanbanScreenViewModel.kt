package com.example.taskmanagerver2

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.taskmanagerver2.model.Constants.statusColorList
import com.example.taskmanagerver2.model.database.TasksDbEntity

class KanbanScreenViewModel : ViewModel() {
    var state by mutableStateOf(
        KanbanScreenState()
    )

    init {
        val boards = mutableListOf<Board>()

        for(status in statusColorList){
            val boardId = statusColorList.indexOf(status)
            val boardTitle = status.name
            val boardItems = mutableStateListOf<TasksDbEntity>()

            boards.add(Board(boardId, boardTitle, boardItems))
        }
        state = state.copy(boards = boards)
    }

    fun onDragAndDropFinished(dropItem: TasksDbEntity, toBoard: Board) {
        // Find the board from which the item was dragged
        val fromBoard = state.boards.find { it.items.contains(dropItem) } ?: return

        if (fromBoard == toBoard) return
    }
}
