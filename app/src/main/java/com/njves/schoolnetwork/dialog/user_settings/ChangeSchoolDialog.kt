package com.njves.schoolnetwork.dialog.user_settings

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.Models.network.models.auth.School
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.fragments.settings.UserSettingsFragment
import com.njves.schoolnetwork.presenter.school.ISchool
import com.njves.schoolnetwork.presenter.school.SchoolPresenter

class ChangeSchoolDialog : DialogFragment(), ISchool {
    private var school: Int = 0
    private lateinit var spinnerSchool: Spinner
    companion object{
        private const val SCHOOL_ARG = "school"
        const val SCHOOL_RESULT = "new_school"
        fun newInstance(school: Int): ChangeSchoolDialog{
            val bundle = Bundle()
            bundle.putInt(SCHOOL_ARG,school)
            val instance = ChangeSchoolDialog()
            instance.arguments = bundle
            return instance
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_change_school, null)
        SchoolPresenter(this).getSchool()
        val dialog = AlertDialog.Builder(context)
        dialog.setView(view)
        spinnerSchool = view.findViewById(R.id.spinnerSchool)
        spinnerSchool.setSelection(school, true)
        dialog.setPositiveButton(R.string.action_submit) { dialog, which ->
            val intent = Intent()
            intent.putExtra(SCHOOL_RESULT, (spinnerSchool.selectedItem as School).index)
            targetFragment!!.onActivityResult(UserSettingsFragment.SCHOOL_DIALOG, Activity.RESULT_OK, intent)
        }
        return dialog.create()
    }

    override fun onSuccess(positionList: List<School>) {
        spinnerSchool.adapter = ArrayAdapter(context!!, R.layout.support_simple_spinner_dropdown_item, positionList)
    }

    override fun onError(message: String?) {
        Toast.makeText(context,"Неудалось получить список школ", Toast.LENGTH_SHORT).show()
    }

    override fun onFail(t: Throwable) {
        Toast.makeText(context, "Произашла ошибка сервера при получение списка школ", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}