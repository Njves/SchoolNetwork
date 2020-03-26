package com.njves.schoolnetwork.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.njves.schoolnetwork.R
import com.njves.schoolnetwork.fragments.TaskFragment

class SubmitActionDialog(val MODE : Int): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
        // TODO: Название задачи
        dialog.setTitle("Задача")
        dialog.setMessage(getActionText(MODE))
        dialog.setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
            val intent = Intent()

            //parentFragment?.onActivityResult(Activity.RESULT_OK, TaskFragment.SUBMIT_DIALOG_CODE, )
        }
        dialog.setNegativeButton("Нет"){ dialogInterface: DialogInterface, i: Int ->

        }
        return dialog.create()
    }

    private fun getActionText(MODE : Int) : String
    {
        when(MODE){
            MODE_DELETE->{
                return context?.resources?.getString(R.string.warning_action_delete, TEXT_DELETE)!!
            }

        }
        return "Вы действительно хотите это сделать?"
    }
    companion object{
        const val MODE_DELETE = 0
        const val TEXT_DELETE = "Удалить"
    }

}