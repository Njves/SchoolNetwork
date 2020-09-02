package com.njves.schoolnetwork.fragments.task

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.njves.schoolnetwork.Models.network.models.profile.Profile
import com.njves.schoolnetwork.Models.network.models.task.Task
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.adapter.ReceiversAdapter
import com.njves.schoolnetwork.dialog.ErrorDialog
import com.njves.schoolnetwork.preferences.ProfilePreferences
import com.njves.schoolnetwork.presenter.task.task_edit.ITaskEdit
import com.njves.schoolnetwork.presenter.task.task_edit.TaskEditPresenter
import java.util.*

class TaskEditFragment : Fragment(), ITaskEdit {
    lateinit var etTitle: EditText
    lateinit var etDescription: EditText
    lateinit var btnDatePicker: Button
    lateinit var rvReceivers : RecyclerView
    lateinit var btnSubmit: Button
    lateinit var pb: ProgressBar
    var datePick = GregorianCalendar()
    lateinit var adapter : ReceiversAdapter
    private var presenter = TaskEditPresenter(this)

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
        pb = v.findViewById(R.id.pbLoading)
        rvReceivers.layoutManager = LinearLayoutManager(context)
        
        // Получаем список учителей
        presenter.getListProfile(ProfilePreferences.getInstance(context).getLocalUserProfile()?.position!!,1)
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

        val title = etTitle.text.toString()
        val desc = etDescription.text.toString()
        val sender = ProfilePreferences.getInstance(context).getLocalUserProfile()
        val receiver = adapter.getUser()
        val task = Task(0, title, desc, datePick.time.time/1000, ProfilePreferences.getInstance(context).getLocalUserProfile()!!, receiver!!, null)
        presenter.sendTask(task)
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

    override fun onSuccessGetList(list: List<Profile>) {
        adapter = ReceiversAdapter(context, list)
        rvReceivers.adapter = adapter
    }

    override fun pickDate() {

    }

    override fun onSuccessSend() {
        view?.let {
            Snackbar.make(view!!, "Задача была успешно отправлена", Snackbar.LENGTH_SHORT).show()
        }
        // Закрыть фрагмент
        findNavController().navigateUp()
    }

    override fun onError(message: String?) {
        ErrorDialog.newInstance(message).show(activity?.supportFragmentManager, "dialogError")
    }

    override fun onFail(t: Throwable) {
        Toast.makeText(context, "Не удалось отправить задачу", Toast.LENGTH_LONG).show()
        Log.e(TAG, t.toString())
    }

    override fun showProgressBar() {
        pb.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        pb.visibility = View.GONE
    }
}