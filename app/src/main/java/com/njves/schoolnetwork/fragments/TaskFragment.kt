package com.njves.schoolnetwork.fragments

import android.content.Context
import android.content.Intent
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
import com.njves.schoolnetwork.dialog.SubmitActionDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TaskFragment : Fragment(), OnRecyclerViewTaskOnItemClickListener, SwipeRefreshLayout.OnRefreshListener, Callback<NetworkResponse<List<TaskViewModel>>>, TaskAdapter.OnActionTask{

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
        const val FLAG_GETTER = "flag"
        const val FLAG_GET = 0
        const val FLAG_GET_MY = 1
        const val SUBMIT_DIALOG_CODE = 1
        fun newInstance(flag : Int) : TaskFragment{
            val bundle = Bundle()
            bundle.putInt(FLAG_GETTER, flag)
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
        // Флаг запроса на фрагменты
        when(arguments?.get(FLAG_GETTER)){
            FLAG_GET-> {
                val call = taskService.getTaskList("GET", storage.getUserDetails() ?: "")
                call.enqueue(this)
            }
            FLAG_GET_MY-> {
                val call = taskService.getTaskList("GET_MY", storage.getUserDetails() ?: "")
                call.enqueue(this)
            }
        }
        rvTask = v.findViewById(R.id.rvTask)
        rvTask.layoutManager = LinearLayoutManager(context)


        return v
    }
    // Слушатель клика на элемент списка
    override fun onItemClick(task: TaskViewModel) {
        val bundle = Bundle()
        bundle.putString(ARG_TASK, gson.toJson(task))
        val options = NavOptions.Builder()
        options.setEnterAnim(R.anim.nav_default_enter_anim)
        findNavController().navigate(R.id.nav_task_detail, bundle, options.build())

    }
    // Обновление refresh layout
    override fun onRefresh() {
        swipeLayout.isRefreshing = false
        flagCalling()
        }

    // Методы запроса
    override fun onResponse(call: Call<NetworkResponse<List<TaskViewModel>>>,
                            response: Response<NetworkResponse<List<TaskViewModel>>>) {
        pbLoading.visibility = View.GONE
        if (response.body()?.code == 0) {
            adapter = TaskAdapter(context, response.body()?.data!!, this@TaskFragment)
            rvTask.adapter = adapter
        } else {
            tvErrorMsg = view!!.findViewById(R.id.tvErrorMsg)
            tvErrorMsg.text = response.body()?.message
            tvErrorMsg.visibility = View.VISIBLE
        }
    }

    override fun onFailure(call: Call<NetworkResponse<List<TaskViewModel>>>, t: Throwable) {
        Snackbar.make(view!!, "Произашла ошибка получения данных", Snackbar.LENGTH_LONG)
        Log.d(TAG, t.toString())
        pbLoading.visibility = View.GONE
    }
    // Коллбэк на удаление
    override fun onDelete(index : Int) {
        val submitDialog = SubmitActionDialog(SubmitActionDialog.MODE_DELETE)
        setTargetFragment(submitDialog, SUBMIT_DIALOG_CODE)
        submitDialog.show(fragmentManager, "submit_dialog")
    }
    // Результат на диалоги
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1)
        {
            when(resultCode)
            {
                SUBMIT_DIALOG_CODE->{

                }
            }
        }
    }

    private fun flagCalling(){
        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)
        val storage = AuthStorage(context)
        lateinit var call : Call<NetworkResponse<List<TaskViewModel>>>
        when(arguments?.get(FLAG_GETTER)){
            FLAG_GET->{
                call = taskService.getTaskList("GET", storage.getUserDetails() ?: "")
            }
            FLAG_GET_MY->{
                call = taskService.getTaskList("GET_MY", storage.getUserDetails() ?: "")
            }
        }
        call?.let {
            it.enqueue(this)
        }
    }
}