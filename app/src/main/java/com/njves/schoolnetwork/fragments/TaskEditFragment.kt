package com.njves.schoolnetwork.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.auth.Profile
import com.njves.schoolnetwork.Models.network.models.auth.User
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.TaskPostModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Models.network.request.TeachersService
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
    lateinit var btnSubmit : Button

    lateinit var adapter : ReceiversAdapter
    var currentUser : User?= null
    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_task_edit, container, false)
        rvReceivers = v.findViewById(R.id.rvReceivers)

        rvReceivers.layoutManager = LinearLayoutManager(context)


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
        val teachersService = NetworkService.instance.getRetrofit().create(TeachersService::class.java)
        val call = teachersService.getTeacherList(1,1)
        call.enqueue(object : Callback<NetworkResponse<List<Profile>>>{
            override fun onFailure(call: Call<NetworkResponse<List<Profile>>>, t: Throwable) {
                Log.d("TaskEdit", t.toString())
                Toast.makeText(context, "Не удалось получить список учителей", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<NetworkResponse<List<Profile>>>,
                response: Response<NetworkResponse<List<Profile>>>) {
                adapter = ReceiversAdapter(context, response.body()?.data?:listOf())
                rvReceivers.adapter = adapter
                Log.d("TaskEdit", response.body()?.data.toString())
            }


        })
        etTitle = v.findViewById(R.id.edTitle)
        etDescription = v.findViewById(R.id.edDescription)
        btnDatePicker = v.findViewById(R.id.btnDatePicker)
        btnSubmit = v.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString()
            val desc = etDescription.text.toString()
            val date = Date()
            val receiver = adapter.getUser()?.uid
            val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
            Log.d("TaskEditFragment", adapter.getUser().toString())
            val storage1 = AuthStorage(context)
            val sender = storage.getUserDetails()!!
            Log.d("TaskEditFragment", "DEBUG INFO: $receiver,$sender")
            val postCall = taskService.postCallTask(
                RequestTaskModel(
                    "POST",
                    TaskPostModel(0, title, desc, date.toString(), storage.getUserDetails()!!, receiver!!)
                )
            )
            postCall.enqueue(object : Callback<NetworkResponse<TaskPostModel>> {
                override fun onFailure(call: Call<NetworkResponse<TaskPostModel>>, t: Throwable) {
                    Toast.makeText(context, "Не удалось отправить задачу", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<NetworkResponse<TaskPostModel>>,
                    response: Response<NetworkResponse<TaskPostModel>>
                ) {
                    val code = response.body()?.code
                    val message = response.body()?.message
                    if (code == 0) {
                        // TODO:Проблемный код
                        view?.let {
                            Snackbar.make(view!!, "Задача была успешно отправлена", Snackbar.LENGTH_SHORT).show()
                        }
                        // Закрыть фрагмент
                        findNavController().navigateUp()
                    } else {
                        val errorDialog = AuthErrorDialog.newInstance(message)
                        errorDialog.show(activity?.supportFragmentManager, "dialogError")

                    }
                }

            })
        }

        return v
    }


}