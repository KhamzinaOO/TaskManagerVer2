package com.example.taskmanagerver2

import com.example.taskmanagerver2.model.database.TasksDbEntity

data class Board(
    val id: Int,
    val title: String,
    var items: List<TasksDbEntity>
)
//
//data class BoardItem(
//    val id: String,
//    val title: String,
//    val longText: String,
//)