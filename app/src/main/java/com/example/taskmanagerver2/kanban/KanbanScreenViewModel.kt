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

//        // Remove the item from the source board
//        val updatedSourceBoard = fromBoard.copy(
//            items = fromBoard.items.toMutableList().apply {
//                remove(dropItem)
//            }
//        )
//
//        // Add the item to the destination board
//        val updatedDestinationBoard = toBoard.copy(
//            items = toBoard.items.toMutableList().apply {
//                add(dropItem)
//            }
//        )
//
//        // Update the state with the changes
//        state = state.copy(
//            boards = state.boards.map { board ->
//                when (board) {
//                    fromBoard -> updatedSourceBoard
//                    toBoard -> updatedDestinationBoard
//                    else -> board
//                }
//            }
//        )
    }
}
