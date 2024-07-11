package com.example.taskmanagerver2.model.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@Parcelize
@Entity(tableName = "Tasks")
data class TasksDbEntity(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val tag: String,
    @ColumnInfo val status: String,
    @ColumnInfo val dateOfCreation: Date? = null,
    @ColumnInfo val deadline: Date? = null
) : Parcelable

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}