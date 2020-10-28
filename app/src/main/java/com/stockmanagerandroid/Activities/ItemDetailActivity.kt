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

        val constraint_layout_two = findViewById<ConstraintLayout>(R.id.constraint_layout_two)
        val add_location = constraint_layout_two.findViewById<Button>(R.id.add_location)
        add_location.setOnClickListener{
            val inflater: LayoutInflater =
                getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view = inflater.inflate(R.layout.layout_add_location, null)

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

            val up_aisle = view.findViewById<Button>(R.id.up_aisle)
            val dynamic_aisle = view.findViewById<TextView>(R.id.dynamic_aisle)
            val down_aisle = view.findViewById<Button>(R.id.down_aisle)
            val up_aisle_section = view.findViewById<Button>(R.id.up_aisle_section)
            val dynamic_aisle_section = view.findViewById<TextView>(R.id.dynamic_aisle_section)
            val down_aisle_section = view.findViewById<Button>(R.id.down_aisle_section)
            val up_spot = view.findViewById<Button>(R.id.up_spot)
            val dynamic_spot = view.findViewById<TextView>(R.id.dynamic_spot)
            val down_spot = view.findViewById<Button>(R.id.down_spot)
            val right_type = view.findViewById<Button>(R.id.right_type)
            val dynamic_type = view.findViewById<TextView>(R.id.dynamic_type)
            val left_type = view.findViewById<Button>(R.id.left_type)
            val right_accessibility = view.findViewById<Button>(R.id.right_accessibility)
            val dynamic_accessibility = view.findViewById<TextView>(R.id.dynamic_accessibility)
            val left_accessibility = view.findViewById<Button>(R.id.left_accessibility)
            val cancel = view.findViewById<Button>(R.id.cancel)
            val save = view.findViewById<Button>(R.id.save)
            dynamic_accessibility.text = Enums.accessibility[0]
            dynamic_type.text = Enums.locationTypes[0]
            up_aisle.setOnClickListener{
                val aisleNum = dynamic_aisle.text.toString().toInt() + 1
                dynamic_aisle.text = aisleNum.toString()
            }
            down_aisle.setOnClickListener{
                if(dynamic_aisle.text.toString() != "1") {
                    val aisleNum = dynamic_aisle.text.toString().toInt() - 1
                    dynamic_aisle.text = aisleNum.toString()
                }
            }
            up_aisle_section.setOnClickListener{
                if(dynamic_aisle_section.text.toString() != "Z") {
                    val aisleSectionNum =
                        dynamic_aisle_section.text.toString().toCharArray()[0].toInt() + 1
                    dynamic_aisle_section.text = aisleSectionNum.toChar().toString()
                }
            }
            down_aisle_section.setOnClickListener{
                if(dynamic_aisle_section.text.toString() != "A") {
                    val aisleSectionNum = dynamic_aisle_section.text.toString().toCharArray()[0].toInt() - 1
                    dynamic_aisle_section.text = aisleSectionNum.toChar().toString()
                }
            }
            up_spot.setOnClickListener{
                val spotNum = dynamic_spot.text.toString().toInt() + 1
                dynamic_spot.text = spotNum.toString()
            }
            down_spot.setOnClickListener{
                if(dynamic_spot.text.toString() != "1") {
                    val spotNum = dynamic_spot.text.toString().toInt() - 1
                    dynamic_spot.text = spotNum.toString()
                }
            }
            right_type.setOnClickListener{
                typeIndex++
                dynamic_type.text = Enums.locationTypes[typeIndex%Enums.locationTypes.size]
            }
            left_type.setOnClickListener{
                typeIndex--
                dynamic_type.text = Enums.locationTypes[typeIndex%Enums.locationTypes.size]
            }
            right_accessibility.setOnClickListener{
                accessibilityIndex++
                dynamic_accessibility.text = Enums.accessibility[accessibilityIndex%Enums.accessibility.size]
            }
            left_accessibility.setOnClickListener{
                accessibilityIndex--
                dynamic_accessibility.text = Enums.accessibility[accessibilityIndex%Enums.accessibility.size]
            }
            cancel.setOnClickListener{
                popupWindow.dismiss()
            }
            save.setOnClickListener{
                API.changesMade = true
                var location = Location()
                location.accessibility = dynamic_accessibility.text.toString()
                location.aisle = dynamic_aisle.text.toString()
                location.aisleSection = dynamic_aisle_section.text.toString()
                location.spot = dynamic_spot.text.toString()
                location.type = dynamic_type.text.toString()
                if(API.modifiedItem.locations != null) {
                    API.modifiedItem.locations!!.add(location)
                } else {
                    val locationsList = arrayListOf<Location>(location)
                    API.modifiedItem.locations = locationsList
                }
                API.mAdapter.keysList = API.modifiedItem.locations!!
                API.mAdapter.notifyDataSetChanged()
                popupWindow.dismiss()
            }

            popupWindow.dimBehind()
        }

        API.locationDescriptionToEdit.observeForever(Observer {
            if(it != null) {
                var location = it
                val index = API.modifiedItem.locations!!.indexOf(location)

                val inflater: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                val view = inflater.inflate(R.layout.layout_edit_description, null)

                val popupWindow = PopupWindow(
                    view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
                val description_edit = view.findViewById<EditText>(R.id.description_edit)
                description_edit.setText(location.description)

                val cancel = view.findViewById<Button>(R.id.cancel)
                val save = view.findViewById<Button>(R.id.save)
                cancel.setOnClickListener{
                    popupWindow.dismiss()
                }
                save.setOnClickListener{
                    API.modifiedItem.locations!![index].description = description_edit.text.toString()
                    API.mAdapter = LocationsAdapter()
                    API.mAdapter.keysList = API.modifiedItem.locations!!
                    API.mAdapter.notifyDataSetChanged()
                    mRecyclerView.adapter = API.mAdapter
                    API.changesMade = true
                    popupWindow.dismiss()
                }
                TransitionManager.beginDelayedTransition(activity_item_detail)
                if(!isFinishing()) {
                    popupWindow.showAtLocation(
                        activity_item_detail, // Location to display popup window
                        Gravity.CENTER, // Exact position of layout to display popup
                        0, // X offset
                        0 // Y offset
                    )
                    popupWindow.dimBehind()
                }

            }
        })

        API.locationToMove.observeForever(Observer {
            if(it != null) {
                var location = it
                var index = API.modifiedItem.locations!!.indexOf(location)
                val inflater: LayoutInflater =
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

                val view = inflater.inflate(R.layout.layout_add_location, null)

                val popupWindow = PopupWindow(
                    view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                )
                popupWindow.isFocusable = false

                val up_aisle = view.findViewById<Button>(R.id.up_aisle)
                val dynamic_aisle = view.findViewById<TextView>(R.id.dynamic_aisle)
                dynamic_aisle.text = location.aisle
                val down_aisle = view.findViewById<Button>(R.id.down_aisle)
                val up_aisle_section = view.findViewById<Button>(R.id.up_aisle_section)
                val dynamic_aisle_section = view.findViewById<TextView>(R.id.dynamic_aisle_section)
                dynamic_aisle_section.text = location.aisleSection
                val down_aisle_section = view.findViewById<Button>(R.id.down_aisle_section)
                val up_spot = view.findViewById<Button>(R.id.up_spot)
                val dynamic_spot = view.findViewById<TextView>(R.id.dynamic_spot)
                if(location.spot == "") {
                    dynamic_spot.text = "1"
                } else {
                    dynamic_spot.text = location.spot
                }

                val down_spot = view.findViewById<Button>(R.id.down_spot)
                val right_type = view.findViewById<Button>(R.id.right_type)
                val dynamic_type = view.findViewById<TextView>(R.id.dynamic_type)
                val left_type = view.findViewById<Button>(R.id.left_type)
                val right_accessibility = view.findViewById<Button>(R.id.right_accessibility)
                val dynamic_accessibility = view.findViewById<TextView>(R.id.dynamic_accessibility)
                val left_accessibility = view.findViewById<Button>(R.id.left_accessibility)
                val cancel = view.findViewById<Button>(R.id.cancel)
                val save = view.findViewById<Button>(R.id.save)
                accessibilityIndex = Enums.accessibility.indexOf(location.accessibility)
                dynamic_accessibility.text = Enums.accessibility[accessibilityIndex%Enums.accessibility.size]
                typeIndex = Enums.locationTypes.indexOf(location.type)
                if(typeIndex == -1) {
                    typeIndex = 0
                }
                dynamic_type.text = Enums.locationTypes[typeIndex%Enums.locationTypes.size]
                up_aisle.setOnClickListener{
                    val aisleNum = dynamic_aisle.text.toString().toInt() + 1
                    dynamic_aisle.text = aisleNum.toString()
                }
                down_aisle.setOnClickListener{
                    if(dynamic_aisle.text.toString() != "1") {
                        val aisleNum = dynamic_aisle.text.toString().toInt() - 1
                        dynamic_aisle.text = aisleNum.toString()
                    }
                }
                up_aisle_section.setOnClickListener{
                    if(dynamic_aisle_section.text.toString() != "Z") {
                        val aisleSectionNum =
                            dynamic_aisle_section.text.toString().toCharArray()[0].toInt() + 1
                        dynamic_aisle_section.text = aisleSectionNum.toChar().toString()
                    }
                }
                down_aisle_section.setOnClickListener{
                    if(dynamic_aisle_section.text.toString() != "A") {
                        val aisleSectionNum = dynamic_aisle_section.text.toString().toCharArray()[0].toInt() - 1
                        dynamic_aisle_section.text = aisleSectionNum.toChar().toString()
                    }
                }
                up_spot.setOnClickListener{
                    val spotNum = dynamic_spot.text.toString().toInt() + 1
                    dynamic_spot.text = spotNum.toString()
                }
                down_spot.setOnClickListener{
                    if(dynamic_spot.text.toString() != "1") {
                        val spotNum = dynamic_spot.text.toString().toInt() - 1
                        dynamic_spot.text = spotNum.toString()
                    }
                }
                right_type.setOnClickListener{
                    typeIndex++
                    dynamic_type.text = Enums.locationTypes[typeIndex%Enums.locationTypes.size]
                }
                left_type.setOnClickListener{
                    typeIndex--
                    dynamic_type.text = Enums.locationTypes[typeIndex%Enums.locationTypes.size]
                }
                right_accessibility.setOnClickListener{
                    accessibilityIndex++
                    dynamic_accessibility.text = Enums.accessibility[accessibilityIndex%Enums.accessibility.size]
                }
                left_accessibility.setOnClickListener{
                    accessibilityIndex--
                    dynamic_accessibility.text = Enums.accessibility[accessibilityIndex%Enums.accessibility.size]
                }
                cancel.setOnClickListener{
                    popupWindow.dismiss()
                }
                save.setOnClickListener{
                    API.changesMade = true
                    var location = Location()
                    location.accessibility = dynamic_accessibility.text.toString()
                    location.aisle = dynamic_aisle.text.toString()
                    location.aisleSection = dynamic_aisle_section.text.toString()
                    location.spot = dynamic_spot.text.toString()
                    location.type = dynamic_type.text.toString()
                    API.modifiedItem.locations!!.removeAt(index)
                    if(index == API.modifiedItem.locations!!.size) {
                        API.modifiedItem.locations!!.add(location)
                    } else {
                        API.modifiedItem.locations!![index] = location
                    }
                    API.mAdapter = LocationsAdapter()
                    API.mAdapter.keysList = API.modifiedItem.locations!!
                    API.mAdapter.notifyDataSetChanged()
                    mRecyclerView.adapter = API.mAdapter
                    API.changesMade = true
                    popupWindow.dismiss()
                }

                if(!isFinishing) {
                    TransitionManager.beginDelayedTransition(activity_item_detail)
                    popupWindow.showAtLocation(
                        activity_item_detail, // Location to display popup window
                        Gravity.CENTER, // Exact position of layout to display popup
                        0, // X offset
                        0 // Y offset
                    )
                    popupWindow.dimBehind()
                }

            }
        })

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
            Log.d("Changes Made", changes_made.text.toString())
            save.setOnClickListener{
                API.itemSearchedFor.postValue(API.modifiedItem)
                Log.d("UPDATING", "ITEM")
                API.updateItem(API.modifiedItem, API.currentUser.storeID)
                popupWindow.dismiss()
                API.changesMade = false
                API.locationToMove.postValue(null)
                API.locationDescriptionToEdit.postValue(null)
                super.onBackPressed()
            }
            cancel.setOnClickListener{
                popupWindow.dismiss()
                API.locationToMove.postValue(null)
                API.locationDescriptionToEdit.postValue(null)
                API.modifiedItem = InventoryItem()
                API.changesMade = false
                super.onBackPressed()
            }

        } else {
            API.locationToMove.postValue(null)
            API.locationDescriptionToEdit.postValue(null)
            API.modifiedItem = InventoryItem()
            API.changesMade = false
            super.onBackPressed()
        }

    }
}