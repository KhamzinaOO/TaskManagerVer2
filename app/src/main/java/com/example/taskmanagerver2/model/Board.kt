package com.example.taskmanagerver2.model

import com.example.taskmanagerver2.model.database.TasksDbEntity

data class Board(
    val id: Int,
    val title: String,
    var items: List<TasksDbEntity>
)