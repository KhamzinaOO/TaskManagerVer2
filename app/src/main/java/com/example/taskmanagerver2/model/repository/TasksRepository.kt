package com.example.taskmanagerver2.model.repository

import androidx.lifecycle.LiveData
import com.example.taskmanagerver2.model.database.TasksDao
import com.example.taskmanagerver2.model.database.TasksDbEntity
import java.util.Date

class TasksRepository(
    private val tasksDao: TasksDao,
) {

    suspend fun insertTask(task: TasksDbEntity) = tasksDao.insertTask(task)
    suspend fun updateTask(task: TasksDbEntity) = tasksDao.updateTask(task)
    suspend fun deleteTask(task: TasksDbEntity) = tasksDao.deleteTask(task)
    suspend fun deleteTaskById(id: Int) = tasksDao.deleteTaskByID(id)
    val allTasks: LiveData<List<TasksDbEntity>> = tasksDao.getAllTasks()
    suspend fun getStatus(id:Int) = tasksDao.getStatus(id)
    suspend fun getTags(id:Int) = tasksDao.getTags(id)
    fun getTaskByTag(tag:String) = tasksDao.getTaskByTag(tag)
    fun getTaskByStatus(status:String) = tasksDao.getTaskByStatus(status)
//    suspend fun getTasksByStatus(statusId: Int) = tasksDao.getTasksByStatus(statusId)
//    suspend fun getTasksByDate(date: Date) = tasksDao.getTasksByDate(date)

}