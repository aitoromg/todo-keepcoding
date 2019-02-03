package io.keepcoding.todo.ui.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.keepcoding.todo.data.model.Task
import io.keepcoding.todo.data.repository.TaskRepository
import io.keepcoding.todo.ui.base.BaseViewModel
import io.keepcoding.todo.util.Event
import io.keepcoding.todo.util.call
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class TaskViewModel(val taskRepository: TaskRepository) : BaseViewModel() {

    val tasksEvent = MutableLiveData<List<Task>>()
    val taskEvent = MutableLiveData<Event<Task>>()
    val newTaskAddedEvent = MutableLiveData<Event<Unit>>()
    val taskDeletedEvent = MutableLiveData<Event<Unit>>()
    val taskUpdatedEvent = MutableLiveData<Event<Task>>()

    fun loadTasks() {
        taskRepository
            .observeAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { tasks ->
                    tasksEvent.value = tasks
                },
                onError = {
                    Log.e("TaskViewModel", "Error: $it")
                }
            ).addTo(compositeDisposable)
    }

    fun loadSubtasks(parentId: Long) {
        taskRepository
            .observeSubtasks(parentId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { tasks ->
                    tasksEvent.value = tasks
                },
                onError = {
                    Log.e("TaskViewModel", "Error: $it")
                }
            ).addTo(compositeDisposable)
    }

    fun getTask(id: Long) {
        taskRepository
            .getTaskById(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { task ->
                    taskEvent.call(task)
                },
                onError = {
                    Log.e("TaskViewModel", "Error: $it")
                }
            ).addTo(compositeDisposable)
    }

    fun addNewTask(taskContent: String, priorityLevel: Int, parentId: Long) {
        val newTask = Task(0, if (parentId == 0L) null else parentId, taskContent, Date(), false, priorityLevel)

        Completable.fromCallable {
            taskRepository.insert(newTask)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    newTaskAddedEvent.call()
                },
                onError = {
                    Log.e("TaskViewModel", "$it")
                }
            )
            .addTo(compositeDisposable)
    }

    fun deleteTask(task: Task) {
        Completable.fromCallable {
            taskRepository.delete(task)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    taskDeletedEvent.call()
                },
                onError = {
                    Log.e("TaskViewModel", "$it")
                }
            )
            .addTo(compositeDisposable)
    }

    fun markAsDone(task: Task) {
        if (task.isDone) {
            return
        }

        val newTask = task.copy(isDone = true)
        updateTask(newTask)
    }

    fun markAsNotDone(task: Task) {
        if (!task.isDone) {
            return
        }

        val newTask = task.copy(isDone = false)
        updateTask(newTask)
    }

    fun setPriority(task: Task, priorityLevel: Int) {
        val newTask = task.copy(priorityLevel = priorityLevel)
        updateTask(newTask)
    }

    fun updateTask(task: Task) {
        Completable.fromCallable {
            taskRepository.update(task)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    taskUpdatedEvent.call(task)
                },
                onError = {
                    Log.e("TaskViewModel", "$it")
                }
            )
            .addTo(compositeDisposable)
    }
}