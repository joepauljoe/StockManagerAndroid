package com.stockmanagerandroid.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stockmanagerandroid.Adapters.LocationsAdapter
import com.stockmanagerandroid.Adapters.NameSearchAdapter
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.Models.Location
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_post_name_search.*

class PostNameSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_name_search)

        val nameSearch = intent.getStringExtra("name")
        search_term.text = nameSearch

        var mRecyclerView = findViewById<RecyclerView>(R.id.r_view_name_search)
        var mAdapter = NameSearchAdapter()
        mRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext)
        mRecyclerView.adapter = mAdapter
        API.nameItemsSearchedFor.observe(this, Observer {
            Log.d("ItemsSearchedFor", it.size.toString())
            mAdapter.keysList = it
            mAdapter.notifyDataSetChanged()
        })
    }
}