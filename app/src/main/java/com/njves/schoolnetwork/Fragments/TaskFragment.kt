package com.njves.schoolnetwork.Fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.adapter.TaskAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskFragment : Fragment() {
    private lateinit var rvTask : RecyclerView
    private lateinit var adapter : TaskAdapter
    private lateinit var tvErrorMsg : TextView
    private lateinit var fabAddTask : FloatingActionButton
    private lateinit var interaction : OnFragmentInteraction
    private lateinit var pbLoading : ProgressBar
    companion object{
        fun newInstance() : TaskFragment
        {
            val instance = TaskFragment()
            val bundle = Bundle()
            instance.arguments = bundle
            return instance
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is OnFragmentInteraction){
            interaction = context
        }else{
            throw RuntimeException(" must implemented OnFragmentInteraction")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_task, container, false)
        pbLoading = v.findViewById(R.id.pbLoading)
        pbLoading.visibility = View.VISIBLE

        fabAddTask = v.findViewById(R.id.fabAdd)
        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java);
        val storage = AuthStorage(context);
        val call = taskService.getTaskList("GET",storage.getUserDetails()?:"");
        call.enqueue(object: Callback<NetworkResponse<List<Task>>>{
            override fun onFailure(call: Call<NetworkResponse<List<Task>>>, t: Throwable) {
                Snackbar.make(v, "Произашла ошибка получения данных", Snackbar.LENGTH_LONG)

                Log.d("TaskFragment", t.toString())
                pbLoading.visibility = View.GONE
            }

            override fun onResponse(call: Call<NetworkResponse<List<Task>>>, response: Response<NetworkResponse<List<Task>>>) {
                pbLoading.visibility = View.GONE
                if(response.body()?.code==0)
                {
                    adapter = TaskAdapter(context,response.body()?.data?:listOf())
                    rvTask.adapter = adapter
                }else{
                    tvErrorMsg = v.findViewById(R.id.tvErrorMsg)
                    tvErrorMsg.text = response.body()?.message
                    tvErrorMsg.visibility = View.VISIBLE
                }
            }

        })
        rvTask = v.findViewById(R.id.rvTask)
        rvTask.layoutManager = LinearLayoutManager(context)
        fabAddTask.setOnClickListener{
            interaction.openEditMenu()
        }

        return v
    }
    interface OnFragmentInteraction{
        fun openEditMenu()
    }

}