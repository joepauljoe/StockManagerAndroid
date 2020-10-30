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
import com.stockmanagerandroid.Models.Task
import com.stockmanagerandroid.R
import com.stockmanagerandroid.Services.API
import kotlinx.android.synthetic.main.layout_task_cell.view.*
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.*
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.dynamic_aisle
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.dynamic_aisle_section
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.dynamic_spot
import kotlinx.android.synthetic.main.locations_recycler_view_cell.view.dynamic_type


class TasksAdapter() : RecyclerView.Adapter<TasksAdapter.TasksListItemViewHolder>() {

    var tasksList = ArrayList<Task>()

    class TasksListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initialize(task: Task) {
            itemView.dynamic_aisle.text = task.src["aisle"] as String
            itemView.dynamic_aisle_section.text = task.src["aisleSection"] as String
            itemView.dynamic_type.text = task.src["type"] as String
            itemView.dynamic_spot.text = task.src["spot"] as String
            itemView.dynamic_aisle_dest.text = task.dest["aisle"] as String
            itemView.dynamic_aisle_section_dest.text = task.dest["aisleSection"] as String
            itemView.dynamic_type_dest.text = task.dest["type"] as String
            itemView.dynamic_spot_dest.text = task.dest["spot"] as String

            itemView.complete.setOnClickListener{
                API.completeTask(task.id)
                API.tasksList.remove(task)
                API.tasksFetched.postValue(true)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TasksListItemViewHolder {

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_task_cell, parent, false)

        return TasksListItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    override fun onBindViewHolder(
        holder: TasksAdapter.TasksListItemViewHolder,
        position: Int
    ) {
        val currentItem = tasksList[position]

        holder.initialize(currentItem)
    }
}