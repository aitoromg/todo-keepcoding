package io.keepcoding.todo.data.model.mapper

import io.keepcoding.todo.data.model.Task
import io.keepcoding.todo.data.repository.datasource.local.TaskEntity

class TaskMapper : Mapper<TaskEntity, Task> {

    override fun transform(input: TaskEntity): Task =
        Task(
            input.id,
            input.parentId,
            input.content,
            input.createdAt,
            input.isDone,
            input.priorityLevel
        )

    override fun transformList(input: List<TaskEntity>): List<Task> =
            input.map { transform(it) }

}