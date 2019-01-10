package io.keepcoding.todo.data.model

import java.util.Date

data class Task(
    val id: Long,
    val content: String,
    val createdAt: Date,
    val isDone: Boolean
)