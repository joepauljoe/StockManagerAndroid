package com.stockmanagerandroid.Adapters

import android.app.PendingIntent.getActivity
import android.transition.TransitionManager
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.stockmanagerandroid.Models.Location
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.*


class LocationsAdapter() : RecyclerView.Adapter<LocationsAdapter.LocationsListItemViewHolder>() {

    var keysList = ArrayList<Location>()

    class LocationsListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView1: TextView = itemView.dynamic_aisle
        var textView2: TextView = itemView.dynamic_aisle_section
        var textView3: TextView = itemView.dynamic_type
        var textView4: TextView = itemView.dynamic_accessibility

        fun initialize(location: Location) {
            textView1.text = location.aisle
            textView2.text = location.aisleSection
            textView3.text = location.type
            textView4.text = location.accessibility

            if(location.description != "") {
                itemView.dynamic_description.text = location.description
                itemView.dynamic_description.visibility = View.VISIBLE
                itemView.edit.visibility = View.VISIBLE
                itemView.static_description.visibility = View.VISIBLE
                itemView.more_details_button.visibility = View.GONE

                itemView.edit.setOnClickListener{
                    API.locationDescriptionToEdit.postValue(location)
                }
            }

            itemView.more_details_button.setOnClickListener {
                itemView.dynamic_description.text = location.description
                itemView.dynamic_description.visibility = View.VISIBLE
                itemView.edit.visibility = View.VISIBLE
                itemView.static_description.visibility = View.VISIBLE
                itemView.more_details_button.visibility = View.GONE

                itemView.edit.setOnClickListener{
                    API.locationDescriptionToEdit.postValue(location)
                }
            }
            itemView.remove_location_button.setOnClickListener {
                API.modifiedItem.locations!!.remove(location)
                API.mAdapter.keysList = API.modifiedItem.locations!!
                API.mAdapter.notifyDataSetChanged()
            }
            itemView.move_location_button.setOnClickListener{
                API.locationToMove.postValue(location)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationsListItemViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.locations_recycler_view_cell, parent, false)

        return LocationsListItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return keysList.size
    }

    override fun onBindViewHolder(
        holder: LocationsAdapter.LocationsListItemViewHolder,
        position: Int
    ) {
        val currentItem = keysList[position]

        holder.initialize(currentItem)
    }
}