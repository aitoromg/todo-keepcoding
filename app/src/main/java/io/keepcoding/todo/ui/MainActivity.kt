package io.keepcoding.todo.ui

import android.os.Bundle
import io.keepcoding.todo.R
import io.keepcoding.todo.ui.base.BaseActivity
import io.keepcoding.todo.ui.tasks.TaskFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()
    }

    private fun setUp() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, TaskFragment())
            .commit()
    }

}
