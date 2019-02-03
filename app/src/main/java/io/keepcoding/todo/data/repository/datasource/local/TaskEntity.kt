package io.keepcoding.todo.data.repository.datasource.local

import androidx.room.*
import java.util.*

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parent_id"),
        onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index(value = ["parent_id"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "parent_id")
    val parentId: Long?,
    val content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean,
    @ColumnInfo(name = "priority_level")
    val priorityLevel: Int
)