package com.njves.schoolnetwork.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.njves.schoolnetwork.Models.network.models.task.TaskPostModel
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.callback.OnRecyclerViewTaskOnItemClickListener

class TaskAdapter(val context: Context?, var listTask: List<TaskViewModel>, val onItemClickListener : OnRecyclerViewTaskOnItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

    companion object {
        const val TAG = "TaskAdapter"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false)

        return TaskHolder(view)
    }

    override fun getItemCount(): Int {
        return listTask.size
    }

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        Log.d(TAG, "OnCreateAdapter")
        holder.bindItem(listTask[position])
    }

    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txTitle: TextView = itemView.findViewById(R.id.title)
        private val txDescription: TextView = itemView.findViewById(R.id.description)
        private val txFrom: TextView = itemView.findViewById(R.id.from)
        private val txDate: TextView = itemView.findViewById(R.id.date)

        fun bindItem(item: TaskViewModel) {

            txTitle.text = item.title
            txDescription.text = item.description
            item.sender.let{
                txFrom.text = item.sender.firstName
            }
            txDate.text = item.date
            Log.d(TAG, txTitle.text.toString())
            itemView.setOnClickListener{
                onItemClickListener.onItemClick(listTask[adapterPosition])
            }

        }
    }

}