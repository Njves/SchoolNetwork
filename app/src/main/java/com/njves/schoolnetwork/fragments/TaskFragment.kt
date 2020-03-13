package com.njves.schoolnetwork.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.NetworkService
import com.njves.schoolnetwork.Models.network.models.NetworkResponse
import com.njves.schoolnetwork.Models.network.models.task.TaskViewModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.Storage.AuthStorage
import com.njves.schoolnetwork.adapter.TaskAdapter
import com.njves.schoolnetwork.callback.OnRecyclerViewTaskOnItemClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskFragment : Fragment(), OnRecyclerViewTaskOnItemClickListener, SwipeRefreshLayout.OnRefreshListener, Callback<NetworkResponse<List<TaskViewModel>>>{

    private lateinit var rvTask : RecyclerView
    private lateinit var adapter : TaskAdapter
    private lateinit var tvErrorMsg : TextView


    private lateinit var pbLoading : ProgressBar
    private lateinit var gson : Gson
    private lateinit var swipeLayout : SwipeRefreshLayout
    private var flag : Int = 0

    companion object{
        const val TAG  = "TaskFragment"
        const val ARG_TASK = "task"
        // TODO: Поменять
        const val FLAG = "flag"
        const val FLAG_GET = 0
        const val FLAG_MY = 1
        fun newInstance(flag : Int) : TaskFragment{
            val bundle = Bundle()
            bundle.putInt(FLAG, flag)
            val instance = TaskFragment()
            instance.arguments = bundle
            return instance
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_task, container, false)
        pbLoading = v.findViewById(R.id.pbLoading)
        pbLoading.visibility = View.VISIBLE


        gson = Gson()
        swipeLayout = v.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener(this)

        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
        val storage = AuthStorage(context)
        val flag = arguments?.get(FLAG)
        if(flag==0) {
            val call = taskService.getTaskList("GET", storage.getUserDetails() ?: "")
            call.enqueue(object : Callback<NetworkResponse<List<TaskViewModel>>> {
                override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {
                    Snackbar.make(v, "Произашла ошибка получения данных", Snackbar.LENGTH_LONG)

                    Log.d(TAG, t.toString())
                    pbLoading.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<NetworkResponse<List<TaskViewModel>>>,
                    response: Response<NetworkResponse<List<TaskViewModel>>>
                ) {
                    pbLoading.visibility = View.GONE
                    if (response.body()?.code == 0) {
                        adapter = TaskAdapter(context, response.body()?.data!!, this@TaskFragment)
                        rvTask.adapter = adapter
                    } else {
                        tvErrorMsg = v.findViewById(R.id.tvErrorMsg)
                        tvErrorMsg.text = response.body()?.message
                        tvErrorMsg.visibility = View.VISIBLE
                    }
                }

            })
        }else if(flag==1)
        {
            val call = taskService.getMyTaskList("GET_MY", storage.getUserDetails() ?: "")
            call.enqueue(object : Callback<NetworkResponse<List<TaskViewModel>>> {
                override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {
                    Snackbar.make(v, "Произашла ошибка получения данных", Snackbar.LENGTH_LONG)
                    Log.d(TAG, t.toString())
                    pbLoading.visibility = View.GONE
                }

                override fun onResponse(
                    call: Call<NetworkResponse<List<TaskViewModel>>>,
                    response: Response<NetworkResponse<List<TaskViewModel>>>
                ) {

                    pbLoading.visibility = View.GONE
                    if (response.body()?.code == 0) {
                        Log.d(TAG, response.body().toString())
                        adapter = TaskAdapter(context, response.body()?.data!!, this@TaskFragment)
                        rvTask.adapter = adapter
                    } else {
                        tvErrorMsg = v.findViewById(R.id.tvErrorMsg)
                        tvErrorMsg.text = response.body()?.message
                        tvErrorMsg.visibility = View.VISIBLE
                    }
                }

            })
        }
        rvTask = v.findViewById(R.id.rvTask)
        rvTask.layoutManager = LinearLayoutManager(context)


        return v
    }

    override fun onItemClick(task: TaskViewModel) {
        val bundle = Bundle()
        bundle.putString(ARG_TASK, gson.toJson(task))
        val options = NavOptions.Builder()
        options.setEnterAnim(R.anim.nav_default_enter_anim)
        findNavController().navigate(R.id.nav_task_detail, bundle, options.build())
        
    }

    override fun onRefresh() {
        swipeLayout.isRefreshing = false

        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
        val storage = AuthStorage(context)
        val call = taskService.getTaskList("GET",storage.getUserDetails()?:"")
        call.enqueue(object: Callback<NetworkResponse<List<TaskViewModel>>>{
            override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {

                Log.d(TAG, t.toString())
                pbLoading.visibility = View.GONE
            }

            override fun onResponse(call: Call<NetworkResponse<List<TaskViewModel>>>, response: Response<NetworkResponse<List<TaskViewModel>>>) {
                pbLoading.visibility = View.GONE
                if(response.body()?.code==0)
                {
                    adapter = TaskAdapter(context,response.body()?.data!!, this@TaskFragment)
                    rvTask.adapter = adapter
                }else{
                    
                    
                    tvErrorMsg.text = response.body()?.message
                    tvErrorMsg.visibility = View.VISIBLE
                }
            }

        })
    }

    override fun onResponse(call: Call<NetworkResponse<List<TaskViewModel>>>,
                            response: Response<NetworkResponse<List<TaskViewModel>>>) {
        pbLoading.visibility = View.GONE
        adapter = TaskAdapter(context,response.body()?.data!!, this@TaskFragment)
        rvTask.adapter = adapter
    }

    override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {

    }


}