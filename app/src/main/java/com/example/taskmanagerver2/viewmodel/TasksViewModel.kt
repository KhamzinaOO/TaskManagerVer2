package com.example.taskmanagerver2.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.taskmanagerver2.model.database.AppDatabase
import com.example.taskmanagerver2.model.database.TasksDbEntity
import com.example.taskmanagerver2.model.repository.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class TasksViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TasksRepository
    val allTasks: LiveData<List<TasksDbEntity>>

    private val _isDialogVisible = MutableLiveData(false)
    val isDialogVisible: Boolean get() = _isDialogVisible.value ?: false


    init {
        val tasksDao = AppDatabase.getDatabase(application, viewModelScope).tasksDao()
        repository = TasksRepository(tasksDao)
        allTasks = repository.allTasks
    }

    fun getStatus(id: Int): LiveData<String?> = liveData {
        val status = repository.getStatus(id)
        emit(status)
    }

    fun getTags(id: Int): LiveData<String?> = liveData {
        val tags = repository.getTags(id)
        emit(tags)
    }

    fun getTasksByStatus(status: String): LiveData<List<TasksDbEntity>> = repository.getTaskByStatus(status)

    fun getTasksByTag(tag: String): LiveData<List<TasksDbEntity>> = repository.getTaskByTag(tag)

    fun getCountByStatus(status: String): LiveData<Int> = liveData {
        val count = repository.getCountByStatus(status)
        emit(count)
    }

    fun getCount(): LiveData<Int> = liveData {
        val count = repository.getCount()
        emit(count)
    }

    fun showDialog(show: Boolean) {
        _isDialogVisible.value = show
    }

    fun insertTask(task: TasksDbEntity) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun updateTask(task: TasksDbEntity) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: TasksDbEntity) = viewModelScope.launch {
        repository.deleteTask(task)
    }

    fun deleteTaskById(id : Int) = viewModelScope.launch {
        repository.deleteTaskById(id)
    }

    suspend fun saveTextToFile(context: Context, filename: String, content: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, filename)
                FileOutputStream(file).use {
                    it.write(content.toByteArray())
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}

class TasksViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TasksViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}