package com.njves.schoolnetwork.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.R
import java.text.SimpleDateFormat
import java.util.*
/*
* Особенности архитектуры на Java unix time нужно умножать на 1000, если получать с сервера то делить на 1000
*/
class TaskAdapter(val context: Context?, var listTask: ArrayList<Task>, val onItemClickListener : TaskAdapterActionListener) : RecyclerView.Adapter<TaskAdapter.TaskHolder>() {

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
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val txDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvFrom: TextView = itemView.findViewById(R.id.tvFrom)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvDelete : TextView = itemView.findViewById(R.id.tvDelete)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        fun bindItem(item: Task) {

            tvTitle.text = item.title
            txDescription.text = item.description
            item.sender.let{
                tvFrom.text = item.sender.firstName
            }
            val dateFormat = SimpleDateFormat("dd.MMM.yyyy", Locale.getDefault())
            tvDate.text = dateFormat.format(Date(item.date*1000.toLong()))
            itemView.setOnClickListener{
                onItemClickListener.onItemClick(listTask[adapterPosition])
            }
            if(item.status==1){
                tvStatus.visibility = View.VISIBLE
            }else{
                tvStatus.visibility = View.GONE
            }


        }
    }
    public interface TaskAdapterActionListener{
        fun onItemClick(item: Task)
    }




}