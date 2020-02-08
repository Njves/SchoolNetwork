package com.njves.schoolnetwork.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Models.network.request.UserService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.adapter.ReceiversAdapter
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TaskEditFragment : Fragment() {
    lateinit var etTitle : EditText
    lateinit var etDescription : EditText
    lateinit var btnDatePicker: Button
    lateinit var rvReceivers : RecyclerView
    lateinit var btnSend : Button
    lateinit var onCloseListener: OnFragmentInteraction
    var currentUser : User?= null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnFragmentInteraction)
        {
            onCloseListener = context
        }else{
            throw RuntimeException(" must implemented OnFragmentInteraction")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_task_edit, container, false)
        rvReceivers = v.findViewById(R.id.rvReceivers)
        val adapter = ReceiversAdapter(context, listOf(User("test", "test", "test", "test", "test", 1)))
        rvReceivers.layoutManager = LinearLayoutManager(context)
        rvReceivers.adapter = adapter

        Log.d("TaskEditFragment", "OnCreate TaskEditFragment")
        val userService = NetworkService.instance.getRetrofit().create(UserService::class.java)
        val storage = AuthStorage(context)
        val userGetCall = userService.getUser(storage.getUserDetails()?:"")
        userGetCall.enqueue(object: Callback<NetworkResponse<User>>{
            override fun onFailure(call: Call<NetworkResponse<User>>, t: Throwable) {

            }

            override fun onResponse(call: Call<NetworkResponse<User>>, response: Response<NetworkResponse<User>>) {
                currentUser = response.body()?.data
            }

        })

        etTitle = v.findViewById(R.id.edTitle)
        etDescription = v.findViewById(R.id.edDescription)
        btnDatePicker = v.findViewById(R.id.btnDatePicker)

        btnSend = v.findViewById(R.id.btnSend)
        btnSend.setOnClickListener{
            val title = etTitle.text.toString()
            val desc = etDescription.text.toString()
            val date = Date()
            val receiver : User? = adapter.getUser()
            val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
            Log.d("TaskEditFragment", adapter.getUser().toString())

                val postCall = taskService.postCallTask(
                    RequestTaskModel(
                        "POST",
                        Task(0, title, desc, date.toString(), currentUser!!, receiver!!)
                    )
                )
                postCall.enqueue(object : Callback<NetworkResponse<Task>> {
                    override fun onFailure(call: Call<NetworkResponse<Task>>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<NetworkResponse<Task>>, response: Response<NetworkResponse<Task>>) {
                        val code = response.body()?.code
                        val message = response.body()?.message
                        if (code == 0) {
                            Toast.makeText(context, "Успех", Toast.LENGTH_SHORT).show()
                            onCloseListener.onClose()
                        } else {
                            val errorDialog = AuthErrorDialog.newInstance(message)
                            errorDialog.show(activity?.supportFragmentManager, "dialogError")
                        }
                    }

                })


        }
        return v
    }
    public interface OnFragmentInteraction{
        fun onClose()
    }
}