package com.njves.schoolnetwork.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel

import com.njves.schoolnetwork.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class TaskDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
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

        return v
    }



}
