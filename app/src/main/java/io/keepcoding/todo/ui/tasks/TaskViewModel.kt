package io.keepcoding.todo.ui.tasks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.keepcoding.todo.data.model.Task
import io.keepcoding.todo.data.repository.TaskRepository
import io.keepcoding.todo.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

class TaskViewModel(val taskRepository: TaskRepository) : BaseViewModel() {

    val tasksEvent = MutableLiveData<List<Task>>()

    init {
        loadTasks()
    }

    fun loadTasks() {
        taskRepository
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { tasks ->
                    tasksEvent.value = tasks
                },
                onError = {
                    Log.e("TaskViewModel", "Error: $it")
                }
            ).addTo(compositeDisposable)
    }

}