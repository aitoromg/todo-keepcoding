package io.keepcoding.todo.ui.newtask

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.Observer
import io.keepcoding.todo.R
import io.keepcoding.todo.ui.base.BaseActivity
import io.keepcoding.todo.ui.tasks.TaskViewModel
import io.keepcoding.todo.util.PRIORITY_LOW
import io.keepcoding.todo.util.PRIORITY_MID
import io.keepcoding.todo.util.PRIORITY_HIGH
import kotlinx.android.synthetic.main.activity_new_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewTaskActivity : BaseActivity() {

    private val taskViewModel: TaskViewModel by viewModel()

    private var priorityLevel: Int = 0
    private var parentId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)
        setUpToolbar(true)
        setTitle(R.string.new_task_title)

        parentId = intent.getLongExtra("parentId", 0)

        bindObserver()
        bindActions()

        priorityLow.isChecked = true
    }

    private fun bindObserver() {
        with (taskViewModel) {
            newTaskAddedEvent.observe(this@NewTaskActivity, Observer {
                if (!it.hasBeenHandled) {
                    it.getContentIfNotHandled()

                    setResult(Activity.RESULT_OK)
                    finish()
                }
            })
        }
    }

    private fun bindActions() {
        buttonSaveTask.setOnClickListener {
            taskViewModel.addNewTask(inputTaskContent.text.toString(), priorityLevel, parentId)
        }

        priorityLow.setOnClickListener {
            priorityLevel = PRIORITY_LOW
        }

        priorityMid.setOnClickListener {
            priorityLevel = PRIORITY_MID
        }

        priorityHigh.setOnClickListener {
            priorityLevel = PRIORITY_HIGH
        }
    }

}