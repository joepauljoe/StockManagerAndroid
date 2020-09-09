package com.stockmanagerandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_post_search.*
import okhttp3.OkHttpClient

class PostSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search)
        if(API.itemSearchedFor.value != null) {
            API.itemSearchedFor.value = null
        }

        val itemIDSearchedFor = intent.getStringExtra("itemID")
        item_number_searched.text = itemIDSearchedFor

        if (itemIDSearchedFor != null) {
            API.queryItemByID(itemIDSearchedFor, "Test Store 1")
            API.itemSearchedFor.observeForever(Observer {
                if(it != null) {
                    item_name.text = it.name
                }
            })
        }


    }

}