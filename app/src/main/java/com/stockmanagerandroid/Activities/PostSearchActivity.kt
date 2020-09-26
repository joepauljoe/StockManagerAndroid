package com.stockmanagerandroid.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import com.stockmanagerandroid.Services.Images.rotateBitmap
import kotlinx.android.synthetic.main.activity_post_search.*
import java.io.ByteArrayInputStream

class PostSearchActivity : AppCompatActivity() {

    lateinit var item : InventoryItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_search)
        if(API.itemSearchedFor.value != null || API.imageData.value != null) {
            API.itemSearchedFor.value = null
            API.imageData.value = null
        }

        val itemIDSearchedFor = intent.getStringExtra("itemID")
        item_number_searched.text = itemIDSearchedFor

        if (itemIDSearchedFor != null) {
            API.queryItemByID(itemIDSearchedFor, "Test Store 1")
            API.itemSearchedFor.observeForever(Observer {
                if(it != null) {
                    item = it
                    item_name.text = item.name
                    API.imageQuery(item.id)
                    API.imageData.observeForever(Observer {
                        if (it != null) {
                            item_image.visibility = View.VISIBLE
                            val bitmapOptions = BitmapFactory.Options()
                            bitmapOptions.inSampleSize = 1
                            bitmapOptions.run {
                                inJustDecodeBounds = false
                                var decodedImage =
                                    BitmapFactory.decodeByteArray(it, 0, it.size, this)
                                decodedImage = rotateBitmap(it, decodedImage)
                                item_image.setImageBitmap(decodedImage)
                                item_image.setClipToOutline(true)
                            }
                        }
                    })
                }
            })
        }

        item_image.setOnClickListener {
            if(API.itemSearchedFor.value != null) {
                enterItemDetailPage(item)
            }
        }
        item_name.setOnClickListener {
            if(API.itemSearchedFor.value != null) {
                enterItemDetailPage(item)
            }
        }


    }

    private fun enterItemDetailPage(item: InventoryItem) {
        val intent = Intent(this, ItemDetailActivity::class.java)
        startActivity(intent)
    }

}