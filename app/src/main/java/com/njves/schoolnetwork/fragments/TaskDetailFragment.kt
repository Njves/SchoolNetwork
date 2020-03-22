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
import com.njves.schoolnetwork.Storage.AuthStorage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class TaskDetailFragment : Fragment() {
    private lateinit var tvTitle : TextView
    private lateinit var tvDescription : TextView
    private lateinit var ivSenderAvatar : ImageView
    private lateinit var tvSender : TextView
    private lateinit var tvAttachFiles : TextView
    private lateinit var btnDelete : Button
    private var argTask : String? = null
    private lateinit var task : TaskViewModel
    private var gson : Gson = Gson()

    companion object{
        const val TAG  = "TaskDetailFragment"
    }
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
        btnDelete = v.findViewById(R.id.btnDelete)
        initView()
        btnDelete.setOnClickListener{
            val taskService =  NetworkService.instance.getRetrofit().create(TaskService::class.java)
            val storage = AuthStorage(context)
            val call = taskService.deleteTask("DELETE", task.uid, storage.getUserDetails()?:"")
            call.enqueue(object : Callback<NetworkResponse<Void>> {
                override fun onFailure(call: Call<NetworkResponse<Void>>, t: Throwable) {
                    Snackbar.make(v, "Произашла ошибка запроса", Snackbar.LENGTH_SHORT).show()
                    Log.e(TAG, "Fail request to delete task $t")
                }

                override fun onResponse(call: Call<NetworkResponse<Void>>, response: Response<NetworkResponse<Void>>) {
                    val httpCode = response.code()
                    val body = response.body()
                    if(httpCode==200){
                        if(body?.code==0){
                            Snackbar.make(v, "Задача успешно удалена", Snackbar.LENGTH_SHORT).show()
                            findNavController().navigateUp()
                        }
                    }
                }

            })
        }
        return v
    }
    private fun initView()
    {
        tvTitle.text = task.title
        tvDescription.text = task.description
        tvSender.text = resources.getString(R.string.name_placeholder, task.sender.firstName, task.sender.lastName)

    }


}
