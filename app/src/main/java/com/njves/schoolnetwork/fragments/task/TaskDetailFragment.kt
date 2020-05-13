package com.njves.schoolnetwork.fragments.task


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.network.models.task.Task

import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.preferences.StatusPreferences
import com.njves.schoolnetwork.presenter.task.task_detail.ITaskDetail
import com.njves.schoolnetwork.presenter.task.task_detail.TaskDetailPresenter

class TaskDetailFragment : Fragment(), ITaskDetail {
    private lateinit var tvTitle : TextView
    private lateinit var tvDescription : TextView
    private lateinit var ivSenderAvatar : ImageView
    private lateinit var tvSender : TextView
    private lateinit var tvAttachFiles : TextView
    private lateinit var btnDelete : Button
    private lateinit var btnStatus : Button
    private var argJsonTask : String? = null
    private lateinit var task : Task
    private var gson = Gson()
    private var taskDetailPresenter = TaskDetailPresenter(this)

    companion object{
        const val TAG  = "TaskDetailFragment"
        const val ARG_TASK = "task_item"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            argJsonTask = it.getString(ARG_TASK)
            task = gson.fromJson(argJsonTask, Task::class.java)
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
        btnDelete = v.findViewById(R.id.btnDelete)
        btnStatus = v.findViewById(R.id.btnStatus)
        taskDetailPresenter.initTask(task)
        btnDelete.setOnClickListener{
            val storage = AuthStorage(context)
            taskDetailPresenter.deleteTask(storage.getUserDetails()!!, task)
        }

        btnStatus.setOnClickListener{
            val storage = StatusPreferences(context)
            val uid = task.uid?:""
            val status = storage.getStatus(uid)
            when(status){
                0->{storage.setStatus(uid, 1)}
                1->{storage.setStatus(uid, 0)}
            }
            
        }
        return v
    }


    override fun onDelete() {

        Toast.makeText(context, "Задача успешно удалена", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onInit(task: Task) {
        tvTitle.text = task.title
        tvDescription.text = task.description
        tvSender.text = resources.getString(R.string.name_placeholder, task.sender.firstName, task.sender.lastName)
    }

    override fun onError(message: String?) {
        Snackbar.make(view!!, "Произашла ошибка $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Snackbar.make(view!!, "Произашла ошибка запроса!", Snackbar.LENGTH_SHORT).show()
        Log.wtf(TAG, t.toString())
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}
