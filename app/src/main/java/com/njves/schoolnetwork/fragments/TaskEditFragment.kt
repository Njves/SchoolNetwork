package com.njves.schoolnetwork.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
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
import com.njves.schoolnetwork.Models.network.models.task.RequestTaskModel
import com.njves.schoolnetwork.Models.network.models.task.TaskPostModel
import com.njves.schoolnetwork.Models.network.request.TaskService
import com.njves.schoolnetwork.Models.network.request.TeachersService
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.preferences.AuthStorage
import com.njves.schoolnetwork.adapter.ReceiversAdapter
import com.njves.schoolnetwork.dialog.AuthErrorDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TaskEditFragment : Fragment(){
    lateinit var etTitle : EditText
    lateinit var etDescription : EditText
    lateinit var btnDatePicker: Button
    lateinit var rvReceivers : RecyclerView
    lateinit var btnSubmit : Button
    var datePick = GregorianCalendar()
    lateinit var adapter : ReceiversAdapter

    companion object{
        const val TAG = "TaskEditFragment"
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = layoutInflater.inflate(R.layout.fragment_task_edit, container, false)
        Log.i(TAG, "OnCreate TaskEditFragment")
        // init fragment menu
        setHasOptionsMenu(true)
        // init recycler view
        rvReceivers = v.findViewById(R.id.rvReceivers)
        rvReceivers.layoutManager = LinearLayoutManager(context)
        Log.d("TaskEditFragment", "OnCreateView TaskEditFragment")

        // Получаем список учителей
        val teachersService = NetworkService.instance.getRetrofit().create(TeachersService::class.java)
        val call = teachersService.getTeacherList(1,1)
        call.enqueue(object : Callback<NetworkResponse<List<Profile>>>{
            override fun onFailure(call: Call<NetworkResponse<List<Profile>>>, t: Throwable) {
                Toast.makeText(context, "Не удалось получить список учителей", Toast.LENGTH_LONG).show()
                Log.e(TAG, "On failure get teachers list, throwable $t")
            }

            override fun onResponse(
                call: Call<NetworkResponse<List<Profile>>>,
                response: Response<NetworkResponse<List<Profile>>>) {
                adapter = ReceiversAdapter(context, response.body()?.data?:listOf())
                rvReceivers.adapter = adapter
                Log.i(TAG, "OnResponse TeachersList")
            }
        })
        etTitle = v.findViewById(R.id.edTitle)
        etDescription = v.findViewById(R.id.edDescription)
        btnDatePicker = v.findViewById(R.id.btnDatePicker)
        btnSubmit = v.findViewById(R.id.btnSubmit)
        btnDatePicker.setOnClickListener{
            val dateSet = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                this.datePick = GregorianCalendar(year, month, dayOfMonth)
            }
            val dialog = DatePickerDialog(context!!,dateSet,datePick.get(Calendar.YEAR),datePick.get(Calendar.MONTH), datePick.get(Calendar.DAY_OF_MONTH))
            dialog.show()
        }
        btnSubmit.setOnClickListener {
            submitData()
        }

        return v
    }
    private fun submitData(){
        val storage = AuthStorage(context)
        val title = etTitle.text.toString()
        val desc = etDescription.text.toString()
        val receiver = adapter.getUser()?.uid
        val taskService = NetworkService.instance.getRetrofit().create(TaskService::class.java)

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        Log.d(TAG, "$title, $desc, $receiver, ${sdf.format(datePick.time)} ${datePick.time} ${datePick.time.time/1000}")
        val postCall = taskService.postCallTask(
            RequestTaskModel(
                "POST",
                TaskPostModel(0, title, desc, datePick.time.time/1000, storage.getUserDetails(), receiver)
            )
        )
        postCall.enqueue(object : Callback<NetworkResponse<TaskPostModel>> {
            override fun onFailure(call: Call<NetworkResponse<TaskPostModel>>, t: Throwable) {
                Toast.makeText(context, "Не удалось отправить задачу", Toast.LENGTH_LONG).show()
                Log.e(TAG, "Failure to send task, throwable: $t")
            }

            override fun onResponse(
                call: Call<NetworkResponse<TaskPostModel>>,
                response: Response<NetworkResponse<TaskPostModel>>
            ) {
                Log.i(TAG, "Response call to create task")
                val code = response.body()?.code
                val message = response.body()?.message
                if (code == 0) {
                    view?.let {
                        Snackbar.make(view!!, "Задача была успешно отправлена", Snackbar.LENGTH_SHORT).show()
                    }
                    // Закрыть фрагмент
                    findNavController().navigateUp()
                } else {
                    AuthErrorDialog.newInstance(message).show(activity?.supportFragmentManager, "dialogError")
                }
            }

        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        val action = menu?.findItem(R.id.action_about)
        action?.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.action_done->{
                submitData()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}