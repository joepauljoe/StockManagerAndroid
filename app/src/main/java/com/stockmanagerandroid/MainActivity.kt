package com.stockmanagerandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //val button_one: Button = findViewById(R.id.button_one) //this is unnecessary in activities

        button_one.setOnClickListener {
            val text = item_number_search.text.toString() + "1"
            item_number_search.text = text
        }
    }
}
