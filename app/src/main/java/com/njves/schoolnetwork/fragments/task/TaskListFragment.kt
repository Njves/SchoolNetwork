package com.njves.schoolnetwork.fragments.task

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
import com.njves.schoolnetwork.preferences.StatusPreferences
import com.njves.schoolnetwork.presenter.task.task_list.TaskListContract
import com.njves.schoolnetwork.presenter.task.task_list.TaskPresenter
import java.util.ArrayList

class TaskListFragment : Fragment(), TaskListContract {

    private lateinit var rvTask : RecyclerView
    private lateinit var adapter : TaskAdapter
    private lateinit var pbLoading : ProgressBar
    private lateinit var swipeLayout : SwipeRefreshLayout
    private lateinit var tvErrorMsg : TextView
    private lateinit var fabTaskEdit: FloatingActionButton
    private lateinit var taskPresenter: TaskPresenter
    private lateinit var storage: AuthStorage
    private var flag : Int = 0

    companion object{
        const val TAG  = "TaskListFragment"
        const val FLAG_GETTER = "flag"
        const val FLAG_GET = 0
        const val FLAG_GET_MY = 1
        const val SUBMIT_DIALOG_CODE = 1
        fun newInstance(flag : Int) : TaskListFragment {
            val bundle = Bundle()
            bundle.putInt(FLAG_GETTER, flag)
            val instance = TaskListFragment()
            instance.arguments = bundle
            return instance
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Прогрес бар
        pbLoading = view.findViewById(R.id.pbLoading)
        pbLoading.visibility = View.VISIBLE
        // TextView для ошибок
        tvErrorMsg = view.findViewById(R.id.tvErrorMsg)
        // Презентер данных
        taskPresenter = TaskPresenter(this)

        // Свайп лэйаут
        swipeLayout = view.findViewById(R.id.swipeLayout)
        swipeLayout.setOnRefreshListener(taskPresenter)

        storage = AuthStorage(context)
        // Флаг запроса на фрагменты

        rvTask = view.findViewById(R.id.rvTask)
        rvTask.layoutManager = LinearLayoutManager(context)

    }
    override fun onNavigateToDetail(task: Task) {
        val gson = Gson()
        val bundle = Bundle()
        bundle.putString(TaskDetailFragment.ARG_TASK, gson.toJson(task))
        val options = NavOptions.Builder()
        options.setEnterAnim(R.anim.nav_default_enter_anim)
        findNavController().navigate(R.id.nav_task_detail, bundle, options.build())
    }
    fun onReceiveTaskMode(flag: Int){
        when(flag){
            FLAG_GET -> {
                updateTaskMode(flag)
            }
            FLAG_GET_MY -> {
                updateTaskMode(flag)
            }
        }
    }
    private fun updateTaskMode(type: Int){
        taskPresenter.getTaskList("GET",AuthStorage.getInstance(requireContext()).getUserDetails() ?: return)
    }
    override fun onResponseList(taskList: List<Task>) {
        val pref = StatusPreferences(context)
        taskList.forEach {
            it.status = pref.getStatus(it.uid!!)
        }
        adapter = TaskAdapter(context, taskList as ArrayList<Task>, taskPresenter)
        rvTask.adapter = adapter
        Log.d(TAG, taskList.hashCode().toString())
    }


    override fun onResponseEmptyList() {
        showErrorMsg("У вас пока что нет задач")
    }

    override fun showConfirmDialog() {

    }

    override fun onError(message: String?) {
        Snackbar.make(view!!, "Ошибка: $message", Snackbar.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Log.d(TAG, "TaskListFragment throwable: $t")
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
