package io.keepcoding.todo.ui.edittask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.DialogInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.keepcoding.todo.R
import io.keepcoding.todo.data.model.Task
import io.keepcoding.todo.ui.tasks.TaskViewModel
import io.keepcoding.todo.util.*
import kotlinx.android.synthetic.main.fragment_edit_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditTaskFragment : BottomSheetDialog() {

    private var listener: OnDismissListener? = null

    interface OnDismissListener {
        fun onDismissListener()
    }

    companion object {
        const val PARAM_TASK = "task"

        fun newInstance(task: Task): EditTaskFragment =
            EditTaskFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAM_TASK, task)
                }
            }
    }

    private val taskViewModel: TaskViewModel by viewModel()

    private var task: Task? = null

    private var priorityLevel: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_task, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        task = arguments?.let {
            it.getParcelable(PARAM_TASK)
        }
        if (task == null) {
            dismiss()
        }

        setUp()
    }

    private fun setUp() {
        fillData()
        bindEvents()
        bindActions()
    }

    private fun bindEvents() {
        with (taskViewModel) {
            taskUpdatedEvent.observe(this@EditTaskFragment, Observer {
                dismiss()
            })
        }
    }

    private fun fillData() {
        requireNotNull(task) {
            "Task is null dialog should be closed"
        }

        inputTaskContent.setText(task!!.content)
        when (task!!.priorityLevel){
            PRIORITY_LOW -> priorityLow.isChecked = true
            PRIORITY_MID -> priorityMid.isChecked = true
            PRIORITY_HIGH -> priorityHigh.isChecked = true
        }
    }

    private fun bindActions() {
        buttonSaveTask.setOnClickListener {
            val newTask = task!!.copy(
                content = inputTaskContent.text.toString(),
                priorityLevel = priorityLevel
            )

            taskViewModel.updateTask(newTask)
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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener?.let {
            it.onDismissListener()
        }
    }

    fun setOnDismissListener(listener: OnDismissListener) {
        this.listener = listener
    }

}