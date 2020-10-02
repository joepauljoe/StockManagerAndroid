package com.stockmanagerandroid.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.stockmanagerandroid.Adapters.LocationsAdapter
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        company_name.text = API.currentUser.companyID
        store_name.text = API.currentUser.storeID
        hello_user.text = "Hello, " + API.currentUser.firstName


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

        search_by_name.setOnClickListener{
            val inflater: LayoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.layout_search_by_name_popup, null)

            val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            val name_edit = view.findViewById<EditText>(R.id.name_edit)

            val cancel = view.findViewById<Button>(R.id.cancel)
            val search = view.findViewById<Button>(R.id.search)
            cancel.setOnClickListener{
                popupWindow.dismiss()
            }
            search.setOnClickListener{
                val intent = Intent(this, PostNameSearchActivity::class.java)
                intent.putExtra("name", name_edit.text.toString())
                API.queryItemByName(name_edit.text.toString(), API.currentUser.storeID)
                startActivity(intent)
                popupWindow.dismiss()
            }
            TransitionManager.beginDelayedTransition(activity_main)
            if(!isFinishing()) {
                popupWindow.showAtLocation(
                    activity_main, // Location to display popup window
                    Gravity.CENTER, // Exact position of layout to display popup
                    0, // X offset
                    0 // Y offset
                )
                popupWindow.dimBehind()
            }
        }
    }

    fun PopupWindow.dimBehind() {
        val container = contentView.rootView
        val context = contentView.context
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams
        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.3f
        wm.updateViewLayout(container, p)
    }
}
