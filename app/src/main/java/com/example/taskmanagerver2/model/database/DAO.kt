package com.example.taskmanagerver2.model.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.util.Date

@Dao
interface TasksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TasksDbEntity)

    @Update
    suspend fun updateTask(task: TasksDbEntity)

    @Delete
    suspend fun deleteTask(task: TasksDbEntity)

    @Query("DELETE FROM Tasks WHERE taskId = :taskId")
    suspend fun deleteTaskByID(taskId: Int)

    @Query("SELECT * FROM Tasks")
    fun getAllTasks(): LiveData<List<TasksDbEntity>>

    @Transaction
    @Query("SELECT COUNT(*) FROM Tasks")
    suspend fun getCount(): Int

    @Transaction
    @Query("SELECT status FROM Tasks WHERE taskId = :id LIMIT 1")
    suspend fun getStatus(id: Int): String?

    @Transaction
    @Query("SELECT * FROM Tasks WHERE status = :status")
    fun getTaskByStatus(status: String): LiveData<List<TasksDbEntity>>

    @Transaction
    @Query("SELECT COUNT(*) FROM Tasks WHERE status = :status")
    suspend fun getCountByStatus(status: String): Int

    @Transaction
    @Query("SELECT * FROM Tasks WHERE instr(Tasks.tag, :tag)>0")
    fun getTaskByTag(tag: String): LiveData<List<TasksDbEntity>>

    @Transaction
    @Query("SELECT status FROM Tasks WHERE taskId = :id LIMIT 1")
    suspend fun getTags(id: Int): String?

//    @Query("SELECT * FROM Tasks WHERE statusId = :statusId")
//    suspend fun getTasksByStatus(statusId: Int): List<TasksDbEntity>
//
//    @Query("SELECT * FROM Tasks WHERE date = :date")
//    suspend fun getTasksByDate(date: Date): List<TasksDbEntity>
}