package com.stockmanagerandroid.Activities

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stockmanagerandroid.Adapters.LocationsAdapter
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.Models.Location
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import com.stockmanagerandroid.Services.Enums
import com.stockmanagerandroid.Services.ExtensionFunctions
import com.stockmanagerandroid.Services.Images.rotateBitmap
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.layout_add_location.*


class ItemDetailActivity : AppCompatActivity() {

    private var typeIndex = 0
    private var accessibilityIndex = 0
    private var item = InventoryItem()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        var mRecyclerView = findViewById<RecyclerView>(R.id.r_view)
        mRecyclerView.layoutManager =
            LinearLayoutManager(applicationContext)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = API.mAdapter
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false)
        val scrollView = findViewById<NestedScrollView>(R.id.nested_scroll)
        scrollView.isFocusableInTouchMode = true
        scrollView.descendantFocusability = ViewGroup.FOCUS_BEFORE_DESCENDANTS

        API.imageData.observeForever(Observer {
            if (it != null) {
                item_detail_image.visibility = View.VISIBLE
                val bitmapOptions = BitmapFactory.Options()
                bitmapOptions.inSampleSize = 1
                bitmapOptions.run {
                    inJustDecodeBounds = false
                    var decodedImage =
                        BitmapFactory.decodeByteArray(it, 0, it.size, this)
                    if(decodedImage != null) {
                        decodedImage = rotateBitmap(it, decodedImage)
                        item_detail_image.setImageBitmap(decodedImage)
                        item_detail_image.setClipToOutline(true)

                    }
                }
            }
        })

        item = API.itemSearchedFor.value!!
        API.modifiedItem = ExtensionFunctions.copyItem(item)
        dynamic_ISQ.text = (API.modifiedItem.backstockQuantity + API.modifiedItem.customerAccessibleQuantity).toString()
        dynamic_id.text = API.modifiedItem.userDesignatedID
        dynamic_name.text = API.modifiedItem.name
        dynamic_bsq.text = API.modifiedItem.backstockQuantity.toString()
        dynamic_caq.text = API.modifiedItem.customerAccessibleQuantity.toString()

        if(API.modifiedItem.locations != null) {
            API.mAdapter.keysList = item.locations!!
        } else {
            locations.visibility = View.GONE
        }

        down_caq.setOnClickListener{
            API.changesMade = true
            if(API.modifiedItem.customerAccessibleQuantity != 0) {
                API.modifiedItem.customerAccessibleQuantity--
                dynamic_caq.text = API.modifiedItem.customerAccessibleQuantity.toString()
                dynamic_ISQ.text = (API.modifiedItem.backstockQuantity + API.modifiedItem.customerAccessibleQuantity).toString()
            }
        }
        up_caq.setOnClickListener{
            API.changesMade = true
            API.modifiedItem.customerAccessibleQuantity++
            dynamic_caq.text = API.modifiedItem.customerAccessibleQuantity.toString()
            dynamic_ISQ.text = (API.modifiedItem.backstockQuantity + API.modifiedItem.customerAccessibleQuantity).toString()
        }
        down_bsq.setOnClickListener{
            API.changesMade = true
            if(API.modifiedItem.backstockQuantity != 0) {
                API.modifiedItem.backstockQuantity--
                dynamic_bsq.text = API.modifiedItem.backstockQuantity.toString()
                dynamic_ISQ.text = (API.modifiedItem.backstockQuantity + API.modifiedItem.customerAccessibleQuantity).toString()
            }
        }
        up_bsq.setOnClickListener{
            API.changesMade = true
            API.modifiedItem.backstockQuantity++
            dynamic_bsq.text = API.modifiedItem.backstockQuantity.toString()
            dynamic_ISQ.text = (API.modifiedItem.backstockQuantity + API.modifiedItem.customerAccessibleQuantity).toString()
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

    override fun onBackPressed() {
        if(API.changesMade) {
            val inflater: LayoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.confirm_changes_made, null)

            val popupWindow = PopupWindow(
                view,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
            )
            popupWindow.isFocusable = false

            TransitionManager.beginDelayedTransition(activity_item_detail)
            popupWindow.showAtLocation(
                activity_item_detail, // Location to display popup window
                Gravity.CENTER, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )

            val save = view.findViewById<Button>(R.id.save)
            val cancel = view.findViewById<Button>(R.id.cancel)
            val changes_made = view.findViewById<TextView>(R.id.changes_made)
            changes_made.text = ExtensionFunctions.getConfirmationString(item, API.modifiedItem)
            if(changes_made.text == "") {
                popupWindow.dismiss()
                super.onBackPressed()
            }

            save.setOnClickListener{
                API.itemSearchedFor.postValue(API.modifiedItem)
                popupWindow.dismiss()
                if (API.modifiedItem.customerAccessibleQuantity > item.customerAccessibleQuantity) {
                    API.increment(item.userDesignatedID, API.modifiedItem.customerAccessibleQuantity - item.customerAccessibleQuantity, "customerAccessibleQuantity")
                } else if (item.customerAccessibleQuantity > API.modifiedItem.customerAccessibleQuantity) {
                    API.decrement(item.userDesignatedID, item.customerAccessibleQuantity - API.modifiedItem.customerAccessibleQuantity, "customerAccessibleQuantity")
                }
                if (API.modifiedItem.backstockQuantity > item.backstockQuantity) {
                    API.increment(item.userDesignatedID, API.modifiedItem.backstockQuantity - item.backstockQuantity, "backstockQuantity")
                } else if (item.backstockQuantity > API.modifiedItem.backstockQuantity) {
                    API.decrement(item.userDesignatedID, item.backstockQuantity - API.modifiedItem.backstockQuantity, "backstockQuantity")
                }
                API.changesMade = false
                super.onBackPressed()
            }
            cancel.setOnClickListener{
                popupWindow.dismiss()
                API.modifiedItem = InventoryItem()
                API.changesMade = false
                super.onBackPressed()
            }

        } else {
            API.modifiedItem = InventoryItem()
            API.changesMade = false
            super.onBackPressed()
        }

    }
}