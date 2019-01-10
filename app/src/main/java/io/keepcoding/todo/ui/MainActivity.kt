package io.keepcoding.todo.ui

import android.os.Bundle
import android.app.Activity
import io.keepcoding.todo.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

}
