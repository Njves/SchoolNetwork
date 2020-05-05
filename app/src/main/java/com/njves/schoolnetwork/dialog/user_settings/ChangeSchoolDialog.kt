package com.njves.schoolnetwork.dialog.user_settings

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ChangeSchoolDialog : DialogFragment() {
    private var school: Int? = null
    companion object{
        private const val SCHOOL_ARG = "school"
        const val SCHOOL_RESULT = "new_school"
        fun newInstance(school: Int): ChangeSchoolDialog{
            val bundle = Bundle()
            bundle.putInt(SCHOOL_ARG,school)
            return ChangeSchoolDialog()
        }
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}