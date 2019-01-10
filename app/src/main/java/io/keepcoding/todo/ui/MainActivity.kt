package io.keepcoding.todo.ui

import android.os.Bundle
import android.widget.Toast
import io.keepcoding.todo.R
import io.keepcoding.todo.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
