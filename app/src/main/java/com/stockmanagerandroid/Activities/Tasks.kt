package com.stockmanagerandroid.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stockmanagerandroid.Adapters.TasksAdapter
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API

class Tasks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        API.getTasks()

        var mRecyclerView = findViewById<RecyclerView>(R.id.r_view_tasks)
        mRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = API.mAdapter
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false)
        var mAdapter = TasksAdapter()
        mRecyclerView.adapter = mAdapter

        API.tasksFetched.observe(this, Observer {
            if (it) {
                mAdapter.tasksList = API.tasksList
                mAdapter.notifyDataSetChanged()
            }
        })


    }


    override fun onBackPressed() {
        API.tasksFetched.postValue(false)
        super.onBackPressed()
    }
}