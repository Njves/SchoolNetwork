package com.njves.schoolnetwork.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel

import com.njves.schoolnetwork.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class TaskDetailFragment : Fragment() {
    private lateinit var tvTitle : TextView
    private lateinit var tvDescription : TextView
    private lateinit var ivSenderAvatar : ImageView
    private lateinit var tvSender : TextView
    private lateinit var tvAttachFiles : TextView

    private var argTask : String? = null
    private lateinit var task : TaskViewModel
    private var gson : Gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            argTask = it.getString(TaskFragment.ARG_TASK)
            task = gson.fromJson(argTask, TaskViewModel::class.java)
            Log.d("TaskDetailFragment", task.toString())
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_task_detail, container, false)
        tvTitle = v.findViewById(R.id.tvTitle)
        tvDescription = v.findViewById(R.id.tvDescription)
        tvSender = v.findViewById(R.id.tvSender)
        ivSenderAvatar = v.findViewById(R.id.ivSenderAvatar)
        tvAttachFiles = v.findViewById(R.id.tvAttachedFiles)

        tvTitle.text = task.title
        tvDescription.text = task.description
        tvSender.text = resources.getString(R.string.name_placeholder, task.sender.firstName, task.sender.lastName)
        tvAttachFiles.text = "${tvAttachFiles.text}" +"\n Job.work"
        return v
    }



}
