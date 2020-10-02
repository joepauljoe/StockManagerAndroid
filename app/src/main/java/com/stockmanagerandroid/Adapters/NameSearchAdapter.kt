package com.stockmanagerandroid.Adapters

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.BitmapFactory
import android.transition.TransitionManager
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.stockmanagerandroid.Activities.ItemDetailActivity
import com.stockmanagerandroid.Models.InventoryItem
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import com.stockmanagerandroid.Services.Images
import kotlinx.android.synthetic.main.activity_post_search.*
import kotlinx.android.synthetic.main.name_search_cell.view.*


class NameSearchAdapter() : RecyclerView.Adapter<NameSearchAdapter.NameSearchViewHolder>() {

    var keysList = ArrayList<InventoryItem>()

    class NameSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView = itemView.item_name

        fun initialize(item: InventoryItem) {
            textView1.text = item.name

            itemView.item_image.visibility = View.VISIBLE
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inSampleSize = 1
            API.nameToImage.observeForever(Observer{
                if(API.nameToImage.value!= null && API.nameToImage.value!![item.name] != null) {
                    bitmapOptions.run {
                        inJustDecodeBounds = false
                        var decodedImage =
                            BitmapFactory.decodeByteArray(
                                API.nameToImage.value!![item.name],
                                0,
                                API.nameToImage.value!![item.name]!!.size,
                                this
                            )
                        if (decodedImage != null) {
                            decodedImage = Images.rotateBitmap(API.nameToImage.value!![item.name], decodedImage)
                            itemView.item_image.setImageBitmap(decodedImage)
                            itemView.item_image.setClipToOutline(true)
                        }
                    }
                }
            })

            itemView.setOnClickListener{
                val intent = Intent(itemView.context, ItemDetailActivity::class.java)
                API.itemSearchedFor.postValue(item)
                API.imageData.postValue(API.nameToImage.value!![item.name])
                itemView.context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NameSearchViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.name_search_cell, parent, false)

        return NameSearchViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return keysList.size
    }

    override fun onBindViewHolder(
        holder: NameSearchAdapter.NameSearchViewHolder,
        position: Int
    ) {
        val currentItem = keysList[position]

        holder.initialize(currentItem)
    }
}

