package com.njves.schoolnetwork.fragments.task

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.adapter.TaskAdapter
import com.njves.schoolnetwork.presenter.task.task_list.ITask
import com.njves.schoolnetwork.presenter.task.task_list.TaskPresenter
import java.util.ArrayList

class TaskFragment : Fragment(), ITask {

    private lateinit var rvTask : RecyclerView
    private lateinit var adapter : TaskAdapter
    private lateinit var pbLoading : ProgressBar
    private lateinit var gson : Gson
    private lateinit var swipeLayout : SwipeRefreshLayout
    private lateinit var tvErrorMsg : TextView
    private lateinit var fabTaskEdit: FloatingActionButton
    private lateinit var taskPresenter: TaskPresenter
    private lateinit var storage: AuthStorage
    private var flag : Int = 0

    companion object{
        const val TAG  = "TaskFragment"
        const val FLAG_GETTER = "flag"
        const val FLAG_GET = 0
        const val FLAG_GET_MY = 1
        const val SUBMIT_DIALOG_CODE = 1
        fun newInstance(flag : Int) : TaskFragment {
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
        tvErrorMsg = v.findViewById(R.id.tvErrorMsg)
        taskPresenter = TaskPresenter(this)
        gson = Gson()
        swipeLayout = v.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener(taskPresenter)

        storage = AuthStorage(context)
        // Флаг запроса на фрагменты
        when(arguments?.get(FLAG_GETTER)){
            FLAG_GET -> {
                taskPresenter.getTaskList("GET", storage.getUserDetails()!!)
            }
            FLAG_GET_MY -> {
                taskPresenter.getTaskList("GET_MY", storage.getUserDetails()!!)
            }
        }
        rvTask = v.findViewById(R.id.rvTask)
        rvTask.layoutManager = LinearLayoutManager(context)


        return v
    }

    override fun onNavigateToDetail(task: Task) {
        val bundle = Bundle()
        bundle.putString(TaskDetailFragment.ARG_TASK, gson.toJson(task))
        val options = NavOptions.Builder()
        options.setEnterAnim(R.anim.nav_default_enter_anim)
        findNavController().navigate(R.id.nav_task_detail, bundle, options.build())
    }

    override fun onResponseList(taskList: List<Task>) {
        adapter = TaskAdapter(context, taskList as ArrayList<Task>, taskPresenter)
        rvTask.adapter = adapter
        Log.d(TAG, taskList.hashCode().toString())
    }

    override fun onResponseEmptyList() {
        showErrorMsg("У вас пока что нет задач")
    }

    override fun showConfirmDialog() {

    }

    override fun onError(message: String) {
        Snackbar.make(view!!, "Ошибка: $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Log.d(TAG, "TaskFragment throwable: $t")
        Snackbar.make(view!!, "Произошла ошибка получения данных", Snackbar.LENGTH_INDEFINITE).show()
    }

    override fun showProgressBar() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pbLoading.visibility = View.GONE
    }

    override fun onRefresh() {
        swipeLayout.isRefreshing = false
        when(arguments?.get(FLAG_GETTER)){
            FLAG_GET -> {
                taskPresenter.getTaskList("GET", storage.getUserDetails()!!)
            }
            FLAG_GET_MY -> {
                taskPresenter.getTaskList("GET_MY", storage.getUserDetails()!!)
            }
        }
    }

    private fun showErrorMsg(message : String){
        tvErrorMsg.text = message
        tvErrorMsg.visibility = View.VISIBLE
    }



}
