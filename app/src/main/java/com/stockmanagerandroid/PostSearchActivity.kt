package com.stockmanagerandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_post_search.*

class PostSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search)

        val itemIDSearchedFor = intent.getStringExtra("itemID")
        item_number_searched.text = itemIDSearchedFor
    }
}