package com.stockmanagerandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button_x.visibility = View.GONE
        button_arrow.visibility = View.GONE

        button_one.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "1"
            item_number_search.text = text
        }

        button_two.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "2"
            item_number_search.text = text
        }

        button_three.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "3"
            item_number_search.text = text
        }

        button_four.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "4"
            item_number_search.text = text
        }

        button_five.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "5"
            item_number_search.text = text
        }

        button_six.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "6"
            item_number_search.text = text
        }

        button_seven.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "7"
            item_number_search.text = text
        }

        button_eight.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "8"
            item_number_search.text = text
        }

        button_nine.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "9"
            item_number_search.text = text
        }

        button_x.setOnClickListener {
            var text = item_number_search.text.toString()
            if(text.length > 1) {
                text = text.substring(0, text.length-1)
            } else {
                text = ""
                button_x.visibility = View.GONE
                button_arrow.visibility = View.GONE
            }
            item_number_search.text = text
        }

        button_zero.setOnClickListener {
            button_x.visibility = View.VISIBLE
            button_arrow.visibility = View.VISIBLE
            val text = item_number_search.text.toString() + "0"
            item_number_search.text = text
        }

        button_arrow.setOnClickListener {
            //move to next activity
            val intent = Intent(this, PostSearchActivity::class.java)
            intent.putExtra("itemID", item_number_search.text.toString())
            startActivity(intent)
        }
    }
}
