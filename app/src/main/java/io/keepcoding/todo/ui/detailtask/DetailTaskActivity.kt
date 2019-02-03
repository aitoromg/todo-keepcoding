package io.keepcoding.todo.ui.detailtask

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.view.clicks
import io.keepcoding.todo.R
import io.keepcoding.todo.data.model.Task
import io.keepcoding.todo.ui.base.BaseActivity
import io.keepcoding.todo.ui.tasks.TaskFragment
import io.keepcoding.todo.ui.tasks.TaskViewModel
import io.keepcoding.todo.ui.edittask.EditTaskFragment
import io.keepcoding.todo.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_detail_task.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DetailTaskActivity : BaseActivity(), EditTaskFragment.OnDismissListener {

    private val taskViewModel: TaskViewModel by viewModel()
    private val compositeDisposable = CompositeDisposable()
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_task)

        // Toolbar
        setUpToolbar(true)
        setTitle(R.string.detail_task_title)

        // Get task
        task = intent.getParcelableExtra("task")
        if (task == null) {
            setResult(Activity.RESULT_OK)
            finish()
        }

        fillData()
        bindEvents()
        bindActions()

        val fragment = TaskFragment()
        val bundle = Bundle()
        bundle.putLong("parentId", task!!.id)
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun bindEvents() {
        with (taskViewModel) {
            taskDeletedEvent.observe(this@DetailTaskActivity, Observer {
                setResult(Activity.RESULT_OK)
                finish()
            })

            taskEvent.observe(this@DetailTaskActivity, Observer {
                if (!it.hasBeenHandled) {
                    task = it.getContentIfNotHandled()
                    fillData()
                }
            })
        }
    }

    private fun fillData() {
        task?.let {
            textContent.text = it.content
            textDate.text = DateHelper.calculateTimeAgo(it.createdAt)
            checkIsDone.isChecked = it.isDone

            when (it.priorityLevel) {
                PRIORITY_LOW -> {
                    textPriority.text = getString(R.string.priority_low)
                    iconPriority.setColorDrawable(Color.BLUE)
                }
                PRIORITY_MID -> {
                    textPriority.text = getString(R.string.priority_mid)
                    iconPriority.setColorDrawable(Color.GREEN)
                }
                PRIORITY_HIGH -> {
                    textPriority.text = getString(R.string.priority_high)
                    iconPriority.setColorDrawable(Color.RED)
                }
            }
        }
    }

    private fun bindActions() {
        fabs
            .clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                Navigator.navigateToNewTaskActivity(task!!.id,this)
            }
            .addTo(compositeDisposable)

        checkIsDone.setOnClickListener {
            if (checkIsDone.isChecked) {
                taskViewModel.markAsDone(task!!)
            } else {
                taskViewModel.markAsNotDone(task!!)
            }
        }
    }

    override fun onDismissListener() {
        taskViewModel.getTask(task!!.id)
    }

    // Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_task, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item!!.itemId) {
        R.id.ac_edit -> {
            Navigator.navigateToEditTaskFragment(task!!, supportFragmentManager).setOnDismissListener(this)
            true
        }

        R.id.ac_delete -> {
            showConfirmDeleteTaskDialog(task!!)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showConfirmDeleteTaskDialog(task: Task) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_task_title)
            .setMessage(R.string.delete_task_message)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                taskViewModel.deleteTask(task)
            }
            .setNegativeButton(getString(R.string.no), null)
            .create()
            .show()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

}