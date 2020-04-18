package com.njves.schoolnetwork.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService

import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.presenter.task.task_detail.ITaskDetail
import com.njves.schoolnetwork.presenter.task.task_detail.TaskDetailPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskDetailFragment : Fragment(), ITaskDetail {
    private lateinit var tvTitle : TextView
    private lateinit var tvDescription : TextView
    private lateinit var ivSenderAvatar : ImageView
    private lateinit var tvSender : TextView
    private lateinit var tvAttachFiles : TextView
    private lateinit var btnDelete : Button
    private var argJsonTask : String? = null
    private lateinit var task : TaskViewModel
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
            task = gson.fromJson(argJsonTask, TaskViewModel::class.java)
            taskDetailPresenter.initTask(task)
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
        btnDelete.setOnClickListener{
            val storage = AuthStorage(context)
            taskDetailPresenter.deleteTask(storage.getUserDetails()!!, task)
        }
        return v
    }


    override fun onDelete() {
        Snackbar.make(view!!, "Задача успешно удалена", Snackbar.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onInit(task: TaskViewModel) {
        tvTitle.text = task.title
        tvDescription.text = task.description
        tvSender.text = resources.getString(R.string.name_placeholder, task.sender.firstName, task.sender.lastName)
    }

    override fun onError(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Snackbar.make(view!!, "Произошла ошибка запроса!", Snackbar.LENGTH_SHORT).show()
        Log.wtf(TAG, t.toString())
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}
