package com.example.taskmanagerver2.viewmodel

import com.example.taskmanagerver2.model.Board

data class KanbanScreenState(
    val boards: List<Board> = emptyList()
)
