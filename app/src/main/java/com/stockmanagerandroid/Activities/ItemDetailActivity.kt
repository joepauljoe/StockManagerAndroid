package com.stockmanagerandroid.Activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import com.stockmanagerandroid.Services.Images.rotateBitmap
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_post_search.*

class ItemDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_detail)

        val itemID = intent.getStringExtra("id")
        Log.d("itemID", API.itemSearchedFor.value!!.id)

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

        val item = API.itemSearchedFor.value!!
        dynamic_ISQ.text = (item.backstockQuantity + item.customerAccessibleQuantity).toString()
        dynamic_id.text = item.userDesignatedID
        dynamic_name.text = item.name

        if(item.locations != null) {
            var i=0
            for (location in item.locations!!) {
                
            }
        } else {
            locations.visibility = View.GONE
        }

    }
}